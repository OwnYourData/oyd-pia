package eu.ownyourdata.pia.domain.plugin;

import eu.ownyourdata.pia.domain.InvalidManifestException;
import eu.ownyourdata.pia.repository.HostPluginRepository;
import eu.ownyourdata.pia.repository.ManifestNotFoundException;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by michael on 21.03.16.
 */
@Component
public class PluginFactory {

    @Inject
    private HostPluginRepository hostPluginRepository;

    public Plugin build(ZipFile zip) throws InvalidManifestException, ManifestNotFoundException, RequirementManifestException {
        Manifest manifest = extractManifest(zip);

        return build(manifest);
    }

    public Plugin build(Manifest manifest) throws RequirementManifestException {
        switch (manifest.getType()) {
            case "host": {
                return createHostPugin(manifest);
            }
            case "hosted": {
                return createHostedPlugin(manifest);
            }
            case "external": {
                return createExternalPlugin(manifest);
            }
        }

        return null;
    }

    private HostPlugin createHostPugin(Manifest manifest) {
        HostPlugin newPlugin = setGeneralProperties(new HostPlugin(),manifest);

        newPlugin.setStartCommand(manifest.getStartCommand());
        newPlugin.setInstallCommand(manifest.getInstallCommand());
        newPlugin.setModulesPath(manifest.getModules());

        return newPlugin;
    }

    private HostedPlugin createHostedPlugin(Manifest manifest) throws RequirementManifestException {
        HostedPlugin newPlugin = setGeneralProperties(new HostedPlugin(),manifest);

        for(String requires : manifest.getRequires()) {
            Optional<HostPlugin> hostPlugin = hostPluginRepository.findOneByIdentifier(requires);
            if (hostPlugin.isPresent()) {
                newPlugin.setHost(hostPlugin.get());
                break;
            }
        }

        if (newPlugin.getHost() == null) {
            throw new RequirementManifestException("could not meet requirements: "+manifest.getRequires());
        }

        return newPlugin;
    }

    private ExternalPlugin createExternalPlugin(Manifest manifest) {
        ExternalPlugin newPlugin = setGeneralProperties(new ExternalPlugin(),manifest);
        newPlugin.setUrl(manifest.getUrl());
        newPlugin.setMobileUrl(manifest.getMobileUrl());
        return newPlugin;
    }

    private <T extends Plugin> T setGeneralProperties(T plugin, Manifest manifest) {
        plugin.setIdentifier(manifest.getIdentifier());
        plugin.setName(manifest.getName());
        plugin.setInfourl(manifest.getInfourl());
        plugin.setPicture(manifest.getPicture());
        plugin.setPermissions(manifest.getPermissions());

        return plugin;
    }

    /**
     *
     * @param zip
     * @return a valid Manifest
     * @throws ManifestNotFoundException
     * @throws InvalidManifestException
     */
    public Manifest extractManifest(ZipFile zip) throws ManifestNotFoundException, InvalidManifestException {
        ZipEntry entry = zip.getEntry("manifest.json");
        if (entry == null) {
            throw new ManifestNotFoundException();
        }

        try {
            return new Manifest.ManifestBuilder().withJSON(IOUtils.toString(zip.getInputStream(entry))).build();
        } catch (IOException e) {
            throw new InvalidManifestException(e);
        }
    }
}
