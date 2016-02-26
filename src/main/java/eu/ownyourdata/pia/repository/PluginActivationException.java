package eu.ownyourdata.pia.repository;

/**
 * Created by michael on 26.02.16.
 */
public class PluginActivationException extends Exception {
    public PluginActivationException() {
    }

    public PluginActivationException(String message) {
        super(message);
    }

    public PluginActivationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginActivationException(Throwable cause) {
        super(cause);
    }

    public PluginActivationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
