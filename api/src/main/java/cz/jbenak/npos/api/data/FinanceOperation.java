package cz.jbenak.npos.api.data;

import cz.jbenak.npos.api.shared.enums.DocumentType;
import cz.jbenak.npos.api.shared.enums.FinanceOperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class FinanceOperation {

    private int id;
    private FinanceOperationType operationType;
    private DocumentType documentType;
    private String name;
    private String account;

}
