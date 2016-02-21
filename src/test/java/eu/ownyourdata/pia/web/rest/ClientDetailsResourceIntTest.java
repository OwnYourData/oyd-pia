package eu.ownyourdata.pia.web.rest;


import eu.ownyourdata.pia.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the DataResource REST controller.
 *
 * @see DataResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ClientDetailsResourceIntTest {

    @Inject
    private ClientRegistrationService clientRegistrationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDataMockMvc;

    private BaseClientDetails clientDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientDetailsResource clientDetailsResource = new ClientDetailsResource();
        ReflectionTestUtils.setField(clientDetailsResource, "clientRegistrationService", clientRegistrationService);
        this.restDataMockMvc = MockMvcBuilders.standaloneSetup(clientDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
       clientDetails = new BaseClientDetails();
        clientDetails.setClientId("test_plugin");
        clientDetails.setAccessTokenValiditySeconds(360);
        clientDetails.setAuthorizedGrantTypes(Arrays.asList("client_identity"));
        clientDetails.setClientSecret("secret");
        clientDetails.setScope(Arrays.asList("datatype:read"));
    }

    @Test
    @Transactional
    public void createData() throws Exception {
        int databaseSizeBeforeCreate = clientRegistrationService.listClientDetails().size();

        // Create the Data

        restDataMockMvc.perform(post("/api/clientdetails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDetails)))
            .andExpect(status().isCreated());

        // Validate the Data in the database
        List<ClientDetails> datas = clientRegistrationService.listClientDetails();
        assertThat(datas).hasSize(databaseSizeBeforeCreate + 1);
        assertEquals(datas.get(0),clientDetails);

    }
}

