package cz.jbenak.npos.api.data;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class MeasureUnit implements Comparable<MeasureUnit> {

    private String unit;
    private String name;
    private String baseUnit;
    private BigDecimal ratio;


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
}
