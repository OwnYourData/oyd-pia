package eu.ownyourdata.pia.web.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Item entity.
 */
public class ItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String value;

    private Long belongsId;

    private String belongsIdentifier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getBelongsId() {
        return belongsId;
    }

    public void setBelongsId(Long repoId) {
        this.belongsId = repoId;
    }

    public String getBelongsIdentifier() {
        return belongsIdentifier;
    }

    public void setBelongsIdentifier(String repoIdentifier) {
        this.belongsIdentifier = repoIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ItemDTO itemDTO = (ItemDTO) o;

        if ( ! Objects.equals(id, itemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
            "id=" + id +
            ", value='" + value + "'" +
            '}';
    }
}
