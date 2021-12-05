/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.sortiment;

import cz.jbenak.npos.pos.objekty.finance.DPH;
import cz.jbenak.npos.pos.objekty.partneri.Dodavatel;
import cz.jbenak.npos.pos.objekty.slevy.Sleva;
import cz.jbenak.npos.pos.procesory.ManazerSlev;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-07
 */
public class Sortiment implements Serializable, Comparable<Sortiment> {

    @Serial
    private static final long serialVersionUID = -8299645380846613714L;
    private final BigDecimal sto = new BigDecimal(100);
    private final List<Sleva> slevy;
    private String registr;
    private String nazev;
    private SkupinaSortimentu skupina;
    private String PLU;
    private CarovyKod carovyKod;
    private CarovyKod hlavniCarovyKod;
    private BigDecimal mnozstvi;
    private BigDecimal minProdavaneMnozstvi;
    private BigDecimal maxProdejneMnozstvi;
    private BigDecimal jednotkovaCena;
    private BigDecimal puvodniCena; // původní cena v případě změny ceny
    private DPH dph;
    private MernaJednotka mj;
    private Dodavatel dodavatel;
    private boolean prodejne;
    private boolean slevaPovolena;
    private boolean zmenaCenyPovolena;
    private boolean neskladova;
    private boolean sluzba;
    private boolean nutnoZadatCenu;
    private boolean nutnoZadatMnozstvi;
    private boolean platebniPoukaz;
    private boolean vratkaPovolena;
    private boolean nedelitelne;
    private String popis;
    private byte[] obrazek;
    private boolean nutnoZadatPopis; // u služeb
    private BigDecimal obsahBaleni; //pro měrné ceny
    private MernaJednotka obsahovaMJ;
    private BigDecimal pomerneMnozstvi; //pro měrné ceny
    private MernaJednotka zakladniObsahovaMJ;
    private BigDecimal zakladniObsahoveMnozstvi;
    private boolean vracena;
    private boolean slevaVAkciPovolena;
    private UUID originalniDoklad;
    private int cisloPolozkyVDokladu;

    public Sortiment() {
        slevy = new ArrayList();
    }

    public List<Sleva> getSlevy() {
        return slevy;
    }

    public void setRegistr(String registr) {
        this.registr = registr;
    }

    public String getRegistr() {
        return registr;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public SkupinaSortimentu getSkupina() {
        return skupina;
    }

    public void setSkupina(SkupinaSortimentu skupina) {
        this.skupina = skupina;
    }

    public String getPLU() {
        return PLU;
    }

    public void setPLU(String PLU) {
        this.PLU = PLU;
    }

    public CarovyKod getCarovyKod() {
        return carovyKod;
    }

    public CarovyKod getHlavniCarovyKod() {
        return hlavniCarovyKod;
    }

    public void setHlavniCarovyKod(CarovyKod hlavniCarovyKod) {
        this.hlavniCarovyKod = hlavniCarovyKod;
    }

    public void setCarovyKod(CarovyKod carovyKod) {
        this.carovyKod = carovyKod;
    }

    public BigDecimal getMnozstvi() {
        return mnozstvi;
    }

    public void setMnozstvi(BigDecimal mnozstvi) {
        this.mnozstvi = mnozstvi;
    }

    public BigDecimal getMinProdavaneMnozstvi() {
        return minProdavaneMnozstvi;
    }

    public void setMinProdavaneMnozstvi(BigDecimal minProdavaneMnozstvi) {
        this.minProdavaneMnozstvi = minProdavaneMnozstvi;
    }

    public BigDecimal getMaxProdejneMnozstvi() {
        return maxProdejneMnozstvi;
    }

    public void setMaxProdejneMnozstvi(BigDecimal maxProdejneMnozstvi) {
        this.maxProdejneMnozstvi = maxProdejneMnozstvi;
    }

    public BigDecimal getJednotkovaCena() {
        return jednotkovaCena;
    }

    public void setJednotkovaCena(BigDecimal jednotkovaCena) {
        this.jednotkovaCena = jednotkovaCena;
    }

    public BigDecimal getPuvodniCena() {
        return puvodniCena;
    }

    public void setPuvodniCena(BigDecimal puvodniCena) {
        this.puvodniCena = puvodniCena;
    }

    public DPH getDph() {
        return dph;
    }

    public void setDph(DPH dph) {
        this.dph = dph;
    }

    public MernaJednotka getMj() {
        return mj;
    }

    public void setMj(MernaJednotka mj) {
        this.mj = mj;
    }

    public Dodavatel getDodavatel() {
        return dodavatel;
    }

    public void setDodavatel(Dodavatel dodavatel) {
        this.dodavatel = dodavatel;
    }

    public boolean isProdejne() {
        return prodejne;
    }

    public void setProdejne(boolean prodejne) {
        this.prodejne = prodejne;
    }

    public boolean isSlevaPovolena() {
        return slevaPovolena;
    }

    public void setSlevaPovolena(boolean slevaPovolena) {
        this.slevaPovolena = slevaPovolena;
    }

    public boolean isZmenaCenyPovolena() {
        return zmenaCenyPovolena;
    }

    public void setZmenaCenyPovolena(boolean zmenaCenyPovolena) {
        this.zmenaCenyPovolena = zmenaCenyPovolena;
    }

    public boolean isNeskladova() {
        return neskladova;
    }

    public void setNeskladova(boolean neskladova) {
        this.neskladova = neskladova;
    }

    public boolean isSluzba() {
        return sluzba;
    }

    public void setSluzba(boolean sluzba) {
        this.sluzba = sluzba;
    }

    public boolean isNutnoZadatCenu() {
        return nutnoZadatCenu;
    }

    public void setNutnoZadatCenu(boolean nutnoZadatCenu) {
        this.nutnoZadatCenu = nutnoZadatCenu;
    }

    public boolean isPlatebniPoukaz() {
        return platebniPoukaz;
    }

    public void setPlatebniPoukaz(boolean platebniPoukaz) {
        this.platebniPoukaz = platebniPoukaz;
    }

    public boolean isVratkaPovolena() {
        return vratkaPovolena;
    }

    public void setVratkaPovolena(boolean vratkaPovolena) {
        this.vratkaPovolena = vratkaPovolena;
    }

    public boolean isNedelitelne() {
        return nedelitelne;
    }

    public void setNedelitelne(boolean nedelitelne) {
        this.nedelitelne = nedelitelne;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public byte[] getObrazek() {
        return obrazek;
    }

    public void setObrazek(byte[] obrazek) {
        this.obrazek = obrazek;
    }

    public BigDecimal getObsahBaleni() {
        return obsahBaleni;
    }

    public void setObsahBaleni(BigDecimal obsahBaleni) {
        this.obsahBaleni = obsahBaleni;
    }

    public MernaJednotka getObsahovaMJ() {
        return obsahovaMJ;
    }

    public void setObsahovaMJ(MernaJednotka obsahovaMJ) {
        this.obsahovaMJ = obsahovaMJ;
    }

    public BigDecimal getPomerneMnozstvi() {
        return pomerneMnozstvi;
    }

    public void setPomerneMnozstvi(BigDecimal pomerneMnozstvi) {
        this.pomerneMnozstvi = pomerneMnozstvi;
    }

    public MernaJednotka getZakladniObsahovaMJ() {
        return zakladniObsahovaMJ;
    }

    public void setZakladniObsahovaMJ(MernaJednotka zakladniObsahovaMJ) {
        this.zakladniObsahovaMJ = zakladniObsahovaMJ;
    }

    public BigDecimal getZakladniObsahoveMnozstvi() {
        return zakladniObsahoveMnozstvi;
    }

    public void setZakladniObsahoveMnozstvi(BigDecimal zakladniObsahoveMnozstvi) {
        this.zakladniObsahoveMnozstvi = zakladniObsahoveMnozstvi;
    }

    public boolean isNutnoZadatPopis() {
        return nutnoZadatPopis;
    }

    public void setNutnoZadatPopis(boolean nutnoZadatPopis) {
        this.nutnoZadatPopis = nutnoZadatPopis;
    }

    public boolean isNutnoZadatMnozstvi() {
        return nutnoZadatMnozstvi;
    }

    public void setNutnoZadatMnozstvi(boolean nutnoZadatMnozstvi) {
        this.nutnoZadatMnozstvi = nutnoZadatMnozstvi;
    }

    public boolean isVracena() {
        return vracena;
    }

    public void setVracena(boolean vracena) {
        this.vracena = vracena;
    }

    public UUID getOriginalniDoklad() {
        return originalniDoklad;
    }

    public void setOriginalniDoklad(UUID originalniDoklad) {
        this.originalniDoklad = originalniDoklad;
    }

    public int getCisloPolozkyVDokladu() {
        return cisloPolozkyVDokladu;
    }

    public void setCisloPolozkyVDokladu(int cisloPolozkyVDokladu) {
        this.cisloPolozkyVDokladu = cisloPolozkyVDokladu;
    }

    public BigDecimal getCenaCelkemBezeSlev() {
        return jednotkovaCena.multiply(mnozstvi);
    }

    public BigDecimal getCelkovyZakladDPHBezeSlev() {
        if (dph.getSazba().equals(BigDecimal.ZERO)) {
            return getCenaCelkemBezeSlev();
        } else {
            return (getCenaCelkemBezeSlev().divide(dph.getSazba().add(sto), new MathContext(5))).multiply(sto);
        }
    }

    public BigDecimal getDanBezeSlev() {
        if (dph.getSazba().equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        } else {
            return getCenaCelkemBezeSlev().subtract(getCelkovyZakladDPHBezeSlev());
        }
    }

    public BigDecimal getCenaCelkem() {
        return getCenaCelkemBezeSlev().subtract(ManazerSlev.getHodnotuSlev(this));
    }

    public BigDecimal getCelkovyZakladDPH() {
        if (dph.getSazba().equals(BigDecimal.ZERO)) {
            return getCenaCelkem();
        } else {
            return (getCenaCelkem().divide(dph.getSazba().add(sto), new MathContext(5))).multiply(sto);
        }
    }

    public BigDecimal getDan() {
        if (dph.getSazba().equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        } else {
            return getCenaCelkem().subtract(getCelkovyZakladDPH());
        }
    }

    public BigDecimal getMernaCena() {
        return jednotkovaCena.multiply(pomerneMnozstvi);
    }

    public boolean jePrazdny() {
        return registr == null || registr.isEmpty();
    }

    public boolean isSlevaVAkciPovolena() {
        return slevaVAkciPovolena;
    }

    public void setSlevaVAkciPovolena(boolean slevaVAkciPovolena) {
        this.slevaVAkciPovolena = slevaVAkciPovolena;
    }

    @Override
    public int compareTo(Sortiment o) {
        if (o.getRegistr() == null || o.getRegistr().isEmpty()) {
            return Integer.compare(o.hashCode(), this.hashCode());
        } else {
            return o.getRegistr().compareToIgnoreCase(registr);
        }
    }

    @Override
    public String toString() {
        return registr + " - " + nazev;
    }
}
