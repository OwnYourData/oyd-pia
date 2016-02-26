package eu.ownyourdata.pia.repository;

/**
 * Created by michael on 26.02.16.
 */
public class PluginInstallationException extends Exception {

    public PluginInstallationException() {
    }

    public PluginInstallationException(String message) {
        super(message);
    }

    public PluginInstallationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginInstallationException(Throwable cause) {
        super(cause);
    }

    public PluginInstallationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
