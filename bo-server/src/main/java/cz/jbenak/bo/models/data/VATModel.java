package cz.jbenak.bo.models.data;

import cz.jbenak.npos.api.shared.enums.VATType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("vat")
public record VATModel(@Id int id, VATType vat_type, BigDecimal percentage, LocalDate valid_from) {
}
