package cz.jbenak.bo.models.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import cz.jbenak.npos.api.shared.enums.DocumentType;

import java.time.LocalDate;

@Table("numbering_series")
public record DocumentNumberingModel(@Id int number,
                                     String definition,
                                     DocumentType document_type,
                                     int sequence_number_length,
                                     LocalDate valid_from,
                                     int start_serial_from,
                                     String label) {
}
