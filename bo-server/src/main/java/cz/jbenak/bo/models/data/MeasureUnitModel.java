package cz.jbenak.bo.models.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("measure_units")
@Getter
@Setter
public class MeasureUnitModel implements Persistable<String> {

    @Id
    private String unit;
    private String name;
    private String base_unit;
    private BigDecimal ratio;
    @Transient
    private transient boolean saveNew;

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
