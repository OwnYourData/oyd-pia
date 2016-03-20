package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.DatatypeCount;
import eu.ownyourdata.pia.domain.Datatype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Datatype entity.
 */
public interface DatatypeRepository extends JpaRepository<Datatype,Long> {
    Optional<Datatype> findOneByName(String name);

    @Query("Select new eu.ownyourdata.pia.domain.DatatypeCount(type.name, count(data.id)) from Datatype type left join type.data as data group by type.name")
    List<DatatypeCount> getCounts();

}
