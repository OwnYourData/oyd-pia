package eu.ownyourdata.pia.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.ownyourdata.pia.domain.Datatype;
import eu.ownyourdata.pia.repository.DatatypeRepository;
import eu.ownyourdata.pia.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Datatype.
 */
@RestController
@RequestMapping("/api")
public class DatatypeResource {

    private final Logger log = LoggerFactory.getLogger(DatatypeResource.class);

    @Inject
    private DatatypeRepository datatypeRepository;

    /**
     * POST  /datatypes -> Create a new datatype.
     */
    @RequestMapping(value = "/datatypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Datatype> createDatatype(@Valid @RequestBody Datatype datatype) throws URISyntaxException {
        log.debug("REST request to save Datatype : {}", datatype);

        Datatype result = datatypeRepository.save(datatype);
        return ResponseEntity.created(new URI("/api/datatypes/" + result.getName()))
            .headers(HeaderUtil.createEntityCreationAlert("datatype", result.getName()))
            .body(result);
    }

    /**
     * PUT  /datatypes -> Updates an existing datatype.
     */
    @RequestMapping(value = "/datatypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Datatype> updateDatatype(@Valid @RequestBody Datatype datatype) throws URISyntaxException {
        log.debug("REST request to update Datatype : {}", datatype);

        Datatype result = datatypeRepository.save(datatype);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("datatype", datatype.getName()))
            .body(result);
    }

    /**
     * GET  /datatypes -> get all the datatypes.
     */
    @RequestMapping(value = "/datatypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Datatype> getAllDatatypes() {
        log.debug("REST request to get all Datatypes");
        return datatypeRepository.findAll();
            }

    /**
     * GET  /datatypes/:id -> get the "id" datatype.
     */
    @RequestMapping(value = "/datatypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Datatype> getDatatype(@PathVariable Long id) {
        log.debug("REST request to get Datatype : {}", id);
        Datatype datatype = datatypeRepository.findOne(id);
        return Optional.ofNullable(datatype)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /datatypes/:id -> delete the "id" datatype.
     */
    @RequestMapping(value = "/datatypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDatatype(@PathVariable Long id) {
        log.debug("REST request to delete Datatype : {}", id);
        datatypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("datatype", id.toString())).build();
    }
}
