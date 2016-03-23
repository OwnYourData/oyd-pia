package eu.ownyourdata.pia.domain;

/**
 * Created by michael on 23.03.16.
 */
public class RepoItemCount {

    private String type;
    private Long count;

    public RepoItemCount(String type, Long count) {
        this.type = type;
        this.count = count;
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

        if (!type.equals(that.type)) return false;
        return count.equals(that.count);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + count.hashCode();
        return result;
    }
}
