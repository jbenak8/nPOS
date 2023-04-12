package cz.jbenak.npos.api.data;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@ToString
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class Country implements Comparable<Country> {

    private String isoCode;
    private String commonName;
    private String fullName;
    private String currencyIsoCode;
    private boolean main;

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
}
