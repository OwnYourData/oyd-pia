package eu.ownyourdata.pia.domain.sam;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Plugin entity.
 */
public class StorePlugin implements Serializable {

    private Long id;

    @NotNull
    private String identifier;

    @NotNull
    private String version;

    @NotNull
    private Integer versionNumber;

    private String description;

    private Integer downloads;


    private Double ratings;

    private Long uploadedById;

    private String uploadedByName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StorePlugin pluginDTO = (StorePlugin) o;

        if ( ! Objects.equals(id, pluginDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PluginDTO{" +
            "id=" + id +
            ", identifier='" + identifier + "'" +
            ", version='" + version + "'" +
            ", versionNumber='" + versionNumber + "'" +
            ", description='" + description + "'" +
            ", downloads='" + downloads + "'" +
            ", ratings='" + ratings + "'" +
            '}';
    }
}
