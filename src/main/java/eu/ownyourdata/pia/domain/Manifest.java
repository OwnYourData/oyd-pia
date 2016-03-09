package eu.ownyourdata.pia.domain;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by michael on 26.02.16.
 */
public class Manifest {
    private String identifier;

    private String description;

    private String json;

    private Environment environment;

    private String type;

    private String name;

    private Optional<String> installation;

    private String command;

    private List<String> permissions = new ArrayList<>();

    public Manifest(String json) throws InvalidManifestException {
        this.json = json;

        try {
            JSONObject jsonObject = new JSONObject(json);
            name = jsonObject.getString("name");
            identifier = jsonObject.getString("identifier");
            type = jsonObject.getString("type");
            description = jsonObject.optString("description");

            if (type.startsWith("host")) {
                environment = Environment.valueOf(jsonObject.getString("environment").toUpperCase());
                command = jsonObject.optString("startCommand");
                installation = Optional.ofNullable(jsonObject.optString("postExtractCommand",null));
            }
            if ("html".equalsIgnoreCase(type)) {
                environment = Environment.HOST_HTML;
                command = null;
                installation = Optional.empty();

                if (jsonObject.has("permissions")) {
                    JSONArray permissions = jsonObject.getJSONArray("permissions");
                    for(int i=0;i<permissions.length();i++) {
                        this.permissions.add(permissions.getString(i));
                    }
                }
            }
        } catch (JSONException e) {
            throw new InvalidManifestException(e);
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public String getJson() {
        return json;
    }

    public boolean isValid() {
        return
            StringUtils.isNotBlank(identifier)
            && StringUtils.isNotBlank(command)
            && environment != null;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Optional<String> getPostExtractCommand() {
        return installation;
    }

    public String getStartCommand() {
        return command;
    }

    public String getType() {
        return type;
    }
}
