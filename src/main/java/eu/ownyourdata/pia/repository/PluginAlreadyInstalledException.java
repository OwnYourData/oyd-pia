package eu.ownyourdata.pia.repository;

/**
 * Created by michael on 26.02.16.
 */
public class PluginAlreadyInstalledException extends Exception {

    public PluginAlreadyInstalledException() {
    }

    public PluginAlreadyInstalledException(String message) {
        super(message);
    }

    public PluginAlreadyInstalledException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginAlreadyInstalledException(Throwable cause) {
        super(cause);
    }

    public PluginAlreadyInstalledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
