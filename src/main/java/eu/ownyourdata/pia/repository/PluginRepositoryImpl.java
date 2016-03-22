package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.InvalidManifestException;
import eu.ownyourdata.pia.domain.plugin.Manifest;
import eu.ownyourdata.pia.domain.plugin.Plugin;
import eu.ownyourdata.pia.domain.plugin.PluginFactory;
import eu.ownyourdata.pia.domain.plugin.RequirementManifestException;
import eu.ownyourdata.pia.domain.plugin.visitor.PluginCredentialsWriter;
import eu.ownyourdata.pia.domain.plugin.visitor.PluginInstaller;
import eu.ownyourdata.pia.domain.plugin.visitor.PluginUninstaller;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.zip.ZipFile;

import static org.apache.commons.io.FilenameUtils.concat;

/**
 * Created by michael on 26.02.16.
 */
public class PluginRepositoryImpl implements PluginRepositoryCustom {

    public static final String PLUGINS_PATH = "plugins";

    @Inject
    private ClientRegistrationService clientRegistrationService;

    @Inject
    private PluginRepository pluginRepository;

    @Inject
    private PluginFactory pluginFactory;

    @Override
    public boolean isInstalled(String identifier) {
        return FileUtils.getFile(getPluginInstallPath(identifier)).exists();
    }


    @Override
    public Plugin install(ZipFile zip) throws ManifestNotFoundException, PluginAlreadyInstalledException, InvalidManifestException, PluginInstallationException {
        //verifyPluginNotInstalled(manifest);
        try {
            Plugin plugin = pluginFactory.build(zip);
            plugin.accept(new PluginInstaller(zip));

            return plugin;
        } catch (IOException e) {
            throw new PluginInstallationException("Could not install plugin ", e);
        } catch (Exception e) {
            throw new PluginInstallationException("Could not install plugin ", e);
        }
    }

    @Override
    public Plugin uninstall(Plugin plugin) throws Exception {
        plugin.accept(new PluginUninstaller());

        return plugin;
    }


    private String getPluginInstallPath(String pluginIdentifier) {
        return concat(PLUGINS_PATH, pluginIdentifier);

    }

    private void verifyPluginNotInstalled(Manifest manifest) throws PluginAlreadyInstalledException {
        if (isInstalled(manifest.getIdentifier())) {
            throw new PluginAlreadyInstalledException("Plugin " + manifest.getIdentifier() + " is already installed");
        }
    }

    @Override
    public ClientDetails activate(Plugin plugin) throws InvalidManifestException, PluginActivationException {
        try {
            BaseClientDetails clientDetails = createBaseClientDetails(plugin);
            clientRegistrationService.addClientDetails(clientDetails);

            plugin.accept(new PluginCredentialsWriter(clientDetails));

            return clientDetails;
        } catch (IOException e) {
            throw new PluginActivationException(e);
        } catch (Exception e) {
            throw new PluginActivationException(e);
        }
    }

    @Override
    public Plugin deactivate(Plugin plugin) {
        try {
            clientRegistrationService.removeClientDetails(plugin.getIdentifier());
        } catch (NoSuchClientException exception) {
            // ignore, remove silently
        }
        return plugin;
    }

    @Override
    public Plugin get(Manifest manifest) throws RequirementManifestException {
        return pluginRepository.findOneByIdentifier(manifest.getIdentifier())
            .map(plugin -> plugin)
            .orElse(pluginFactory.build(manifest));
    }

    private BaseClientDetails createBaseClientDetails(Plugin plugin) {
        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setScope(plugin.getPermissions());
        baseClientDetails.setClientId(plugin.getIdentifier());
        baseClientDetails.setClientSecret(RandomStringUtils.randomAlphanumeric(20));
        baseClientDetails.setRefreshTokenValiditySeconds(3600);
        baseClientDetails.setAccessTokenValiditySeconds(3600);
        baseClientDetails.setAuthorizedGrantTypes(Collections.singleton("client_credentials"));

        return baseClientDetails;
    }


}
