package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.Plugin;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by michael on 05.03.16.
 */
@Repository
public class InMemoryProcessRepository implements ProcessRepository {

    private Map<Plugin,Process> processes = new ConcurrentHashMap<>();

    @Inject
    private ClientDetailsService clientDetailsService;

    @PostConstruct
    protected void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            for(Process process : processes.values()) {
                process.destroy();
            }
            processes.clear();
        }));
    }

    @Override
    public boolean isRunning(Plugin plugin) {
        Optional<Process> process = Optional.ofNullable(processes.get(plugin));
        if (process.isPresent()) {
            if (process.get().isAlive()) {
                return true;
            } else {
                processes.remove(plugin);
                return false;
            }
        }
        return false;
    }

    @Override
    public Process create(Plugin plugin) throws IOException {
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(plugin.getIdentifier());
        String clientId = clientDetails.getClientId();
        String clientSecret = clientDetails.getClientSecret();
        File log = new File(FilenameUtils.concat(plugin.getPath(), plugin.getIdentifier() + ".log"));

        ProcessBuilder processBuilder = new ProcessBuilder(plugin.getCommand().split(" "));
        processBuilder.directory(new File(plugin.getPath()));
        processBuilder.environment().put("NODE_ENV","production");
        processBuilder.environment().put("PORT","8081");
        processBuilder.environment().put("ID",clientId);
        processBuilder.environment().put("SECRET",clientSecret);
        processBuilder.redirectError(log);
        processBuilder.redirectOutput(log);
        Process process = processBuilder.start();

        processes.put(plugin,process);

        return process;
    }

    @Override
    public boolean stop(Plugin plugin) {
        Optional<Process> process = Optional.ofNullable(processes.get(plugin));
        if (process.isPresent()) {
            process.get().destroy();
            processes.remove(plugin);

            return true;
        }
        return false;
    }
}
