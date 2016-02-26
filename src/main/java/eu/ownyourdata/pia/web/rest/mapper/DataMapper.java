package eu.ownyourdata.pia.web.rest.mapper;

import eu.ownyourdata.pia.domain.Data;
import eu.ownyourdata.pia.domain.Datatype;
import eu.ownyourdata.pia.repository.DatatypeRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.mapstruct.Mapper;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Mapper for the entity Data and its DTO DataDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class DataMapper {

    @Inject
    private DatatypeRepository datatypeRepository;

    public JSONObject dataToJson(Data data) {
        try {
            JSONObject result = new JSONObject(data.getValue());
            result.put("id", data.getId());
            result.put("type", data.getType().getName());
            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public Data jsonToData(JSONObject json) {
        Data result = new Data();
        result.setId(json.optLong("id"));
        setDatatype(result, json.optString("type"));

        json.remove("id");
        json.remove("type");

        result.setValue(json.toString());
        return result;
    }

    private void setDatatype(Data result, String type) {
        Optional<Datatype> datatype = datatypeRepository.findOneByName(type);
        if (datatype.isPresent()) {
            result.setType(datatype.get());
        } else {
            result.setType(createDatatype(type));
        }
    }

    private Datatype createDatatype(String type) {
        Datatype newDatatype = new Datatype();
        newDatatype.setName(type);

        return datatypeRepository.save(newDatatype);
    }
}
