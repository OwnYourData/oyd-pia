package eu.ownyourdata.pia.domain.plugin;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("EXTERNAL")
@Table(name="ExternalPlugin")
public class ExternalPlugin extends Plugin {

    @Override
    public void accept(PluginVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}
