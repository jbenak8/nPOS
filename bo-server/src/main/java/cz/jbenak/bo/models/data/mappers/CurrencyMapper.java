package cz.jbenak.bo.models.data.mappers;

import cz.jbenak.bo.models.data.CurrencyModel;
import cz.jbenak.npos.api.data.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    @Mapping(source = "isoCode", target = "iso_code")
    CurrencyModel toModel(Currency source);

    @Mapping(source = "iso_code", target = "isoCode")
    Currency fromModel(CurrencyModel model);
}
