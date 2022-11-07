package cz.jbenak.npos.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Currency implements Comparable<Currency> {

    private String isoCode;
    private String name;
    private String symbol;
    private boolean acceptable;
    private boolean main;

    @JsonProperty(value = "isoCode", required = true)
    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("acceptable")
    public boolean isAcceptable() {
        return acceptable;
    }

    public void setAcceptable(boolean acceptable) {
        this.acceptable = acceptable;
    }

    @JsonProperty("main")
    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Currency that = (Currency) obj;
        return isoCode.equals(that.isoCode);
    }

    @Override
    public int compareTo(Currency o) {
        return o.isoCode.compareTo(this.isoCode);
    }

    @Override
    public String toString() {
        return "Currency = {" +
                "ISO code: " + isoCode
                + ", name: " + name
                + ", national symbol: " + symbol
                + ", acceptable: " + acceptable
                + ", main currency: " + main
                + "}";
    }
}
