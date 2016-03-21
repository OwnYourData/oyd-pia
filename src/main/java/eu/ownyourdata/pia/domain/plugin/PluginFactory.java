package eu.ownyourdata.pia.domain.plugin;

import eu.ownyourdata.pia.domain.InvalidManifestException;
import eu.ownyourdata.pia.repository.ManifestNotFoundException;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by michael on 21.03.16.
 */
@Component
public class PluginFactory {

    public Plugin build(ZipFile zip) throws InvalidManifestException, ManifestNotFoundException {
        Manifest manifest = extractManifest(zip);

        if(manifest.getType().equals("host")) {
            HostPlugin hostPugin = createHostPugin(manifest);

            return hostPugin;
        }
        if(manifest.getType().equals("html")) {

        }
        return null;
    }

    public Plugin build(Manifest manifest)  {
        if(manifest.getType().equals("external")) {
            return createExternalPlugin(manifest);
        }
        return null;
    }

    private HostPlugin createHostPugin(Manifest manifest) {
        HostPlugin newPlugin = setGeneralProperties(new HostPlugin(),manifest);

        newPlugin.setStartCommand(manifest.getStartCommand());
        newPlugin.setInstallCommand(manifest.getInstallCommand());
        newPlugin.setModulesPath(manifest.getModules());
        newPlugin.setHostings(manifest.getHostings());

        return newPlugin;
    }

    private ExternalPlugin createExternalPlugin(Manifest manifest) {
        ExternalPlugin newPlugin = setGeneralProperties(new ExternalPlugin(),manifest);

        return newPlugin;
    }

    private <T extends Plugin> T setGeneralProperties(T plugin, Manifest manifest) {
        plugin.setIdentifier(manifest.getIdentifier());
        plugin.setName(manifest.getName());
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
