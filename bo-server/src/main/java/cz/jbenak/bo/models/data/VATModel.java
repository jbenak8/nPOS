package cz.jbenak.bo.models.data;

import cz.jbenak.npos.api.data.VAT;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("vat")
public record VATModel(@Id int id, VAT.Type vat_type, BigDecimal percentage, String label, LocalDate valid_from,
                       LocalDate valid_to) {
}
