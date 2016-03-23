package eu.ownyourdata.pia.web.rest;

import eu.ownyourdata.pia.Application;
import eu.ownyourdata.pia.domain.Item;
import eu.ownyourdata.pia.domain.Repo;
import eu.ownyourdata.pia.repository.ItemRepository;
import eu.ownyourdata.pia.repository.RepoRepository;
import eu.ownyourdata.pia.service.ItemService;
import eu.ownyourdata.pia.service.RepoService;

import eu.ownyourdata.pia.web.rest.mapper.ItemMapper;
import org.boon.di.In;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RepoResource REST controller.
 *
 * @see RepoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RepoResourceIntTest {

    private static final String DEFAULT_IDENTIFIER = "eu.ownyourdata.test";
    private static final String UPDATED_IDENTIFIER = "eu.ownyourdata.test2";
    private static final String DEFAULT_DESCRIPTION = "Some Description";
    private static final String UPDATED_DESCRIPTION = "Other Description";
    private static final String DEFAULT_CREATOR = "Creator A";
    private static final String UPDATED_CREATOR = "Creator B";

    @Inject
    private RepoRepository repoRepository;

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private RepoService repoService;

    @Inject
    private ItemService itemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private ItemMapper itemMapper;

    private MockMvc restRepoMockMvc;

    private Repo repo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RepoResource repoResource = new RepoResource();
        ReflectionTestUtils.setField(repoResource, "repoService", repoService);
        ReflectionTestUtils.setField(repoResource, "itemService", itemService);
        ReflectionTestUtils.setField(repoResource, "itemMapper", itemMapper);
        this.restRepoMockMvc = MockMvcBuilders.standaloneSetup(repoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        repo = new Repo();
        repo.setIdentifier(DEFAULT_IDENTIFIER);
        repo.setDescription(DEFAULT_DESCRIPTION);
        repo.setCreator(DEFAULT_CREATOR);
    }

    @Test
    @Transactional
    public void createRepo() throws Exception {
        int databaseSizeBeforeCreate = repoRepository.findAll().size();

        // Create the Repo

        restRepoMockMvc.perform(post("/api/repos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repo)))
                .andExpect(status().isCreated());

        // Validate the Repo in the database
        List<Repo> repos = repoRepository.findAll();
        assertThat(repos).hasSize(databaseSizeBeforeCreate + 1);
        Repo testRepo = repos.get(repos.size() - 1);
        assertThat(testRepo.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testRepo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRepo.getCreator()).isEqualTo(DEFAULT_CREATOR);
    }

    @Test
    @Transactional
    public void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = repoRepository.findAll().size();
        // set the field null
        repo.setIdentifier(null);

        // Create the Repo, which fails.

        restRepoMockMvc.perform(post("/api/repos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repo)))
                .andExpect(status().isBadRequest());

        List<Repo> repos = repoRepository.findAll();
        assertThat(repos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRepos() throws Exception {
        // Initialize the database
        repoRepository.saveAndFlush(repo);

        // Get all the repos
        restRepoMockMvc.perform(get("/api/repos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(repo.getId().intValue())))
                .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR.toString())));
    }

    @Test
    @Transactional
    public void getRepo() throws Exception {
        // Initialize the database
        repoRepository.saveAndFlush(repo);

        // Get the repo
        restRepoMockMvc.perform(get("/api/repos/{id}", repo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(repo.getId().intValue()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRepo() throws Exception {
        // Get the repo
        restRepoMockMvc.perform(get("/api/repos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRepo() throws Exception {
        // Initialize the database
        repoRepository.saveAndFlush(repo);

		int databaseSizeBeforeUpdate = repoRepository.findAll().size();

        // Update the repo
        repo.setIdentifier(UPDATED_IDENTIFIER);
        repo.setDescription(UPDATED_DESCRIPTION);
        repo.setCreator(UPDATED_CREATOR);

        restRepoMockMvc.perform(put("/api/repos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repo)))
                .andExpect(status().isOk());

        // Validate the Repo in the database
        List<Repo> repos = repoRepository.findAll();
        assertThat(repos).hasSize(databaseSizeBeforeUpdate);
        Repo testRepo = repos.get(repos.size() - 1);
        assertThat(testRepo.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testRepo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRepo.getCreator()).isEqualTo(UPDATED_CREATOR);
    }

    @Test
    @Transactional
    public void deleteRepo() throws Exception {
        // Initialize the database
        repoRepository.saveAndFlush(repo);

		int databaseSizeBeforeDelete = repoRepository.findAll().size();

        // Get the repo
        restRepoMockMvc.perform(delete("/api/repos/{id}", repo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Repo> repos = repoRepository.findAll();
        assertThat(repos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void createRepoImplicitly() throws Exception {
        JSONObject json = new JSONObject("{'valueA':1, 'valueB':2}");

        restRepoMockMvc.perform(post("/api/repos/eu.ownyourdata.test/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(json)))
            .andExpect(status().isOk());

        Optional<Repo> repo = repoRepository.findOneByIdentifier("eu.ownyourdata.test");

        assertThat(repo).isPresent();
    }

    @Test
    @Transactional
    public void createRepoItem() throws Exception {
        JSONObject json = new JSONObject("{'valueA':1, 'valueB':2}");

        MvcResult result =restRepoMockMvc.perform(post("/api/repos/eu.ownyourdata.test/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(json)))
            .andExpect(status().isOk())
            .andReturn();

        JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());

        assertThat(jsonResult.optLong("id")).isNotNull();
        assertThat(jsonResult.optInt("valueA")).isEqualTo(1);
        assertThat(jsonResult.optInt("valueB")).isEqualTo(2);

        Item item = itemRepository.findOne(jsonResult.getLong("id"));
        assertThat(item).isNotNull();
        assertThat(item.getBelongs().getIdentifier()).isEqualTo("eu.ownyourdata.test");
    }

    @Test
    @Transactional
    public void getRepoItemCounts() throws Exception {
        Repo repository = repoRepository.save(repo);

        createItems(repository,5);

        restRepoMockMvc.perform(get("/api/repos/counts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].type").value("eu.ownyourdata.test"))
            .andExpect(jsonPath("$.[0].count").value(5));

    }

    @Test
    @Transactional
    public void updateRepoItem() throws Exception {
        Repo repository = repoRepository.save(repo);

        Item item = new Item();
        item.setValue("{'valueA':1, 'valueB':2}");
        item.setBelongs(repository);

        item = itemRepository.save(item);
        item.setValue("{'valueA':5}");

        MvcResult result = restRepoMockMvc.perform(put("/api/repos/eu.ownyourdata.test/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemMapper.itemToJson(item))))
            .andExpect(status().isOk())
            .andReturn();

        JSONObject found = itemMapper.itemToJson(itemRepository.findOne(item.getId()));

        assertThat(found.optInt("valueA")).isEqualTo(5);
    }

    @Test
    @Transactional
    public void updateDifferentRepoItem() throws Exception {
        Repo repository = repoRepository.save(repo);

        Item item = new Item();
        item.setValue("{'valueA':1, 'valueB':2}");
        item.setBelongs(repository);

        item = itemRepository.save(item);
        item.setValue("{'valueA':5}");

        restRepoMockMvc.perform(put("/api/repos/eu.ownyourdata.other/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemMapper.itemToJson(item))))
            .andExpect(status().isNotFound())
            .andReturn();
    }


    @Test
    @Transactional
    public void deleteRepoItem() throws Exception {
        Repo repository = repoRepository.save(repo);

        Item item = new Item();
        item.setValue("{'valueA':1, 'valueB':2}");
        item.setBelongs(repository);

        item = itemRepository.save(item);

        restRepoMockMvc.perform(delete("/api/repos/eu.ownyourdata.test/items/"+item.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());


       assertThat(itemRepository.findOne(item.getId())).isNull();
    }

    @Test
    @Transactional
    public void deleteDifferentRepoItem() throws Exception {
        Repo repository = repoRepository.save(repo);

        Item item = new Item();
        item.setValue("{'valueA':1, 'valueB':2}");
        item.setBelongs(repository);

        item = itemRepository.save(item);
        item.setValue("{'valueA':5}");

        restRepoMockMvc.perform(delete("/api/repos/eu.ownyourdata.other/items/"+item.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound())
            .andReturn();
    }


    @Test
    @Transactional
    public void getAllRepoItems() throws Exception {
        // Initialize the database
        repoRepository.saveAndFlush(repo);
        createItems(repo,3);

        // Get all the repos
        restRepoMockMvc.perform(get("/api/repos/"+repo.getIdentifier()+"/items"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].value").exists());
    }

    private void createItems(Repo repository,int amount) {
        for(int i=0;i<amount;i++) {
            Item item = new Item();
            item.setValue("{'value': "+i+"}");
            item.setBelongs(repository);

            itemRepository.save(item);
        }
    }


}
