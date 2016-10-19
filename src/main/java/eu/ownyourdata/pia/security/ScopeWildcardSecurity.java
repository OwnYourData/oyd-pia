package eu.ownyourdata.pia.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by michael on 19.10.16.
 */
@Component
public class ScopeWildcardSecurity {
    public boolean hasAnyPermission(Collection<String> wildcards, String... scopes) {
        for(String scope : scopes) {
            if(hasPermission(wildcards,scope)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPermission(Collection<String> wildcards, String scope) {
        for(String wildcard : wildcards) {
            String parentScope = StringUtils.substringBefore(wildcard, "*");
            String parentOperation = StringUtils.substringAfter(wildcard,":");

            if(scope.startsWith(parentScope) && scope.endsWith(parentOperation)) {
                return true;
            }
        }
        return false;
    }
}
