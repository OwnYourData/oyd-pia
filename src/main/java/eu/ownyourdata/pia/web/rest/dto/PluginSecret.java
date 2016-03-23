package eu.ownyourdata.pia.web.rest.dto;

/**
 * Created by michael on 23.03.16.
 */
public class PluginSecret {
    private String secret;

    public PluginSecret(String secret) {
        this.secret = secret;
    }

    public PluginSecret() {
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
