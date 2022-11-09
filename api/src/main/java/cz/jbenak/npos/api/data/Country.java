package cz.jbenak.npos.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Country implements Comparable<Country> {

    private String isoCode;
    private String commonName;
    private String fullName;
    private String currencyIsoCode;
    private boolean main;

    @JsonProperty(value = "isoCode", required = true)
    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    @JsonProperty("commonName")
    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    @JsonProperty("fullName")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonProperty("currencyIsoCode")
    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public void setCurrencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
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
        Country that = (Country) obj;
        return isoCode.equals(that.isoCode);
    }

    @Override
    public int compareTo(Country o) {
        return o.isoCode.compareTo(this.isoCode);
    }

    @Override
    public String toString() {
        return "Currency = {" +
                "ISO code: " + isoCode
                + ", common name: " + commonName
                + ", full name: " + fullName
                + ", currency ISO code: " + currencyIsoCode
                + ", main country: " + main
                + "}";
    }
}
