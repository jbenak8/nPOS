package cz.jbenak.npos.pos.objekty.meny;

import javafx.scene.image.Image;

public class ObrazekNaTlacitku {

    private int cislo;
    private int cisloDenominace;
    private Image obrazek;

    public int getCislo() {
        return cislo;
    }

    public void setCislo(int cislo) {
        this.cislo = cislo;
    }

    public int getCisloDenominace() {
        return cisloDenominace;
    }

    public void setCisloDenominace(int cisloDenominace) {
        this.cisloDenominace = cisloDenominace;
    }

    public Image getObrazek() {
        return obrazek;
    }

    public void setObrazek(Image obrazek) {
        this.obrazek = obrazek;
    }
}
