package cz.jbenak.bo.models.data;

import cz.jbenak.npos.api.shared.enums.DocumentType;
import cz.jbenak.npos.api.shared.enums.FinanceOperationType;
import org.springframework.data.relational.core.mapping.Table;

@Table("finance_operations")
public record FinanceOperationModel(int id,
                                    FinanceOperationType operation_type,
                                    DocumentType document_type,
                                    String operation_name,
                                    String account) {
}
