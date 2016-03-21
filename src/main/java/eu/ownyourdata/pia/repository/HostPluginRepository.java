package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.plugin.HostPlugin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by michael on 21.03.16.
 */
public interface HostPluginRepository extends JpaRepository<HostPlugin,Long> {
    Optional<HostPlugin> findOneByIdentifier(String identifier);
}
