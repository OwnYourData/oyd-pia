package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.plugin.Plugin;
import eu.ownyourdata.pia.domain.plugin.StandalonePlugin;
import eu.ownyourdata.pia.domain.process.PluginProcess;
import eu.ownyourdata.pia.domain.process.PluginProcessBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by michael on 05.03.16.
 */
@Repository
public class InMemoryProcessRepository implements ProcessRepository {

    private Map<Plugin,PluginProcess> processes = new ConcurrentHashMap<>();

    @Inject
    private ClientDetailsService clientDetailsService;

    @Inject
    private ApplicationContext applicationContext;

    @PostConstruct
    protected void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            for(PluginProcess process : processes.values()) {
                process.destroy();
            }
            processes.clear();
        }));
    }

    @Override
    public int getProcessPort(Plugin plugin) {
       if (isRunning(plugin)) {
           return processes.get(plugin).getPort();
       } else {
           return -1;
       }
    }

    @Override
    public boolean isRunning(Plugin plugin) {
        Optional<PluginProcess> process = Optional.ofNullable(processes.get(plugin));
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
    public PluginProcess create(StandalonePlugin plugin) throws IOException {
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(plugin.getIdentifier());
        String clientId = clientDetails.getClientId();
        String clientSecret = clientDetails.getClientSecret();
        File log = new File(FilenameUtils.concat(plugin.getPath(), plugin.getIdentifier() + ".log"));

        Environment env = applicationContext.getEnvironment();

        PluginProcess process = new PluginProcessBuilder(plugin.getStartCommand().split(" "))
            .directory(new File(plugin.getPath()))
            .withNodeProductionEnvironment()
            .clientIdentifier(clientId)
            .clientSecret(clientSecret)
            .piaInetAddress(InetAddress.getLocalHost())
            .piaPort(env.getProperty("server.port"))
            .anyPluginPort()
            .redirectError(log)
            .redirectOutput(log).start();

        processes.put(plugin,process);

        return process;
    }

    @Override
    public boolean stop(StandalonePlugin plugin) {
        Optional<PluginProcess> process = Optional.ofNullable(processes.get(plugin));
        if (process.isPresent()) {
            process.get().destroy();
            processes.remove(plugin);

            return true;
        }
        return false;
    }
}
