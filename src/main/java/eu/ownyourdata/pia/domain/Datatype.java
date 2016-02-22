package eu.ownyourdata.pia.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Datatype.
 */
@Entity
@Table(name = "datatype")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Datatype implements Serializable {

    @Id
    private String name;

    @Column(name = "description")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Datatype datatype = (Datatype) o;
        if(datatype.name == null || name == null) {
            return false;
        }
        return Objects.equals(name, datatype.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Datatype{" +
            "name='" + name + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
