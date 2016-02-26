package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.InvalidManifestException;
import eu.ownyourdata.pia.domain.Manifest;

import java.util.zip.ZipFile;

/**
 * Created by michael on 26.02.16.
 */
public interface PluginRepositoryCustom {
    boolean isInstalled(String identifier);
    Manifest install(ZipFile file) throws ManifestNotFoundException, PluginAlreadyInstalledException, InvalidManifestException, PluginInstallationException;
    void activate(String identifier) throws InvalidManifestException, PluginActivationException;
}
