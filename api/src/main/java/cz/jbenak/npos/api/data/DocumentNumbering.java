package cz.jbenak.npos.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class DocumentNumbering {

    public enum DocumentType {
        INVOICE, RECEIPT, DELIVERY_NOTE, CREDIT_NOTE
    }

    private int number;
    private String definition;
    private DocumentType documentType;
    private int sequenceNumberLength;
    private LocalDate validFrom;

    @JsonProperty("number")
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @JsonProperty("definition")
    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @JsonProperty("documentType")
    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType type) {
        this.documentType = type;
    }

    @JsonProperty("sequenceNumberLength")
    public int getSequenceNumberLength() {
        return sequenceNumberLength;
    }

    public void setSequenceNumberLength(int sequenceNumberLength) {
        this.sequenceNumberLength = sequenceNumberLength;
    }

    @JsonProperty("validFrom")
    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    @Override
    public String toString() {
        return "Document numbering = {number=" + number
                + ", definition=" + definition
                + ", type=" + documentType
                + ", sequenceNumberLength=" + sequenceNumberLength
                + ", validFrom=" + validFrom + "}";
    }
}
