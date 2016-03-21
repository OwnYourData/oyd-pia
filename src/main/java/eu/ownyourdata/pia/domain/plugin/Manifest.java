package eu.ownyourdata.pia.domain.plugin;

import eu.ownyourdata.pia.domain.InvalidManifestException;
import org.apache.commons.lang3.Validate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by michael on 26.02.16.
 */
public class Manifest {
    private String identifier;

    private String description;

    private String type;

    private String name;

    private String installCommand;

    private String startCommand;

    private String modules;

    private List<String> permissions = new ArrayList<>();

    private List<String> requires = new ArrayList<>();

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

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getModules() {
        return modules;
    }

    public List<String> getRequires() {
        return requires;
    }

    public static class ManifestBuilder {

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

            withPermissions(jsonObject.optJSONArray("permissions"));
            withRequirements(jsonObject.optJSONArray("requires"));

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

        private ManifestBuilder withRequirements(JSONArray requirements) {
            try {
                if (requirements != null) {
                    for (int i = 0; i < requirements.length(); i++) {
                        manifest.requires.add(requirements.getString(i));
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

        public ManifestBuilder withStartCommand(String startCommand) {
            manifest.startCommand = startCommand;
            return this;
        }

        public ManifestBuilder withModules(String modules) {
            manifest.modules = modules;
            return this;
        }

        public ManifestBuilder withRequirements(String... requirements) {
            Collections.addAll(manifest.requires, requirements);
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
                }

                if (manifest.type.equals("hosted")) {
                    Validate.notEmpty(manifest.requires);
                }

                return manifest;
            } catch (IllegalArgumentException e) {
                throw new InvalidManifestException(e);
            }
        }
    }
}
