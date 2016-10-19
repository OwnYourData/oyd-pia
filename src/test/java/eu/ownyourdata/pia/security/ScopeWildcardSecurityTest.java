package eu.ownyourdata.pia.security;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by michael on 19.10.16.
 */
public class ScopeWildcardSecurityTest {

    private ScopeWildcardSecurity wildcardSecurity;

    @Before
    public void init() {
        wildcardSecurity = new ScopeWildcardSecurity();
    }

    @Test
    public void noWildcardMatch() {
        // given
        Collection<String> wildcards = wildcards("com.something:read","com.something.write");

        // when
        boolean result = wildcardSecurity.hasAnyPermission(wildcards, "com.something:read");

        // then
        assertTrue("should ignore non wildcards and have permission",result);
    }

    @Test
    public void simpleWildcardMatch() {
        // given
        Collection<String> wildcards = wildcards("com.*:read","com.something.write");

        // when
        boolean result = wildcardSecurity.hasAnyPermission(wildcards, "com.something:read");

        // then
        assertTrue("should have permission",result);
    }

    @Test
    public void checkWildcardHierachy() {
        // given
        Collection<String> wildcards = wildcards("com.*:read","com.something.write");

        // when
        boolean result = wildcardSecurity.hasAnyPermission(wildcards, "combank.something:read");

        // then
        assertFalse("should not have permission",result);
    }

    @Test
    public void shouldCheckOperation() {
        // given
        Collection<String> wildcards = wildcards("com.*:read");

        // when
        boolean result = wildcardSecurity.hasAnyPermission(wildcards, "com.something:write");

        // then
        assertFalse("should not have permission",result);
    }

    public Collection<String> wildcards(String... wildcards) {
        return Arrays.asList(wildcards);
    }
}
