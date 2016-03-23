package eu.ownyourdata.pia.service;

import eu.ownyourdata.pia.domain.Repo;
import eu.ownyourdata.pia.domain.RepoItemCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;

/**
 * Service Interface for managing Repo.
 */
public interface RepoService {

    /**
     * Save a repo.
     * @return the persisted entity
     */
    public Repo save(Repo repo);

    /**
     *  get all the repos.
     *  @return the list of entities
     */
    public Page<Repo> findAll(Pageable pageable);

    /**
     *  get the "id" repo.
     *  @return the entity
     */
    public Repo findOne(Long id);

    public Optional<Repo> findOneByIdentifier(String identifier);

    public Repo getByIdentifier(String identifier, Repo defaultValue);

    /**
     *  delete the "id" repo.
     */
    public void delete(Long id);

    public Collection<RepoItemCount> getRepoItemCounts();
}
