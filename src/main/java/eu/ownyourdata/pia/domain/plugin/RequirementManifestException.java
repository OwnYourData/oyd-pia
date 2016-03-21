package eu.ownyourdata.pia.domain.plugin;

/**
 * Created by michael on 21.03.16.
 */
public class RequirementManifestException extends Exception {

    public RequirementManifestException() {
    }

    public RequirementManifestException(String message) {
        super(message);
    }

    public RequirementManifestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequirementManifestException(Throwable cause) {
        super(cause);
    }

    public RequirementManifestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
