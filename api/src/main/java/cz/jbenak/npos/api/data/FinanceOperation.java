package cz.jbenak.npos.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.jbenak.npos.api.shared.enums.DocumentType;
import cz.jbenak.npos.api.shared.enums.FinanceOperationType;

public class FinanceOperation {

    private int id;
    private FinanceOperationType operationType;
    private DocumentType documentType;
    private String name;
    private String account;

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("operationType")
    public FinanceOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(FinanceOperationType operationType) {
        this.operationType = operationType;
    }

    @JsonProperty("documentType")
    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "FinanceOperation {" +
                "id=" + id +
                ", operationType=" + operationType +
                ", documentType=" + documentType +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
