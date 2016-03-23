package eu.ownyourdata.pia.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.ownyourdata.pia.domain.*;
import eu.ownyourdata.pia.domain.plugin.Manifest;
import eu.ownyourdata.pia.domain.plugin.Plugin;
import eu.ownyourdata.pia.domain.plugin.RequirementManifestException;
import eu.ownyourdata.pia.domain.plugin.StandalonePlugin;
import eu.ownyourdata.pia.repository.*;
import eu.ownyourdata.pia.web.rest.dto.PluginDTO;
import eu.ownyourdata.pia.web.rest.dto.PluginSecret;
import eu.ownyourdata.pia.web.rest.mapper.PluginMapper;
import eu.ownyourdata.pia.web.rest.util.HeaderUtil;
import eu.ownyourdata.pia.web.rest.util.PaginationUtil;
import eu.ownyourdata.pia.web.utils.Files;
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
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Plugin.
 */
@RestController
@RequestMapping("/api")
public class PluginResource {

    private final Logger log = LoggerFactory.getLogger(PluginResource.class);

    @Inject
    private PluginMapper pluginMapper;

    @Inject
    private PluginRepository pluginRepository;

    @Inject
    private ProcessRepository processRepository;

    @Inject
    private ClientDetailsService clientDetailsService;

    /**
     * GET  /plugins -> get all the plugins.
     */
    @RequestMapping(value = "/plugins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PluginDTO>> getAllPlugins(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Plugins");
        Page<Plugin> page = pluginRepository.findAll(pageable);

        List<PluginDTO> pluginDTOs = page.getContent().stream()
            .map(plugin -> pluginMapper.pluginToPluginDTO(plugin))
            .collect(Collectors.toList());


        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/plugins");
        return new ResponseEntity<>(pluginDTOs, headers, HttpStatus.OK);
    }

    /**
     * GET  /plugins/:id -> get the "id" plugin.
     */
    @RequestMapping(value = "/plugins/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PluginDTO> getPlugin(@PathVariable Long id) {
        log.debug("REST request to get Plugin : {}", id);
        Plugin plugin = pluginRepository.findOne(id);
        return Optional.ofNullable(plugin)
            .map(result -> new ResponseEntity<>(
                pluginMapper.pluginToPluginDTO(result),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /plugins/:id/start -> start the "id" plugin.
     */
    @RequestMapping(value = "/plugins/{id}/start",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> startPlugin(@PathVariable Long id) {
        log.debug("REST request to start Plugin : {}", id);
        Plugin plugin = pluginRepository.findOne(id);
        try {
            processRepository.create(((StandalonePlugin) plugin));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET  /plugins/:id/stop -> stop the "id" plugin.
     */
    @RequestMapping(value = "/plugins/{id}/stop",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> stopPlugin(@PathVariable Long id) {
        log.debug("REST request to start Plugin : {}", id);
        Plugin plugin = pluginRepository.findOne(id);
        if (processRepository.stop(((StandalonePlugin) plugin))) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        Plugin plugin = pluginRepository.findOne(id);

        if (plugin != null) {
            try {
                pluginRepository.deactivate(plugin);
                pluginRepository.uninstall(plugin);
                pluginRepository.delete(id);
                return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("plugin", id.toString())).build();
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/plugins/upload")
    public void upload(@RequestPart("name") String name, @RequestPart("file") MultipartFile file) throws IOException, JSONException {
        try {
            Plugin plugin = pluginRepository.install(Files.toZipFile(file));
            pluginRepository.activate(plugin);
            pluginRepository.save(plugin);
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

    /**
     * GET  /plugins/:id -> get the "id" plugin.
     */
    @RequestMapping(value = "/plugins/{id}/secret",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PluginSecret> getPluginSecret(@PathVariable Long id) {
        log.debug("REST request to get Plugin : {}", id);
        Plugin plugin = pluginRepository.findOne(id);
        if (plugin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            try {
                ClientDetails clientDetails = clientDetailsService.loadClientByClientId(plugin.getIdentifier());

                return new ResponseEntity<>(new PluginSecret(clientDetails.getClientSecret()),HttpStatus.OK);
            } catch (ClientRegistrationException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/plugins/register")
    public ResponseEntity<JSONObject> register(@Valid @RequestBody String base64) throws IOException, JSONException {
        byte[] decode = Base64.getDecoder().decode(base64);
        try {
            Manifest manifest = new Manifest.ManifestBuilder().withJSON(new String(decode,"UTF-8")).build();

            Plugin plugin = pluginRepository.get(manifest);
            ClientDetails clientDetails = pluginRepository.activate(plugin);
            pluginRepository.save(plugin);

            JSONObject result = new JSONObject();
            result.put("client_id",clientDetails.getClientId());
            result.put("client_secret",clientDetails.getClientSecret());

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (InvalidManifestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (PluginActivationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RequirementManifestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
