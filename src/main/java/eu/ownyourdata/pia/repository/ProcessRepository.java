package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.Plugin;

import java.io.IOException;

/**
 * Created by michael on 05.03.16.
 */
public interface ProcessRepository {
    boolean isRunning(Plugin plugin);
    Process create(Plugin plugin) throws IOException;
    boolean stop(Plugin plugin);
}
