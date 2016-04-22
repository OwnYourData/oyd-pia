package eu.ownyourdata.pia.domain.process;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by michael on 22.04.16.
 */
public class PluginProcess {
    private final Process process;
    private final Map<String,String> configuration;

    public PluginProcess(Process process,Map<String,String> configuration) {
        this.process = process;
        this.configuration = new HashMap(configuration);
    }

    public OutputStream getOutputStream() {
        return process.getOutputStream();
    }

    public InputStream getErrorStream() {
        return process.getErrorStream();
    }

    public void destroy() {
        process.destroy();
    }

    public int exitValue() {
        return process.exitValue();
    }

    public int waitFor() throws InterruptedException {
        return process.waitFor();
    }

    public InputStream getInputStream() {
        return process.getInputStream();
    }

    public boolean waitFor(long timeout, TimeUnit unit) throws InterruptedException {
        return process.waitFor(timeout, unit);
    }

    public boolean isAlive() {
        return process.isAlive();
    }

    public Process destroyForcibly() {
        return process.destroyForcibly();
    }

    public int getPort() {
        return Integer.parseInt(configuration.get("PORT"));
    }
}
