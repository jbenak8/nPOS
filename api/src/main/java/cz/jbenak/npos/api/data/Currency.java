package cz.jbenak.npos.api.data;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class Currency implements Comparable<Currency> {

    private String isoCode;
    private String name;
    private String symbol;
    private boolean acceptable;
    private boolean main;

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

}
