package eu.ownyourdata.pia.domain.plugin.visitor;

import eu.ownyourdata.pia.domain.plugin.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.io.File;
import java.io.IOException;

/**
 * Created by michael on 21.03.16.
 */
public class PluginCredentialsWriter implements PluginVisitor {

    private ClientDetails clientDetails;

    public PluginCredentialsWriter(ClientDetails clientDetails) {
        this.clientDetails = clientDetails;
    }

    @Override
    public void visit(HostPlugin hostPlugin) throws Exception {
        writePluginClientCredentials(hostPlugin);
    }

    @Override
    public void visit(HostedPlugin hostedPlugin) throws Exception {

    }

    @Override
    public void visit(StandalonePlugin standalonePlugin) throws Exception {
        writePluginClientCredentials(standalonePlugin);
    }

    @Override
    public void visit(ExternalPlugin externalPlugin) throws Exception {

    }

    private void writePluginClientCredentials(StandalonePlugin plugin) throws IOException {
        try {
            JSONObject json = new JSONObject();
            json.put("client_id", clientDetails.getClientId());
            json.put("client_secret", clientDetails.getClientSecret());

            File credentials = new File(FilenameUtils.concat(plugin.getPath(), "credentials.json"));

            FileUtils.deleteQuietly(credentials);
            FileUtils.writeStringToFile(credentials, json.toString());
        } catch (JSONException e) {
            assert (false);
        }
    }
}
