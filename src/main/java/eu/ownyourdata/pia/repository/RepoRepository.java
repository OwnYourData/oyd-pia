package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.Repo;
import eu.ownyourdata.pia.domain.RepoItemCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;


/**
 * Spring Data JPA repository for the Repo entity.
 */
public interface RepoRepository extends JpaRepository<Repo,Long> {
    Optional<Repo> findOneByIdentifier(String identifier);

    @Query("Select new eu.ownyourdata.pia.domain.RepoItemCount(repo.identifier, count(items.id)) from Repo repo left join repo.items as items group by repo.identifier")
    Collection<RepoItemCount> getRepoItemCounts();
}
