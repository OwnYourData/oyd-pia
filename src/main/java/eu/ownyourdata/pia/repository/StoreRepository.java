package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.sam.Store;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Store entity.
 */
public interface StoreRepository extends JpaRepository<Store,Long> {

}
