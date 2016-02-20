package eu.ownyourdata.pia.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
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
        return clientRegistrationService.listClientDetails();
    }
}
