package eu.ownyourdata.pia.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.expression.OAuth2ExpressionUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by michael on 19.10.16.
 */

@Component
public class RepoSecurityService {


    private ScopeWildcardSecurity scopeWildcardSecurity;

    @Inject
    public RepoSecurityService(ScopeWildcardSecurity wildcardScopeSecurity) {
        this.scopeWildcardSecurity = wildcardScopeSecurity;
    }

    public boolean hasAnyPermission(Authentication authentication,String... scopes) {
        return OAuth2ExpressionUtils.hasAnyScope(authentication,scopes) || hasWildcardPermission(authentication,scopes);
    }

    public boolean hasPermission(Authentication authentication, String scope) {
        return hasAnyPermission(authentication,scope);
    }

    private boolean hasWildcardPermission(Authentication authentication, String... scopes) {
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Request clientAuthentication = ((OAuth2Authentication) authentication).getOAuth2Request();

            Collection<String> wildcards = clientAuthentication.getScope().stream().filter(hasWildcard()).collect(Collectors.toList());
            return scopeWildcardSecurity.hasAnyPermission(wildcards,scopes);
        }

        return false;
    }

    private Predicate<String> hasWildcard() {
        return (s) -> s.contains("*");
    }
}
