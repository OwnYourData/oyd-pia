package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.plugin.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Plugin entity.
 */
public interface PluginRepository extends JpaRepository<Plugin,Long>, PluginRepositoryCustom {
    Optional<Plugin> findOneByIdentifier(String identifier);
    //List<Plugin> findByType(String type);
}
