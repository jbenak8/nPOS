package cz.jbenak.npos.pos.objekty.slevy;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Akce implements Serializable {

    private static final long serialVersionUID = -2597294520729878854L;

    private int id;
    private LocalDateTime platnostOd;
    private LocalDateTime platnostDo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getPlatnostOd() {
        return platnostOd;
    }

    public void setPlatnostOd(LocalDateTime platnostOd) {
        this.platnostOd = platnostOd;
    }

    public LocalDateTime getPlatnostDo() {
        return platnostDo;
    }

    public void setPlatnostDo(LocalDateTime platnostDo) {
        this.platnostDo = platnostDo;
    }
}
