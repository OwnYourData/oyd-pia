package eu.ownyourdata.pia.web.rest.mapper;

import eu.ownyourdata.pia.domain.Item;
import eu.ownyourdata.pia.repository.RepoRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.mapstruct.Mapper;

import javax.inject.Inject;

/**
 * Mapper for the entity Item and its DTO ItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class ItemMapperPia {

    @Inject
    private RepoRepository repoRepository;

    public JSONObject itemToJson(Item item) {
        try {
            JSONObject result = new JSONObject();
            result.put("id", item.getId());
            result.put("value", item.getValue());
            result.put("belongsId", item.getBelongs().getId());
            result.put("belongsIdentifier", item.getBelongs().getIdentifier());

            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public Item jsonToItem(JSONObject json) {
        Item result = new Item();

        result.setId(json.optLong("id"));

        if (json.optLong("belongsId") != 0) {
            result.setBelongs(repoRepository.findOne(json.optLong("belongsId")));
        }

        json.remove("id");
        json.remove("belongsId");
        json.remove("belongsIdentifier");

        result.setValue(json.optString("value"));
        return result;
    }

}
