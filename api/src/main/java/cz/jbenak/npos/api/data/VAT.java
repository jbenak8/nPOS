package cz.jbenak.npos.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class VAT {

    public enum Type {
        BASE, LOWERED_1, LOWERED_2, ZERO
    }

    private int id;
    private Type type;
    private double percentage;
    private String name;
    private LocalDate validFrom;
    private LocalDate validTo;

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
    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("validFrom")
    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    @JsonProperty("validTo")
    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
}
