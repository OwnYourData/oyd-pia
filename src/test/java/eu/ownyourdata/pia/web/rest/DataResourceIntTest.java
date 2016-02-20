package eu.ownyourdata.pia.web.rest;

import eu.ownyourdata.pia.Application;
import eu.ownyourdata.pia.domain.Data;
import eu.ownyourdata.pia.repository.DataRepository;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DataResource REST controller.
 *
 * @see DataResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DataResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private DataRepository dataRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDataMockMvc;

    private Data data;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataResource dataResource = new DataResource();
        ReflectionTestUtils.setField(dataResource, "dataRepository", dataRepository);
        this.restDataMockMvc = MockMvcBuilders.standaloneSetup(dataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        data = new Data();
        data.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createData() throws Exception {
        int databaseSizeBeforeCreate = dataRepository.findAll().size();

        // Create the Data

        restDataMockMvc.perform(post("/api/datas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(data)))
                .andExpect(status().isCreated());

        // Validate the Data in the database
        List<Data> datas = dataRepository.findAll();
        assertThat(datas).hasSize(databaseSizeBeforeCreate + 1);
        Data testData = datas.get(datas.size() - 1);
        assertThat(testData.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataRepository.findAll().size();
        // set the field null
        data.setValue(null);

        // Create the Data, which fails.

        restDataMockMvc.perform(post("/api/datas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(data)))
                .andExpect(status().isBadRequest());

        List<Data> datas = dataRepository.findAll();
        assertThat(datas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDatas() throws Exception {
        // Initialize the database
        dataRepository.saveAndFlush(data);

        // Get all the datas
        restDataMockMvc.perform(get("/api/datas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(data.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getData() throws Exception {
        // Initialize the database
        dataRepository.saveAndFlush(data);

        // Get the data
        restDataMockMvc.perform(get("/api/datas/{id}", data.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(data.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingData() throws Exception {
        // Get the data
        restDataMockMvc.perform(get("/api/datas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateData() throws Exception {
        // Initialize the database
        dataRepository.saveAndFlush(data);

		int databaseSizeBeforeUpdate = dataRepository.findAll().size();

        // Update the data
        data.setValue(UPDATED_VALUE);

        restDataMockMvc.perform(put("/api/datas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(data)))
                .andExpect(status().isOk());

        // Validate the Data in the database
        List<Data> datas = dataRepository.findAll();
        assertThat(datas).hasSize(databaseSizeBeforeUpdate);
        Data testData = datas.get(datas.size() - 1);
        assertThat(testData.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteData() throws Exception {
        // Initialize the database
        dataRepository.saveAndFlush(data);

		int databaseSizeBeforeDelete = dataRepository.findAll().size();

        // Get the data
        restDataMockMvc.perform(delete("/api/datas/{id}", data.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Data> datas = dataRepository.findAll();
        assertThat(datas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
