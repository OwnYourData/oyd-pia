package eu.ownyourdata.pia.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.ownyourdata.pia.domain.Item;
import eu.ownyourdata.pia.domain.Repo;
import eu.ownyourdata.pia.domain.RepoItemCount;
import eu.ownyourdata.pia.service.ItemService;
import eu.ownyourdata.pia.service.RepoService;
import eu.ownyourdata.pia.web.rest.mapper.ItemMapper;
import eu.ownyourdata.pia.web.rest.util.HeaderUtil;
import eu.ownyourdata.pia.web.rest.util.PaginationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Repo.
 */
@Api(value = "Repository", description = "Endpoint for repositories containing the items.")
@RestController
@RequestMapping("/api")
public class RepoResource {

    private final Logger log = LoggerFactory.getLogger(RepoResource.class);

    @Inject
    private RepoService repoService;

    @Inject
    private ItemService itemService;

    @Inject
    private ItemMapper itemMapper;

    /**
     * POST  /repos -> Create a new repo.
     */
    @RequestMapping(value = "/repos", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Repo> createRepo(@Valid @RequestBody Repo repo) throws URISyntaxException {
        log.debug("REST request to save Repo : {}", repo);
        if (repo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("repo", "idexists", "A new repo cannot already have an ID")).body(null);
        }
        Repo result = repoService.save(repo);
        return ResponseEntity.created(new URI("/api/repos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("repo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /repos -> Updates an existing repo.
     */
    @RequestMapping(value = "/repos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Repo> updateRepo(@Valid @RequestBody Repo repo) throws URISyntaxException {
        log.debug("REST request to update Repo : {}", repo);
        if (repo.getId() == null) {
            return createRepo(repo);
        }
        Repo result = repoService.save(repo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("repo", repo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /repos -> get all the repos.
     */
    @RequestMapping(value = "/repos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Repo>> getAllRepos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Repos");
        Page<Repo> page = repoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/repos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /repos/:id -> get the "id" repo.
     */
    @RequestMapping(value = "/repos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Repo> getRepo(@PathVariable Long id) {
        log.debug("REST request to get Repo : {}", id);
        Repo repo = repoService.findOne(id);
        return Optional.ofNullable(repo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /repos/:id -> delete the "id" repo.
     */
    @RequestMapping(value = "/repos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRepo(@PathVariable Long id) {
        log.debug("REST request to delete Repo : {}", id);
        repoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("repo", id.toString())).build();
    }

    /**
     * GET  /repos/counts -> get the "id" datatype.
     */
    @RequestMapping(value = "/repos/counts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Collection<RepoItemCount>> getRepoItemsCounts() {
        log.debug("REST request to get RepoItemCount");

        return ResponseEntity.ok(repoService.getRepoItemCounts());
    }

    /**
     * ================================================================================================================
     * PIA Cross-Domain REST Interface
     * ================================================================================================================
     **/

    /**
     * GET  /datas -> get all the datas.
     */
    @ApiOperation(tags = "Plugins", value="Retrieve all items from a given repository.")
    @RequestMapping(value = "/repos/{identifier:.+}/items", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_ADMIN') OR #oauth2.hasScope(#identifier+':read')")
    public ResponseEntity<List<JSONObject>> getAllItems(@PathVariable String identifier, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Datas");
        Optional<Repo> repo = repoService.findOneByIdentifier(identifier);
        if (repo.isPresent()) {
            Page<Item> page = itemService.findAllByBelongs(repo.get(),pageable);

            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/"+identifier+"/items");
            return new ResponseEntity<>(page.getContent().stream()
                .map(e -> itemMapper.itemToJson(e))
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<List<JSONObject>>(HttpStatus.OK);
        }
    }

    /**
     * POST  /datas -> Create a new data.
     */
    @ApiOperation(tags = "Plugins", value="Saves an item to a repository.")
    @RequestMapping(value = "/repos/{identifier:.+}/items", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_ADMIN') OR #oauth2.hasScope(#identifier+':write')")
    public ResponseEntity<JSONObject> createItem(@PathVariable String identifier, @Valid @RequestBody JSONObject json) throws URISyntaxException, JSONException {
        Repo defaultValue = new Repo();
        defaultValue.setIdentifier(identifier);
        defaultValue.setDescription("n/a");

        Repo repo = repoService.getByIdentifier(identifier, defaultValue);

        Item item = itemMapper.jsonToItem(json);
        item.setBelongs(repo);

        Item result = itemService.save(item);

        return ResponseEntity.ok(itemMapper.itemToJson(result));
    }

    /**
     * PUT  /datas -> Updates an existing data.
     */
    @ApiOperation(tags = "Plugins", value="Updates an item in a repository.")
    @RequestMapping(value = "/repos/{identifier:.+}/items", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_ADMIN') OR #oauth2.hasScope(#identifier+':update')")
    public ResponseEntity<JSONObject> updateItem(@PathVariable String identifier, @Valid @RequestBody JSONObject json) throws URISyntaxException, JSONException {
        log.debug("REST request to update Item : {}", json);
        if (json.optString("id").equals("")) {
            return createItem(identifier,json);
        }

        if (itemService.belongs(json.optLong("id"),identifier)) {
            Item item = itemMapper.jsonToItem(json);
            item = itemService.save(item);
            JSONObject result = itemMapper.itemToJson(item);

            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("item", result.optString("id"))).body(result);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * DELETE  /datas/:id -> delete the "id" data.
     */
    @ApiOperation(tags = "Plugins", value="Deletes an item from a repository.")
    @RequestMapping(value = "/repos/{identifier:.+}/items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_ADMIN') OR #oauth2.hasScope(#identifier+':delete')")
    public ResponseEntity<Void> deleteData(@PathVariable String identifier, @PathVariable Long id) {
        log.debug("REST request to delete Data : {}", id);

        if (itemService.belongs(id,identifier)) {
            itemService.delete(id);

            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("data", id.toString())).build();
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
