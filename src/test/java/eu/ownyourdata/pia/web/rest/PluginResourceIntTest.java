package eu.ownyourdata.pia.web.rest;

import eu.ownyourdata.pia.Application;
import eu.ownyourdata.pia.domain.plugin.Plugin;
import eu.ownyourdata.pia.domain.plugin.StandalonePlugin;
import eu.ownyourdata.pia.repository.PluginRepository;
import eu.ownyourdata.pia.web.rest.mapper.PluginMapper;
import org.junit.Before;
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
 * Test class for the PluginResource REST controller.
 *
 * @see PluginResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PluginResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_IDENTIFIER = "AAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBB";
    private static final String DEFAULT_PATH = "AAAAA";
    private static final String UPDATED_PATH = "BBBBB";

    @Inject
    private PluginRepository pluginRepository;

    @Inject
    private PluginMapper pluginMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPluginMockMvc;

    private StandalonePlugin plugin;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PluginResource pluginResource = new PluginResource();
        ReflectionTestUtils.setField(pluginResource, "pluginRepository", pluginRepository);
        ReflectionTestUtils.setField(pluginResource,"pluginMapper", pluginMapper);

        this.restPluginMockMvc = MockMvcBuilders.standaloneSetup(pluginResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        plugin = new StandalonePlugin();
        plugin.setName(DEFAULT_NAME);
        plugin.setIdentifier(DEFAULT_IDENTIFIER);
        plugin.setPath(DEFAULT_PATH);
    }


    @Test
    @Transactional
    public void getAllPlugins() throws Exception {
        // Initialize the database
        pluginRepository.saveAndFlush(plugin);

        // Get all the plugins
        restPluginMockMvc.perform(get("/api/plugins"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(plugin.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())))
                .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())));
    }

    @Test
    @Transactional
    public void getPlugin() throws Exception {
        // Initialize the database
        pluginRepository.saveAndFlush(plugin);

        // Get the plugin
        restPluginMockMvc.perform(get("/api/plugins/{id}", plugin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(plugin.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlugin() throws Exception {
        // Get the plugin
        restPluginMockMvc.perform(get("/api/plugins/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }


    @Test
    @Transactional
    public void deletePlugin() throws Exception {
        // Initialize the database
        pluginRepository.saveAndFlush(plugin);

		int databaseSizeBeforeDelete = pluginRepository.findAll().size();

        // Get the plugin
        restPluginMockMvc.perform(delete("/api/plugins/{id}", plugin.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Plugin> plugins = pluginRepository.findAll();
        assertThat(plugins).hasSize(databaseSizeBeforeDelete - 1);
    }
}
