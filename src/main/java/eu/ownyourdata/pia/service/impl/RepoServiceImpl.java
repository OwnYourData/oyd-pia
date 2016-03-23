package eu.ownyourdata.pia.service.impl;

import eu.ownyourdata.pia.domain.Repo;
import eu.ownyourdata.pia.domain.RepoItemCount;
import eu.ownyourdata.pia.repository.RepoRepository;
import eu.ownyourdata.pia.service.RepoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;

/**
 * Service Implementation for managing Repo.
 */
@Service
@Transactional
public class RepoServiceImpl implements RepoService{

    private final Logger log = LoggerFactory.getLogger(RepoServiceImpl.class);

    @Inject
    private RepoRepository repoRepository;

    /**
     * Save a repo.
     * @return the persisted entity
     */
    public Repo save(Repo repo) {
        log.debug("Request to save Repo : {}", repo);
        Repo result = repoRepository.save(repo);
        return result;
    }

    /**
     *  get all the repos.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Repo> findAll(Pageable pageable) {
        log.debug("Request to get all Repos");
        Page<Repo> result = repoRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one repo by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Repo findOne(Long id) {
        log.debug("Request to get Repo : {}", id);
        Repo repo = repoRepository.findOne(id);
        return repo;
    }

    public Optional<Repo> findOneByIdentifier(String identifier) {
        log.debug("Request to get Repo : {}",identifier);
        return repoRepository.findOneByIdentifier(identifier);
    }

    public Repo getByIdentifier(String identifier, Repo defaultValue) {
        Optional<Repo> existing = repoRepository.findOneByIdentifier(identifier);

        return existing.isPresent() ? existing.get() : repoRepository.save(defaultValue);
    }

    /**
     *  delete the  repo by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Repo : {}", id);
        repoRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<RepoItemCount> getRepoItemCounts() {
        return repoRepository.getRepoItemCounts();
    }
}
