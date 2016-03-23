package eu.ownyourdata.pia.web.rest;

import eu.ownyourdata.pia.Application;
import eu.ownyourdata.pia.domain.Data;
import eu.ownyourdata.pia.domain.Datatype;
import eu.ownyourdata.pia.repository.DataRepository;
import eu.ownyourdata.pia.repository.DatatypeRepository;
import eu.ownyourdata.pia.web.rest.mapper.DataMapper;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import static org.hamcrest.Matchers.hasItem;
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
@Ignore
public class DataResourceIntTest {

    private static final String DEFAULT_VALUE = "{\"value\":5}";
    private static final String UPDATED_VALUE = "{\"value\":8}";

    @Inject
    private DataRepository dataRepository;

    @Inject
    private DatatypeRepository datatypeRepository;

    @Inject
    private DataMapper dataMapper;

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
        ReflectionTestUtils.setField(dataResource, "dataMapper", dataMapper);
        this.restDataMockMvc = MockMvcBuilders.standaloneSetup(dataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();

        Datatype datatype = new Datatype();
        datatype.setName("testdata");

        datatypeRepository.deleteAll();
        datatypeRepository.save(datatype);
    }

    @Before
    public void initTest() {
        data = new Data();
        data.setType(datatypeRepository.findOneByName("testdata").get());
        data.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createData() throws Exception {
        int databaseSizeBeforeCreate = dataRepository.findAll().size();

        // Create the Data
        JSONObject dataDTO = dataMapper.dataToJson(data);

        restDataMockMvc.perform(post("/api/datas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(dataDTO.toString()))
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
        data.setType(null);


        // Create the Data, which fails.
        JSONObject dataDTO = dataMapper.dataToJson(data);

        restDataMockMvc.perform(post("/api/datas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(dataDTO.toString()))
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
                .andExpect(jsonPath("$.[*].value").value(hasItem(5)));
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
            .andExpect(jsonPath("$.value").value(5));
    }

    @Test
    @Transactional
    public void updateData() throws Exception {
        // Initialize the database
        dataRepository.saveAndFlush(data);

		int databaseSizeBeforeUpdate = dataRepository.findAll().size();

        // Update the data
        data.setValue(UPDATED_VALUE);
        JSONObject dataDTO = dataMapper.dataToJson(data);

        restDataMockMvc.perform(put("/api/datas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(dataDTO.toString()))
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
