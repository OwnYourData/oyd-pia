package eu.ownyourdata.pia.domain.plugin;

import eu.ownyourdata.pia.domain.InvalidManifestException;
import org.apache.commons.lang3.Validate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by michael on 26.02.16.
 */
public class Manifest {
    private String identifier;

    private String description;

    private String json;

    private String type;

    private String name;

    private String installCommand;

    private String startCommand;

    private String modules;

    private List<String> hostings = new ArrayList<>();

    private List<String> permissions = new ArrayList<>();

    private Manifest() {

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

    public String getInstallCommand() {
        return installCommand;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public String getJson() {
        return json;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<String> getHostings() {
        return hostings;
    }

    public String getModules() {
        return modules;
    }

    public static class ManifestBuilder {
        private final Logger log = LoggerFactory.getLogger(ManifestBuilder.class);

        private Manifest manifest = new Manifest();

        private Exception exception;

        public ManifestBuilder withJSON(String json) {
            try {
                return with(new JSONObject(json));
            } catch (JSONException e) {
                exception = e;
            }
            return this;
        }

        public ManifestBuilder with(JSONObject jsonObject) {
            manifest.identifier = jsonObject.optString("identifier");
            manifest.name = jsonObject.optString("name");
            manifest.type = jsonObject.optString("type");
            manifest.description = jsonObject.optString("description");
            manifest.startCommand = jsonObject.optString("startCommand");
            manifest.installCommand = jsonObject.optString("installCommand");
            manifest.modules = jsonObject.optString("modules");

            withHostings(jsonObject.optJSONArray("hostings"));
            withPermissions(jsonObject.optJSONArray("permissions"));

            return this;
        }

        private ManifestBuilder withHostings(JSONArray hostings) {
            try {
                if (hostings != null) {
                    for (int i = 0; i < hostings.length(); i++) {
                        manifest.hostings.add(hostings.getString(i));
                    }
                }
            } catch (JSONException e) {
                exception = e;
            }

            return this;
        }

        private ManifestBuilder withPermissions(JSONArray permissions) {
            try {
                if (permissions != null) {
                    for (int i = 0; i < permissions.length(); i++) {
                        manifest.permissions.add(permissions.getString(i));
                    }
                }
            } catch (JSONException e) {
                exception = e;
            }
            return this;
        }

        public ManifestBuilder withIdentifier(String identifier) {
            manifest.identifier = identifier;
            return this;
        }

        public ManifestBuilder withName(String name) {
            manifest.name = name;
            return this;
        }

        public ManifestBuilder withDescription(String description) {
            manifest.description = description;
            return this;
        }

        public ManifestBuilder withType(String type) {
            manifest.type = type;
            return this;
        }

        public ManifestBuilder withPermissions(String... permissions) {
            Collections.addAll(manifest.permissions, permissions);
            return this;
        }

        public Manifest build() throws InvalidManifestException {
            try {
                if (exception != null) {
                    throw new InvalidManifestException(exception);
                }
                Validate.notBlank(manifest.identifier, "manifests identifier must not be blank");
                Validate.notBlank(manifest.name, "manifests name must not be blank");
                Validate.notBlank(manifest.type, "manifests type must not be blank");

                if (manifest.type.equals("host")) {
                    Validate.notBlank(manifest.startCommand, "hosts startCommand must not be blank");
                    Validate.notBlank(manifest.modules, "hosts modules must not be blank");
                    Validate.notEmpty(manifest.hostings,"hosts hostings capables must not be empty");
                }

                return manifest;
            } catch (IllegalArgumentException e) {
                throw new InvalidManifestException(e);
            }
        }
    }
}
