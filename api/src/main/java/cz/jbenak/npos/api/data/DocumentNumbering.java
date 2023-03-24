package cz.jbenak.npos.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class DocumentNumbering {

    public enum DocumentType {
        INVOICE, RECEIPT, DELIVERY_NOTE, CREDIT_NOTE, RETURN, DEPOSIT, DISBURSEMENT, WAREHOUSE_RECEIPT, WAREHOUSE_ISSUE
    }

    private int number;
    private String definition;
    private DocumentType documentType;
    private String label;
    private int sequenceNumberLength;
    private LocalDate validFrom;
    private int startFrom;

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

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    @JsonProperty("startSerialFrom")
    public int getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    @Override
    public String toString() {
        return "Document numbering = {number=" + number
                + ", definition=" + definition
                + ", type=" + documentType
                + ", label=" + label
                + ", sequenceNumberLength=" + sequenceNumberLength
                + ", validFrom=" + validFrom
                + ", startSerialFrom=" + startFrom + "}";
    }
}
