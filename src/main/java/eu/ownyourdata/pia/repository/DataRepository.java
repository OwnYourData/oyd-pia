package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.Data;
import eu.ownyourdata.pia.domain.Datatype;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Data entity.
 */
public interface DataRepository extends JpaRepository<Data,Long> {
    Page<Data> findAllByType(Datatype type, Pageable pageable);
    List<Data> removeByType(Datatype type);
}
