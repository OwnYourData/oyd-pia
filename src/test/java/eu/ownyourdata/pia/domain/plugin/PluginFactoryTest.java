package eu.ownyourdata.pia.domain.plugin;

import eu.ownyourdata.pia.domain.InvalidManifestException;
import eu.ownyourdata.pia.repository.HostPluginRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 21.03.16.
 */
public class PluginFactoryTest {

    @Mock
    private HostPluginRepository hostPluginRepository;

    private HostPlugin existing;

    private PluginFactory pluginFactory;

    @Before
    public void setup() throws InvalidManifestException {
        MockitoAnnotations.initMocks(this);


        pluginFactory = new PluginFactory();
        ReflectionTestUtils.setField(pluginFactory, "hostPluginRepository", hostPluginRepository);

        existing = (HostPlugin) pluginFactory
            .build(new Manifest.ManifestBuilder()
                .withIdentifier("eu.existing.plugin")
                .withName("Existing Plugin")
                .withType("host")
                .withModules("./modules/")
                .withStartCommand("start.sh")
                .build()
            );
    }

    @Test
    public void requiredPluginAvailable() throws InvalidManifestException {
        when(hostPluginRepository.findOneByIdentifier("eu.required.plugin")).thenReturn(Optional.of(existing));

        Manifest newManifest = new Manifest.ManifestBuilder()
            .withIdentifier("eu.new.plugin")
            .withName("New Plugin")
            .withType("hosted")
            .withRequirements("eu.required.plugin")
            .build();

        Plugin newPlugin = pluginFactory.build(newManifest);

        assertThat(newPlugin, instanceOf(HostedPlugin.class));
        assertThat(((HostedPlugin) newPlugin).getHost(),is(existing));
    }
}
