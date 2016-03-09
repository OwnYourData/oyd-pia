package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Plugin entity.
 */
public interface PluginRepository extends JpaRepository<Plugin,Long>, PluginRepositoryCustom {

    List<Plugin> findByType(String type);
}
