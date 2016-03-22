package eu.ownyourdata.pia.web.rest.mapper;

import eu.ownyourdata.pia.domain.plugin.*;
import eu.ownyourdata.pia.repository.ProcessRepository;
import eu.ownyourdata.pia.web.rest.dto.PluginDTO;
import org.mapstruct.Mapper;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import javax.inject.Inject;

/**
 * Mapper for the entity Plugin and its DTO PluginDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class PluginMapper {

    @Inject
    private ClientDetailsService clientDetailsService;

    @Inject
    private ProcessRepository processRepository;

    public PluginDTO pluginToPluginDTO(Plugin plugin) {
        if (plugin == null) {
            return null;
        }

        PluginDTO target = new PluginDTO();
        target.setId(plugin.getId());
        target.setIdentifier(plugin.getIdentifier());
        target.setName(plugin.getName());
        target.setState(processRepository.isRunning(plugin) ? "Running" : "Stopped");

        try {
            plugin.accept(new PluginRunningSetter(processRepository,target));
            plugin.accept(new PluginTypeSetter(target));
        } catch (Exception e) {
            assert false;
        }

        try {
            target.setPermissions(clientDetailsService.loadClientByClientId(target.getIdentifier()).getScope());
        } catch (NoSuchClientException e) {
            //ignore
        }

        return target;
    }

    private static class PluginTypeSetter implements PluginVisitor {

        private PluginDTO pluginDTO;

        public PluginTypeSetter(PluginDTO pluginDTO) {
            this.pluginDTO = pluginDTO;
        }

        @Override
        public void visit(HostPlugin hostPlugin) {
            pluginDTO.setType("Host");
        }

        @Override
        public void visit(HostedPlugin hostedPlugin) {
            pluginDTO.setType("Hosted");
        }

        @Override
        public void visit(StandalonePlugin standalonePlugin) {
            pluginDTO.setType("Standalone");
        }

        @Override
        public void visit(ExternalPlugin externalPlugin) {
            pluginDTO.setType("External");
        }
    }

    private static class PluginRunningSetter implements PluginVisitor {
        private ProcessRepository processRepository;

        private PluginDTO pluginDTO;

        public PluginRunningSetter(ProcessRepository processRepository, PluginDTO pluginDTO) {
            this.processRepository = processRepository;
            this.pluginDTO = pluginDTO;
        }

        @Override
        public void visit(HostPlugin plugin) throws Exception {
            pluginDTO.setState(processRepository.isRunning(plugin) ? "Running" : "Stopped");
        }

        @Override
        public void visit(HostedPlugin plugin) throws Exception {
            pluginDTO.setState(processRepository.isRunning(plugin.getHost()) ? "Available" : "Unavailable");
        }

        @Override
        public void visit(StandalonePlugin plugin) throws Exception {
            pluginDTO.setState(processRepository.isRunning(plugin) ? "Running" : "Stopped");
        }

        @Override
        public void visit(ExternalPlugin plugin) throws Exception {
            pluginDTO.setState("n/a");
        }
    }
}
