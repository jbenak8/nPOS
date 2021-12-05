package cz.jbenak.npos.pos.objekty.zarizeni;

import cz.jbenak.npos.pos.objekty.platba.DataPlatbyKartou;

import java.math.BigDecimal;

public class PlatebniTerminal {

    public enum TypPripojeni {
        RS232, USB, IP
    }

    private boolean virtualni;
    private boolean inicializovan;
    private String typ;
    private TypPripojeni typPripojeni;
    private String port;
    private String ipAdresa;
    private DataPlatbyKartou dataPlatbyKartou;

    public boolean isVirtualni() {
        return virtualni;
    }

    public void setVirtualni(boolean virtualni) {
        this.virtualni = virtualni;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public TypPripojeni getTypPripojeni() {
        return typPripojeni;
    }

    public void setTypPripojeni(TypPripojeni typPripojeni) {
        this.typPripojeni = typPripojeni;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIpAdresa() {
        return ipAdresa;
    }

    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }

    public DataPlatbyKartou getDataPlatbyKartou() {
        return dataPlatbyKartou;
    }

    public boolean isInicializovan() {
        return inicializovan;
    }

    public void inicializujTerminal() {
        this.inicializovan = true;
    }

    public boolean realizujPlatbu(BigDecimal castka, boolean vratka) {
        return true;
    }
}
