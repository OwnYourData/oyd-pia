package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.Data;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Data entity.
 */
public interface DataRepository extends JpaRepository<Data,Long> {

}
