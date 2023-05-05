package cz.jbenak.bo.models.data.mappers;

import cz.jbenak.bo.models.data.CountryModel;
import cz.jbenak.npos.api.data.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    @Mapping(source = "isoCode", target = "iso_code")
    @Mapping(source = "commonName", target = "common_name")
    @Mapping(source = "fullName", target = "full_name")
    @Mapping(source = "currencyIsoCode", target = "currency_code")
    CountryModel toModel(Country source);

    @Mapping(source = "iso_code", target = "isoCode")
    @Mapping(source = "common_name", target = "commonName")
    @Mapping(source = "full_name", target = "fullName")
    @Mapping(source = "currency_code", target = "currencyIsoCode")
    Country fromModel(CountryModel model);
}
