package eu.ownyourdata.pia.domain.plugin;

/**
 * Created by michael on 21.03.16.
 */
public interface PluginVisitor {

    void visit(HostPlugin hostPlugin) throws Exception;
    void visit(HostedPlugin hostedPlugin) throws Exception;
    void visit(StandalonePlugin standalonePlugin) throws Exception;
    void visit(ExternalPlugin externalPlugin) throws Exception;
}
