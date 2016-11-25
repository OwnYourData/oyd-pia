package eu.ownyourdata.pia.domain;

/**
 * Created by michael on 23.03.16.
 */
public class RepoItemCount {

    private Long id;
    private String description;
    private String type;
    private Long count;

    public RepoItemCount(Long id, String description, String type, Long count) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepoItemCount that = (RepoItemCount) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
