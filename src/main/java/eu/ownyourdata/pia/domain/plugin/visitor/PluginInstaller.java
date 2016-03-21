package eu.ownyourdata.pia.domain.plugin.visitor;

import eu.ownyourdata.pia.domain.plugin.*;
import eu.ownyourdata.pia.domain.util.UnzipUtils;
import eu.ownyourdata.pia.repository.PluginInstallationException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipFile;

/**
 * Created by michael on 21.03.16.
 */
public class PluginInstaller implements PluginVisitor {
    public static final String PLUGINS_PATH = "plugins";

    private ZipFile zip;

    public PluginInstaller(ZipFile zip) {
        this.zip = zip;
    }

    @Override
    public void visit(HostPlugin plugin) throws IOException, PluginInstallationException {
        install(plugin);
    }

    @Override
    public void visit(HostedPlugin plugin) {

    }

    @Override
    public void visit(StandalonePlugin plugin) throws IOException, PluginInstallationException {
        install(plugin);
    }

    @Override
    public void visit(ExternalPlugin externalPlugin) throws Exception {

    }

    private void install(StandalonePlugin plugin) throws IOException, PluginInstallationException {
        plugin.setPath(FilenameUtils.concat(PLUGINS_PATH, plugin.getIdentifier()));


        UnzipUtils.extract(zip,plugin.getPath());

        String installCommand = plugin.getInstallCommand();
        if (installCommand != null) try {
            File log = new File(FilenameUtils.concat(plugin.getPath(), "installation.log"));

            ProcessBuilder processBuilder = new ProcessBuilder(installCommand.split(" "));
            processBuilder.directory(new File(plugin.getPath()));
            processBuilder.redirectError(log);
            processBuilder.redirectOutput(log);

            Process process = processBuilder.start();

            process.waitFor(30, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            throw new PluginInstallationException("Could not install plugin " + plugin.getIdentifier(), e);
        }
    }
}
