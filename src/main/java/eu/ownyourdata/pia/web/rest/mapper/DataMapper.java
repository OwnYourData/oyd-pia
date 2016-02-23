package eu.ownyourdata.pia.web.rest.mapper;

import eu.ownyourdata.pia.domain.*;
import eu.ownyourdata.pia.repository.DatatypeRepository;
import eu.ownyourdata.pia.web.rest.dto.DataDTO;

import org.mapstruct.*;

import javax.inject.Inject;

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
        result.setType(datatypeRepository.findOneByName(dataDTO.getType()).get());

        return result;
    }
}
