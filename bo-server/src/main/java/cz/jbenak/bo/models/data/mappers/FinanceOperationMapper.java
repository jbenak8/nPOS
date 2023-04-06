package cz.jbenak.bo.models.data.mappers;

import cz.jbenak.bo.models.data.FinanceOperationModel;
import cz.jbenak.npos.api.data.FinanceOperation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FinanceOperationMapper {
    @Mapping(source = "operationType", target = "operation_type")
    @Mapping(source = "documentType", target = "document_type")
    @Mapping(source = "name", target = "operation_name")
    public FinanceOperationModel toModel(FinanceOperation source);

    @Mapping(source = "operation_type", target = "operationType")
    @Mapping(source = "document_type", target = "documentType")
    @Mapping(source = "operation_name", target = "name")
    public FinanceOperation fromModel(FinanceOperationModel model);
}
