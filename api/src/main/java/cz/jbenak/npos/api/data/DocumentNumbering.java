package cz.jbenak.npos.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.jbenak.npos.api.shared.enums.DocumentType;

import java.time.LocalDate;

public class DocumentNumbering {

    private int id;
    private String definition;
    private DocumentType documentType;
    private int sequenceNumberLength;
    private LocalDate validFrom;
    private int startFrom;

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @JsonProperty("startSerialFrom")
    public int getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    @Override
    public String toString() {
        return "Document numbering = {id=" + id
                + ", definition=" + definition
                + ", type=" + documentType
                + ", sequenceNumberLength=" + sequenceNumberLength
                + ", validFrom=" + validFrom
                + ", startSerialFrom=" + startFrom + "}";
    }
}
