package eu.ownyourdata.pia.web.rest.mapper;

import eu.ownyourdata.pia.domain.*;
import eu.ownyourdata.pia.repository.DatatypeRepository;
import eu.ownyourdata.pia.web.rest.dto.DataDTO;

import org.mapstruct.*;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Mapper for the entity Data and its DTO DataDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class DataMapper {

    @Inject
    private DatatypeRepository datatypeRepository;

    @Mapping(source = "type.name", target = "type")
    public abstract DataDTO dataToDataDTO(Data data);

    @Mapping(source = "typeId", target = "type")
    public Data dataDTOToData(DataDTO dataDTO) {
        Data result = new Data();
        result.setId(dataDTO.getId());
        result.setValue(dataDTO.getValue());

        setDatatype(result, dataDTO.getType());

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
