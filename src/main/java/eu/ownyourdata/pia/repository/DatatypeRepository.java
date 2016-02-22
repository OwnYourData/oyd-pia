package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.Datatype;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Datatype entity.
 */
public interface DatatypeRepository extends JpaRepository<Datatype,Long> {
    Optional<Datatype> findOneByName(String name);
}
