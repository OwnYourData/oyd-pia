package eu.ownyourdata.pia.domain.plugin;

import javax.persistence.*;
import java.util.List;

/**
 * Created by michael on 20.03.16.
 */
@Entity
@DiscriminatorValue("HOST")
@Table(name="HostPlugin")
public class HostPlugin extends StandalonePlugin {

    @Column(name = "modules_path")
    private String modulesPath;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="Hostings", joinColumns=@JoinColumn(name="plugin_id"))
    @Column(name="hostings")
    public List<String> hostings;


    public String getModulesPath() {
        return modulesPath;
    }

    public void setModulesPath(String modulesPath) {
        this.modulesPath = modulesPath;
    }

    public List<String> getHostings() {
        return hostings;
    }

    public void setHostings(List<String> hostings) {
        this.hostings = hostings;
    }

    @Override
    public void accept(PluginVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}
