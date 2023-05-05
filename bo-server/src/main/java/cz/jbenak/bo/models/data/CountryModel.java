package cz.jbenak.bo.models.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("countries")
@Getter
@Setter
public class CountryModel implements Persistable<String> {

    @Id
    private String iso_code;
    private String common_name;
    private String full_name;
    private String currency_code;
    private boolean main;
    @Transient
    private transient boolean saveNew;

    @Override
    public String getId() {
        return iso_code;
    }

    @Override
    @Transient
    public boolean isNew() {
        return saveNew;
    }

    public CountryModel saveAsNew() {
        this.saveNew = true;
        return this;
    }
}
