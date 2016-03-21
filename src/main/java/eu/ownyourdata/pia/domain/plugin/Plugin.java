package eu.ownyourdata.pia.domain.plugin;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A Plugin.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="Plugin_Type")
@Polymorphism(type = PolymorphismType.IMPLICIT)
@Table(name="Plugin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public abstract class Plugin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "identifier", nullable = false, unique = true)
    private String identifier;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="Permissions", joinColumns=@JoinColumn(name="plugin_id"))
    @Column(name="permission")
    public List<String> permissions;

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

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public abstract void accept(PluginVisitor visitor) throws Exception;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Plugin plugin = (Plugin) o;
        if(plugin.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, plugin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Plugin{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", identifier='" + identifier + "'" +
            '}';
    }
}
