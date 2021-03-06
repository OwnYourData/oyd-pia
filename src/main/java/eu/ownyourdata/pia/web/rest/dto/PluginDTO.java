package eu.ownyourdata.pia.web.rest.dto;

import java.util.Set;

public class PluginDTO {

    private Long id;

    private String name;

    private String identifier;

    private String infourl;

    private String picture;

    private String path;

    private Set<String> permissions;

    private String state;

    private String type;

    private String url;

    private String mobileurl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getInfourl() {
        return infourl;
    }

    public void setInfourl(String infourl) {
        this.infourl = infourl;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getMobileurl() {
        return mobileurl;
    }

    public void setMobileurl(String mobileurl) {
        this.mobileurl = mobileurl;
    }
}
