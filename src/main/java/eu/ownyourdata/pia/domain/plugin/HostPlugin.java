package eu.ownyourdata.pia.domain.plugin;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by michael on 20.03.16.
 */
@Entity
@DiscriminatorValue("HOST")
@Table(name="HostPlugin")
public class HostPlugin extends StandalonePlugin {

    @Column(name = "modules_path")
    private String modulesPath;

    public String getModulesPath() {
        return modulesPath;
    }

    public void setModulesPath(String modulesPath) {
        this.modulesPath = modulesPath;
    }

    @Override
    public void accept(PluginVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}
