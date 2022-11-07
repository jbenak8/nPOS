package cz.jbenak.bo.models.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("currency")
public class CurrencyModel implements Persistable<String> {

    @Id
    private String iso_code;
    private String name;
    private String symbol;
    private boolean acceptable;
    private boolean main;
    @Transient
    private transient boolean saveNew;

    public String getIso_code() {
        return iso_code;
    }

    public void setIso_code(String iso_code) {
        this.iso_code = iso_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean isAcceptable() {
        return acceptable;
    }

    public void setAcceptable(boolean acceptable) {
        this.acceptable = acceptable;
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

    public CurrencyModel saveAsNew() {
        this.saveNew = true;
        return this;
    }
}
