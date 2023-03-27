package cz.jbenak.npos.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.jbenak.npos.api.shared.enums.VATType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VAT {

    private int id;
    private VATType type;
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
    public VATType getType() {
        return type;
    }

    public void setType(VATType type) {
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
