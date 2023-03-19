package cz.jbenak.bo.models.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import cz.jbenak.npos.api.data.DocumentNumbering.DocumentType;

import java.time.LocalDate;

@Table("numbering_series")
public record DocumentNumberingModel(@Id int number, String definition, DocumentType type, int sequenceNumberLength, LocalDate validFrom) {
}
