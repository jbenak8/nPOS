package cz.jbenak.npos.api.data;

import cz.jbenak.npos.api.shared.enums.VATType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class VAT {

    private int id;
    private VATType type;
    private BigDecimal percentage;
    private LocalDate validFrom;

}
