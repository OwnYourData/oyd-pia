package eu.ownyourdata.pia.web.rest.mapper;

import eu.ownyourdata.pia.config.ServerDetection;
import eu.ownyourdata.pia.domain.plugin.ExternalPlugin;
import eu.ownyourdata.pia.domain.plugin.HostPlugin;
import eu.ownyourdata.pia.domain.plugin.HostedPlugin;
import eu.ownyourdata.pia.domain.plugin.Plugin;
import eu.ownyourdata.pia.domain.plugin.PluginVisitor;
import eu.ownyourdata.pia.domain.plugin.StandalonePlugin;
import eu.ownyourdata.pia.repository.ProcessRepository;
import eu.ownyourdata.pia.web.rest.dto.PluginDTO;
import org.mapstruct.Mapper;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;

/**
 * Mapper for the entity Plugin and its DTO PluginDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class PluginMapper {

    private String ip;

    @Inject
    private ClientDetailsService clientDetailsService;

    @Inject
    private ProcessRepository processRepository;

    @Inject
    private ServerDetection serverDetection;

    @PostConstruct
    public void init() throws UnknownHostException {
        ip = InetAddress.getLocalHost().getHostAddress();
    }

    public PluginDTO pluginToPluginDTO(Plugin plugin) {
        if (plugin == null) {
            return null;
        }

        PluginDTO target = new PluginDTO();
        target.setId(plugin.getId());
        target.setIdentifier(plugin.getIdentifier());
        target.setName(plugin.getName());
        target.setInfourl(plugin.getInfourl());
        target.setPicture(plugin.getPicture());
        target.setState(processRepository.isRunning(plugin) ? "Running" : "Stopped");

        try {
            plugin.accept(new PluginTypeSetter(target));
            plugin.accept(new PluginRunningSetter(processRepository,target));
            plugin.accept(new PluginUrlSetter(serverDetection,processRepository, clientDetailsService,target));
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

    private static class PluginUrlSetter implements PluginVisitor {
        private ProcessRepository processRepository;

        private ClientDetailsService clientDetailsService;

        private ServerDetection serverDetection;

        private PluginDTO pluginDTO;

        private String ip;

        public PluginUrlSetter(ServerDetection detection, ProcessRepository processRepository,ClientDetailsService clientDetailsService,PluginDTO pluginDTO) {
            this.serverDetection = detection;
            this.processRepository = processRepository;
            this.clientDetailsService = clientDetailsService;

            this.pluginDTO = pluginDTO;
        }


        @Override
        public void visit(HostPlugin hostPlugin) throws Exception {
            visit((StandalonePlugin) hostPlugin);
        }

        @Override
        public void visit(HostedPlugin hostedPlugin) throws Exception {
            int port = processRepository.getProcessPort(hostedPlugin.getHost());
            if (port > 0) {
                if (serverDetection.getHost() != null) {
                    pluginDTO.setUrl(serverDetection.getHost()+"/"+hostedPlugin.getIdentifier());
                }
            }
        }

        @Override
        public void visit(StandalonePlugin standalonePlugin) throws Exception {
            int port = processRepository.getProcessPort(standalonePlugin);
            if (port > 0) {
                pluginDTO.setUrl("http://"+ip+":"+port);
            }
        }

        @Override
        public void visit(ExternalPlugin externalPlugin) throws Exception {
            String url = externalPlugin.getUrl();
            String mobileurl = externalPlugin.getMobileurl();
            if (url.indexOf("?PIA_URL=") == -1){
                if (url.startsWith("https") ||
                    url.startsWith("http://192.168")) {  //also allow for private address spaces in class C networks
                    if (serverDetection.getHost() != null) {
                        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(externalPlugin.getIdentifier());
                        if (!url.endsWith("/")) {
                            url += "/";
                            mobileurl += "/";
                        }
                        url += "?PIA_URL=" + URLEncoder.encode(serverDetection.getHost(),"utf-8");
                        url += "&APP_KEY=" + clientDetails.getClientId();
                        url += "&APP_SECRET=" + clientDetails.getClientSecret();
                        mobileurl += "?PIA_URL=" + URLEncoder.encode(serverDetection.getHost(),"utf-8");
                        mobileurl += "&APP_KEY=" + clientDetails.getClientId();
                        mobileurl += "&APP_SECRET=" + clientDetails.getClientSecret();

                    }

                }
            } else {
                ClientDetails clientDetails = clientDetailsService.loadClientByClientId(externalPlugin.getIdentifier());
                url += "&APP_KEY=" + clientDetails.getClientId();
                url += "&APP_SECRET=" + clientDetails.getClientSecret();
                mobileurl += "&APP_KEY=" + clientDetails.getClientId();
                mobileurl += "&APP_SECRET=" + clientDetails.getClientSecret();
            }
            pluginDTO.setUrl(url);
            pluginDTO.setMobileurl(mobileurl);
        }
    }
}
