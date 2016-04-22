package eu.ownyourdata.pia.domain.process;

import org.springframework.context.ApplicationContext;
import org.springframework.util.SocketUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by michael on 22.04.16.
 */
public class PluginProcessBuilder {
    @Inject
    private ApplicationContext applicationContext;

    private ProcessBuilder processBuilder;

    public PluginProcessBuilder(String... commands) {
        processBuilder = new ProcessBuilder(commands);
    }

    public PluginProcessBuilder directory(File file) {
        processBuilder.directory(file);
        return this;
    }

    public PluginProcessBuilder clientSecret(String secret) {
        processBuilder.environment().put("SECRET",secret);
        return this;
    }

    public PluginProcessBuilder clientIdentifier(String identifier) {
        processBuilder.environment().put("ID",identifier);
        return this;
    }

    public PluginProcessBuilder piaPort(String port) {
        processBuilder.environment().put("PIA_PORT",port);
        return this;
    }

    public PluginProcessBuilder piaInetAddress(InetAddress inetAddress) {
        processBuilder.environment().put("PIA_IP",inetAddress.getHostAddress());
        return this;
    }

    public PluginProcessBuilder anyPluginPort() {
        processBuilder.environment().put("PORT",SocketUtils.findAvailableTcpPort(8000,8050)+"");
        return this;
    }

    public PluginProcessBuilder withNodeProductionEnvironment() {
        processBuilder.environment().put("NODE_ENV","production");
        return this;
    }

    public PluginProcess start() throws IOException {
        return new PluginProcess(processBuilder.start(), processBuilder.environment());
    }

    public PluginProcessBuilder redirectOutput(File destination) {
        processBuilder.redirectOutput(destination);
        return this;
    }

    public PluginProcessBuilder redirectError(File destination) {
        processBuilder.redirectError(destination);
        return this;
    }
}
