package eu.ownyourdata.pia.web.rest.mapper;

import eu.ownyourdata.pia.domain.Plugin;
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
        target.setPath(plugin.getPath());
        target.setRunning(processRepository.isRunning(plugin));

        try {
            target.setPermissions(clientDetailsService.loadClientByClientId(target.getIdentifier()).getScope());
        } catch (NoSuchClientException e) {
            //ignore
        }

        return target;
    }

    public abstract Plugin pluginDTOtoPlugin(PluginDTO pluginDTO);

}
