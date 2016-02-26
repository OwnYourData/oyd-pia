package eu.ownyourdata.pia.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.ownyourdata.pia.domain.InvalidManifestException;
import eu.ownyourdata.pia.domain.Manifest;
import eu.ownyourdata.pia.domain.Plugin;
import eu.ownyourdata.pia.repository.*;
import eu.ownyourdata.pia.web.rest.util.HeaderUtil;
import eu.ownyourdata.pia.web.rest.util.PaginationUtil;
import eu.ownyourdata.pia.web.utils.Files;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Plugin.
 */
@RestController
@RequestMapping("/api")
public class PluginResource {

    private final Logger log = LoggerFactory.getLogger(PluginResource.class);

    private static final int BUFFER_SIZE = 4096;

    @Inject
    private PluginRepository pluginRepository;

    @Inject
    private ClientRegistrationService clientRegistrationService;


    /**
     * GET  /plugins -> get all the plugins.
     */
    @RequestMapping(value = "/plugins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Plugin>> getAllPlugins(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Plugins");
        Page<Plugin> page = pluginRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/plugins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /plugins/:id -> get the "id" plugin.
     */
    @RequestMapping(value = "/plugins/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Plugin> getPlugin(@PathVariable Long id) {
        log.debug("REST request to get Plugin : {}", id);
        Plugin plugin = pluginRepository.findOne(id);
        return Optional.ofNullable(plugin)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /plugins/:id -> delete the "id" plugin.
     */
    @RequestMapping(value = "/plugins/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlugin(@PathVariable Long id) {
        log.debug("REST request to delete Plugin : {}", id);
        pluginRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("plugin", id.toString())).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/plugins/upload")
    public void upload(@RequestPart("name") String name, @RequestPart("file") MultipartFile file) throws IOException, JSONException {
        try {
            Manifest manifest = pluginRepository.install(Files.toZipFile(file));
            pluginRepository.activate(manifest.getIdentifier());
        } catch (ManifestNotFoundException e) {
            e.printStackTrace();
        } catch (PluginAlreadyInstalledException e) {
            e.printStackTrace();
        } catch (InvalidManifestException invalidManifestException) {
            invalidManifestException.printStackTrace();
        } catch (PluginInstallationException e) {
            e.printStackTrace();
        } catch (PluginActivationException e) {
            e.printStackTrace();
        }

    }


}
