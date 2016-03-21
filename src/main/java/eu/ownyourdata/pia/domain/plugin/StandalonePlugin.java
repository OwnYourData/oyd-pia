package eu.ownyourdata.pia.domain.plugin;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("STANDALONE")
@Table(name="StandalonePlugin")
public class StandalonePlugin extends Plugin {

    @Column(name = "path")
    private String path;

    @Column(name = "startcommand")
    private String startCommand;

    @Column(name = "installCommand")
    private String installCommand;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public void setStartCommand(String startCommand) {
        this.startCommand = startCommand;
    }

    public String getInstallCommand() {
        return installCommand;
    }

    public void setInstallCommand(String installCommand) {
        this.installCommand = installCommand;
    }

    @Override
    public void accept(PluginVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}
