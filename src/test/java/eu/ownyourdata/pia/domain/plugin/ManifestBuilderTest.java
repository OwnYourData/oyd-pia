package eu.ownyourdata.pia.domain.plugin;

import eu.ownyourdata.pia.domain.InvalidManifestException;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by michael on 21.03.16.
 */
public class ManifestBuilderTest {


    @Before
    public void init() {

    }

    @Test(expected = InvalidManifestException.class)
    public void hostedPluginWithoutRequirementsIsInvalid() throws InvalidManifestException {
        new Manifest.ManifestBuilder()
            .withIdentifier("eu.ownyourdata.test.plugin")
            .withName("Some Human Readable Name")
            .withType("hosted").build();
    }
}
