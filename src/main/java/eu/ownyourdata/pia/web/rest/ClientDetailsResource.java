package eu.ownyourdata.pia.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.ownyourdata.pia.config.JHipsterProperties;
import eu.ownyourdata.pia.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing ClientDetails.
 */
@RestController
@RequestMapping("/api")
public class ClientDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ClientDetailsResource.class);

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private ClientRegistrationService clientRegistrationService;


    /**
     * GET  /datas -> get all the datas.
     */
    @RequestMapping(value = "/clientdetails",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ClientDetails> getAllClientDetails() throws URISyntaxException {
        log.debug("REST request to get a page of ClientDetails");
        String internalClientId = jHipsterProperties.getSecurity().getAuthentication().getOauth().getClientid();
        return clientRegistrationService.listClientDetails();
    }

    /**
     * POST  /datatypes -> Create a new datatype.
     */
    @RequestMapping(value = "/clientdetails",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientDetails> createClientDetail(@Valid @RequestBody BaseClientDetails clientDetails) throws URISyntaxException {
        log.debug("REST request to save ClientDetails : {}", clientDetails);
        clientRegistrationService.addClientDetails(clientDetails);


        return ResponseEntity.created(new URI("/api/clientdetails/" + clientDetails.getClientId()))
            .headers(HeaderUtil.createEntityCreationAlert("clientdetails", clientDetails.getClientId()))
            .body(clientDetails);
    }

    /*
    /**
     * PUT  /datatypes -> Updates an existing datatype.
     */
    /*
    @RequestMapping(value = "/datatypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Datatype> updateDatatype(@Valid @RequestBody Datatype datatype) throws URISyntaxException {
        log.debug("REST request to update Datatype : {}", datatype);
        if (datatype.getId() == null) {
            return createDatatype(datatype);
        }
        Datatype result = datatypeRepository.save(datatype);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("datatype", datatype.getId().toString()))
            .body(result);
    }
    */

}
