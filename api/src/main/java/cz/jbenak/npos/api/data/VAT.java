package cz.jbenak.npos.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VAT {

    public enum Type {
        BASE, LOWERED_1, LOWERED_2, ZERO
    }

    private int id;
    private Type type;
    private BigDecimal percentage;
    private String label;
    private LocalDate validFrom;

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("type")
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @JsonProperty("percentage")
    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("validFrom")
    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    @Override
    public String toString() {
        return "VAT {" +
                "id=" + id +
                ", type=" + type +
                ", percentage=" + percentage +
                ", label='" + label + '\'' +
                ", validFrom=" + validFrom +
                '}';
    }
}
