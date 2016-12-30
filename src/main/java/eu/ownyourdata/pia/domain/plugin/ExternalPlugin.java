package eu.ownyourdata.pia.domain.plugin;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("EXTERNAL")
@Table(name="ExternalPlugin")
public class ExternalPlugin extends Plugin {

    @Column(name = "url")
    private String url;

    @Column(name = "mobileurl")
    private String mobileurl;

    @Override
    public void accept(PluginVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String link) {
        this.url = link;
    }

    public String getMobileurl() {
        return mobileurl;
    }

    public void setMobileurl(String mobileurl) {
        this.mobileurl = mobileurl;
    }
}
