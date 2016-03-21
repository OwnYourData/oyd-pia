package eu.ownyourdata.pia.domain.plugin;

import org.apache.commons.io.FilenameUtils;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("HOSTED")
@Table(name="HostedPlugin")
public class HostedPlugin extends Plugin {

    @OneToOne
    private HostPlugin host;



    public String getPath() {
        return FilenameUtils.concat(FilenameUtils.concat(host.getPath(),host.getModulesPath()),getIdentifier());
    }

    @Override
    public void accept(PluginVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}
