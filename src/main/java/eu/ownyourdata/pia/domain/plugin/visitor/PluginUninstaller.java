package eu.ownyourdata.pia.domain.plugin.visitor;

import eu.ownyourdata.pia.domain.plugin.*;
import eu.ownyourdata.pia.repository.PluginInstallationException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by michael on 21.03.16.
 */
public class PluginUninstaller implements PluginVisitor {
    @Override
    public void visit(HostPlugin plugin) throws Exception {
        uninstall(plugin);
    }

    @Override
    public void visit(HostedPlugin plugin) throws Exception {
        uninstall(plugin);
    }

    @Override
    public void visit(StandalonePlugin plugin) throws Exception {
        uninstall(plugin);
    }

    @Override
    public void visit(ExternalPlugin externalPlugin) throws Exception {

    }

    private void uninstall(HostedPlugin plugin) throws IOException, PluginInstallationException {
        File file = new File(plugin.getPath());
        if (!file.isAbsolute()) {
            FileUtils.deleteDirectory(file);
        }
    }

    private void uninstall(StandalonePlugin plugin) throws IOException, PluginInstallationException {
        File file = new File(plugin.getPath());
        if (!file.isAbsolute()) {
            FileUtils.deleteDirectory(file);
        }
    }
}
