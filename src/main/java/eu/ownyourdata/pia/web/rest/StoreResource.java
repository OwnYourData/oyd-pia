package eu.ownyourdata.pia.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.ownyourdata.pia.domain.sam.Store;
import eu.ownyourdata.pia.domain.sam.StorePlugin;
import eu.ownyourdata.pia.repository.StoreRepository;
import eu.ownyourdata.pia.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Store.
 */
@RestController
@RequestMapping("/api")
public class StoreResource {

    private final Logger log = LoggerFactory.getLogger(StoreResource.class);

    @Inject
    private StoreRepository storeRepository;

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * POST  /stores -> Create a new store.
     */
    @RequestMapping(value = "/stores",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Store> createStore(@Valid @RequestBody Store store) throws URISyntaxException {
        log.debug("REST request to save Store : {}", store);
        if (store.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("store", "idexists", "A new store cannot already have an ID")).body(null);
        }
        Store result = storeRepository.save(store);
        return ResponseEntity.created(new URI("/api/stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("store", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stores -> Updates an existing store.
     */
    @RequestMapping(value = "/stores",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Store> updateStore(@Valid @RequestBody Store store) throws URISyntaxException {
        log.debug("REST request to update Store : {}", store);
        if (store.getId() == null) {
            return createStore(store);
        }
        Store result = storeRepository.save(store);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("store", store.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stores -> get all the stores.
     */
    @RequestMapping(value = "/stores",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Store> getAllStores() {
        log.debug("REST request to get all Stores");
        return storeRepository.findAll();
            }

    /**
     * GET  /stores/:id -> get the "id" store.
     */
    @RequestMapping(value = "/stores/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Store> getStore(@PathVariable Long id) {
        log.debug("REST request to get Store : {}", id);
        Store store = storeRepository.findOne(id);
        return Optional.ofNullable(store)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stores/:id -> delete the "id" store.
     */
    @RequestMapping(value = "/stores/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        log.debug("REST request to delete Store : {}", id);
        storeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("store", id.toString())).build();
    }

    /**
     * GET  /stores/:id/lpugins -> get available plugins in store.
     */
    @RequestMapping(value = "/stores/{id}/plugins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StorePlugin[]> getPlugins(@PathVariable Long id) {
        log.debug("REST request to get plugins from Store : {}", id);
        Store store = storeRepository.findOne(id);
        String url = store.getUrl();
        return restTemplate.getForEntity(url+"/api/plugins",StorePlugin[].class);
    }

}
