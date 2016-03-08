package eu.ownyourdata.pia.repository;

import eu.ownyourdata.pia.domain.InvalidManifestException;
import eu.ownyourdata.pia.domain.Manifest;
import eu.ownyourdata.pia.domain.Plugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import javax.inject.Inject;
import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.apache.commons.io.FilenameUtils.concat;

/**
 * Created by michael on 26.02.16.
 */
public class PluginRepositoryImpl implements PluginRepositoryCustom {
    public static final String PLUGINS_PATH = "plugins";


    private static final int BUFFER_SIZE = 4096;

    @Inject
    private ClientRegistrationService clientRegistrationService;

    @Override
    public boolean isInstalled(String identifier) {
        return FileUtils.getFile(getPluginInstallPath(identifier)).exists();
    }



    @Override
    public Plugin install(ZipFile zip) throws ManifestNotFoundException, PluginAlreadyInstalledException, InvalidManifestException, PluginInstallationException {
        Manifest manifest = extractManifest(zip);

        verifyPluginNotInstalled(manifest);

        return install(zip,manifest);
    }

    @Override
    public Plugin uninstall(Plugin plugin) throws IOException {
        File file = new File(plugin.getPath());
        if (!file.isAbsolute()) {
            FileUtils.deleteDirectory(file);
        }

        return plugin;
    }

    private Plugin install(ZipFile zip, Manifest manifest) throws PluginInstallationException {
        String pluginIdentifier = manifest.getIdentifier();
        try {
            String installPath = getPluginInstallPath(pluginIdentifier);
            FileUtils.forceMkdir(new File(installPath));

            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String filePath = concat(installPath, entry.getName());

                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zip.getInputStream(entry), filePath);
                } else {
                    // if the entry is a directory, make the directory
                    FileUtils.forceMkdir(new File(filePath));
                }
            }

            Plugin plugin = new Plugin();

            plugin.setIdentifier(pluginIdentifier);
            plugin.setName(pluginIdentifier);
            plugin.setPath(installPath);
            plugin.setCommand(manifest.getCommand());
            plugin.setEnvironment(manifest.getEnvironment());

            return plugin;

        } catch (IOException exception) {
            throw new PluginInstallationException("Could not install plugin " + pluginIdentifier, exception);
        } finally {
            try {
                zip.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    private String getPluginInstallPath(String pluginIdentifier) {
        return concat(PLUGINS_PATH, pluginIdentifier);
    }


    private void extractFile(InputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    private void verifyPluginNotInstalled(Manifest manifest) throws PluginAlreadyInstalledException {
        if (isInstalled(manifest.getIdentifier())) {
            throw new PluginAlreadyInstalledException("Plugin "+manifest.getIdentifier()+" is already installed");
        }
    }

    public Manifest extractManifest(ZipFile zip) throws ManifestNotFoundException, InvalidManifestException {
        ZipEntry entry = zip.getEntry("manifest.json");
        if (entry == null) {
            throw new ManifestNotFoundException();
        }

        try {
            String jsonManifest = IOUtils.toString(zip.getInputStream(entry));

            return new Manifest(jsonManifest);
        } catch (IOException e) {
            throw new InvalidManifestException(e);
        }
    }

    @Override
    public Plugin activate(Plugin plugin) throws InvalidManifestException, PluginActivationException {
        String pluginInstallPath = plugin.getPath();
        try {
            Manifest manifest = new Manifest(FileUtils.readFileToString(new File(concat(pluginInstallPath,"manifest.json"))));

            BaseClientDetails clientDetails = createBaseClientDetails(manifest);
            clientRegistrationService.addClientDetails(clientDetails);

            writePluginClientCredentials(clientDetails,pluginInstallPath);
            return plugin;
        } catch (IOException e) {
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

    private void writePluginClientCredentials(BaseClientDetails clientDetails, String pluginInstallPath) throws IOException {
        try {
            JSONObject json = new JSONObject();
            json.put("client_id", clientDetails.getClientId());
            json.put("client_secret", clientDetails.getClientSecret());

            File credentials = new File(FilenameUtils.concat(pluginInstallPath, "credentials.json"));

            FileUtils.deleteQuietly(credentials);
            FileUtils.writeStringToFile(credentials,json.toString());
        } catch (JSONException e) {
            assert(false);
        }
    }

    private BaseClientDetails createBaseClientDetails(Manifest manifest) {
        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setScope(manifest.getPermissions());
        baseClientDetails.setClientId(manifest.getIdentifier());
        baseClientDetails.setClientSecret(RandomStringUtils.randomAlphanumeric(20));
        baseClientDetails.setRefreshTokenValiditySeconds(3600);
        baseClientDetails.setAccessTokenValiditySeconds(3600);
        baseClientDetails.setAuthorizedGrantTypes(Collections.singleton("client_credentials"));

        return baseClientDetails;
    }


}
