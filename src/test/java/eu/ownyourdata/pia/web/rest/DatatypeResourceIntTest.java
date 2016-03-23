package eu.ownyourdata.pia.web.rest;

import eu.ownyourdata.pia.Application;
import eu.ownyourdata.pia.domain.Datatype;
import eu.ownyourdata.pia.repository.DatatypeRepository;
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
 * Test class for the DatatypeResource REST controller.
 *
 * @see DatatypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Ignore
public class DatatypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private DatatypeRepository datatypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDatatypeMockMvc;

    private Datatype datatype;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatatypeResource datatypeResource = new DatatypeResource();
        ReflectionTestUtils.setField(datatypeResource, "datatypeRepository", datatypeRepository);
        this.restDatatypeMockMvc = MockMvcBuilders.standaloneSetup(datatypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        datatype = new Datatype();
        datatype.setName(DEFAULT_NAME);
        datatype.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDatatype() throws Exception {
        int databaseSizeBeforeCreate = datatypeRepository.findAll().size();

        // Create the Datatype

        restDatatypeMockMvc.perform(post("/api/datatypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(datatype)))
                .andExpect(status().isCreated());

        // Validate the Datatype in the database
        List<Datatype> datatypes = datatypeRepository.findAll();
        assertThat(datatypes).hasSize(databaseSizeBeforeCreate + 1);
        Datatype testDatatype = datatypes.get(datatypes.size() - 1);
        assertThat(testDatatype.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDatatype.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = datatypeRepository.findAll().size();
        // set the field null
        datatype.setName(null);

        // Create the Datatype, which fails.

        restDatatypeMockMvc.perform(post("/api/datatypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(datatype)))
                .andExpect(status().isBadRequest());

        List<Datatype> datatypes = datatypeRepository.findAll();
        assertThat(datatypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDatatypes() throws Exception {
        // Initialize the database
        datatypeRepository.saveAndFlush(datatype);

        // Get all the datatypes
        restDatatypeMockMvc.perform(get("/api/datatypes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(datatype.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getDatatype() throws Exception {
        // Initialize the database
        datatypeRepository.saveAndFlush(datatype);

        // Get the datatype
        restDatatypeMockMvc.perform(get("/api/datatypes/{id}", datatype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(datatype.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDatatype() throws Exception {
        // Get the datatype
        restDatatypeMockMvc.perform(get("/api/datatypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDatatype() throws Exception {
        // Initialize the database
        datatypeRepository.saveAndFlush(datatype);

		int databaseSizeBeforeUpdate = datatypeRepository.findAll().size();

        // Update the datatype
        datatype.setName(UPDATED_NAME);
        datatype.setDescription(UPDATED_DESCRIPTION);

        restDatatypeMockMvc.perform(put("/api/datatypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(datatype)))
                .andExpect(status().isOk());

        // Validate the Datatype in the database
        List<Datatype> datatypes = datatypeRepository.findAll();
        assertThat(datatypes).hasSize(databaseSizeBeforeUpdate);
        Datatype testDatatype = datatypes.get(datatypes.size() - 1);
        assertThat(testDatatype.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDatatype.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteDatatype() throws Exception {
        // Initialize the database
        datatypeRepository.saveAndFlush(datatype);

		int databaseSizeBeforeDelete = datatypeRepository.findAll().size();

        // Get the datatype
        restDatatypeMockMvc.perform(delete("/api/datatypes/{id}", datatype.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Datatype> datatypes = datatypeRepository.findAll();
        assertThat(datatypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
