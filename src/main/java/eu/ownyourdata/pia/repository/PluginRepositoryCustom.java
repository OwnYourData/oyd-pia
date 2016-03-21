package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.InvalidManifestException;
import eu.ownyourdata.pia.domain.plugin.Manifest;
import eu.ownyourdata.pia.domain.plugin.Plugin;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * Created by michael on 26.02.16.
 */
public interface PluginRepositoryCustom {

    boolean isInstalled(String identifier);
    Plugin install(ZipFile file) throws ManifestNotFoundException, PluginAlreadyInstalledException, InvalidManifestException, PluginInstallationException;
    Plugin uninstall(Plugin plugin) throws Exception;
    ClientDetails activate(Plugin plugin) throws InvalidManifestException, PluginActivationException;
    Plugin deactivate(Plugin plugin);
    Plugin get(Manifest manifest);
}
