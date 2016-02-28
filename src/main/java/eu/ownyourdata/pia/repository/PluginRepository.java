package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Plugin entity.
 */
public interface PluginRepository extends JpaRepository<Plugin,Long>, PluginRepositoryCustom {

}
