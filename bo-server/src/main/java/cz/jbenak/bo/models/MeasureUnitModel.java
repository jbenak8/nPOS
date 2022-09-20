package cz.jbenak.bo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("measure_units")
public record MeasureUnitModel(@Id String unit, String name, String base_unit, BigDecimal ratio) {
}
