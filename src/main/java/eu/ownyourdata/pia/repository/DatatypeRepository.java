package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.Datatype;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Datatype entity.
 */
public interface DatatypeRepository extends JpaRepository<Datatype,Long> {

}
