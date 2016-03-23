package eu.ownyourdata.pia.service.impl;

import eu.ownyourdata.pia.domain.Repo;
import eu.ownyourdata.pia.repository.RepoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 23.03.16.
 */
public class RepoServiceImplTest {

    @Mock
    private RepoRepository repoRepository;

    @InjectMocks
    private  RepoServiceImpl repoService;

    private Repo repo1;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        repo1 = new Repo();
        repo1.setIdentifier("repo1");
        repo1.setDescription("repo1 description");
    }

    @Test
    public void getExistingByIdentifier() {
        when(repoRepository.findOneByIdentifier(repo1.getIdentifier())).thenReturn(Optional.of(repo1));

        Repo found = repoService.getByIdentifier(repo1.getIdentifier(), new Repo());

        assertThat(repo1,equalTo(found));
    }

    @Test
    public void getNonExistingByIdentifier() {
        when(repoRepository.findOneByIdentifier(repo1.getIdentifier())).thenReturn(Optional.empty());
        when(repoRepository.save(repo1)).thenReturn(repo1);

        Repo found = repoService.getByIdentifier(repo1.getIdentifier(), repo1);

        assertThat(found,equalTo(repo1));
        verify(repoRepository).save(repo1);

    }
}
