package eu.ownyourdata.pia.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * A Repo.
 */
@Entity
@Table(name = "repo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Repo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "identifier", nullable = false)
    private String identifier;

    @Column(name = "description")
    private String description;

    @Column(name = "creator")
    private String creator;

    @JsonIgnore
    @OneToMany(mappedBy = "belongs", cascade = CascadeType.REMOVE)
    private Collection<Item> items;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Collection<Item> getItems() {
        return items;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Repo repo = (Repo) o;
        if(repo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, repo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Repo{" +
            "id=" + id +
            ", identifier='" + identifier + "'" +
            ", description='" + description + "'" +
            ", creator='" + creator + "'" +
            '}';
    }
}
