package cz.jbenak.bo.models.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("countries")
public class CountryModel implements Persistable<String> {

    @Id
    private String iso_code;
    private String common_name;
    private String full_name;
    private String currency_code;
    private boolean main;
    @Transient
    private transient boolean saveNew;

    public String getIso_code() {
        return iso_code;
    }

    public void setIso_code(String iso_code) {
        this.iso_code = iso_code;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

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
