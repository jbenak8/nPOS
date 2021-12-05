/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.slevy;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Jan Benák
 * @since 2018-02-10
 * @version 1.0.0.0
 */
public class Sleva implements Serializable {
    
    public enum TypSlevy {
        AKCE, PROCENTNI, HODNOTOVA;
    }
    
    public enum TypUplatneni {
            POLOZKA, DOKLAD;
    }
    
    public enum TypPuvodu {
        AKCNI, RUCNI, ZAKAZNICKA, POUKAZ;
    }
    
    @Serial
    private static final long serialVersionUID = 7858952296158486408L;
    
    private final TypSlevy typ;
    private final TypUplatneni uplatneni;
    private final TypPuvodu puvod;
    private final BigDecimal hodnota;
    private final int cisloSkupinySlev;
    private String popis;
    private int cisloAkce;
    private String cisloPoukazu;

    public Sleva(int cisloSkupinySlev, TypSlevy typ, TypUplatneni uplatneni, TypPuvodu puvod, BigDecimal hodnota) {
        this.typ = typ;
        this.uplatneni = uplatneni;
        this.puvod = puvod;
        this.hodnota = hodnota;
        this.cisloSkupinySlev = cisloSkupinySlev;
    }

    public String getPopis() {
        return popis;
    }

    public TypSlevy getTyp() {
        return typ;
    }

    public TypUplatneni getUplatneni() {
        return uplatneni;
    }

    public TypPuvodu getPuvod() {
        return puvod;
    }

    public BigDecimal getHodnota() {
        return hodnota;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public int getCisloSkupinySlev() {
        return cisloSkupinySlev;
    }

    public int getCisloAkce() {
        return cisloAkce;
    }

    public void setCisloAkce(int cisloAkce) {
        this.cisloAkce = cisloAkce;
    }

    public String getCisloPoukazu() {
        return cisloPoukazu;
    }

    public void setCisloPoukazu(String cisloPoukazu) {
        this.cisloPoukazu = cisloPoukazu;
    }
}
