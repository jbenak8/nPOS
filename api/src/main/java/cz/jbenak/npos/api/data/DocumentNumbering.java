package cz.jbenak.npos.api.data;

import cz.jbenak.npos.api.shared.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class DocumentNumbering {

    private int id;
    private String definition;
    private DocumentType documentType;
    private int sequenceNumberLength;
    private LocalDate validFrom;
    private int startFrom;

}
