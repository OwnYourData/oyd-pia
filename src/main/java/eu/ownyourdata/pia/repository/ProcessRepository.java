package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.plugin.Plugin;
import eu.ownyourdata.pia.domain.plugin.StandalonePlugin;

import java.io.IOException;

/**
 * Created by michael on 05.03.16.
 */
public interface ProcessRepository {
    boolean isRunning(Plugin plugin);
    Process create(StandalonePlugin plugin) throws IOException;
    boolean stop(StandalonePlugin plugin);
}
