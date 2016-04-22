package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.plugin.Plugin;
import eu.ownyourdata.pia.domain.plugin.StandalonePlugin;
import eu.ownyourdata.pia.domain.process.PluginProcess;

import java.io.IOException;

/**
 * Created by michael on 05.03.16.
 */
public interface ProcessRepository {
    int getProcessPort(Plugin plugin);
    boolean isRunning(Plugin plugin);
    PluginProcess create(StandalonePlugin plugin) throws IOException;
    boolean stop(StandalonePlugin plugin);
}
