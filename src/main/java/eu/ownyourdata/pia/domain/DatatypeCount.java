package eu.ownyourdata.pia.domain;

/**
 * Created by michael on 20.03.16.
 */
public class DatatypeCount {

    String type;
    Long count;

    public DatatypeCount(String type, Long count) {
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

        DatatypeCount datatypeCount = (DatatypeCount) o;

        return type.equals(datatypeCount.type);

    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
