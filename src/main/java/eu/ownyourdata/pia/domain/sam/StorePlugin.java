package eu.ownyourdata.pia.domain.sam;


import eu.ownyourdata.pia.domain.plugin.ExternalPlugin;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * A DTO for the Plugin entity.
 */
public class StorePlugin extends ExternalPlugin implements Serializable {


    @NotNull
    private String version;

    @NotNull
    private Integer versionNumber;


    private String description;

    private Integer downloads;

    private Double ratings;

    private Long uploadedById;

    private String uploadedByName;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }
    public Double getRatings() {
        return ratings;
    }

    public void setRatings(Double ratings) {
        this.ratings = ratings;
    }

    public Long getUploadedById() {
        return uploadedById;
    }

    public void setUploadedById(Long userId) {
        this.uploadedById = userId;
    }

    public String getUploadedByName() {
        return uploadedByName;
    }

    public void setUploadedByName(String userLogin) {
        this.uploadedByName = userLogin;
    }

}
