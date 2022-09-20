package cz.jbenak.npos.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class MeasureUnit implements Comparable<MeasureUnit> {

    private String unit;
    private String name;
    private String baseUnit;
    private BigDecimal ratio;

    @JsonProperty(value = "unit", required = true)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("baseUnit")
    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    @JsonProperty("ratio")
    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    @Override
    public int compareTo(MeasureUnit o) {
        return o.unit.compareTo(this.unit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasureUnit that = (MeasureUnit) o;
        return unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit);
    }

    @Override
    public String toString() {
        return "Measure unit = {"
                + "unit: " + unit
                + ", name: " + name
                + ", base unit: " + baseUnit
                + ", ratio to base unit: " + (ratio == null ? "null" : ratio.toPlainString())
                + "}";
    }
}
