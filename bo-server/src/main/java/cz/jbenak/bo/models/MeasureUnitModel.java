package cz.jbenak.bo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("measure_units")

public class MeasureUnitModel implements Persistable<String> {

    @Id
    private String unit;
    private String name;
    private String base_unit;
    private BigDecimal ratio;
    @Transient
    private transient boolean saveNew;

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBase_unit(String base_unit) {
        this.base_unit = base_unit;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public String getBase_unit() {
        return base_unit;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public MeasureUnitModel asNew() {
        this.saveNew = true;
        return this;
    }

    @Override
    public String getId() {
        return unit;
    }

    @Override
    @Transient
    public boolean isNew() {
        return this.saveNew;
    }
}
/*public record MeasureUnitModel(@Id String unit, String name, String base_unit, BigDecimal ratio)  {
}*/
