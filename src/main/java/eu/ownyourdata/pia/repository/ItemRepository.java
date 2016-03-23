package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.Item;
import eu.ownyourdata.pia.domain.Repo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Item entity.
 */
public interface ItemRepository extends JpaRepository<Item,Long> {
    Page<Item> findAllByBelongs(Repo repo, Pageable pageable);
}
