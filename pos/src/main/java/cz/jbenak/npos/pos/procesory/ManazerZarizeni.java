package cz.jbenak.npos.pos.procesory;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.objekty.zarizeni.*;
import cz.jbenak.npos.pos.system.Pos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ManazerZarizeni {

    private static final Logger LOGER = LogManager.getLogger(ManazerZarizeni.class);
    private static volatile ManazerZarizeni instance;
    private final ProgresDialog progres;
    private Tiskarna tiskarna;
    private PlatebniTerminal terminal;
    private Skener skener;
    private ZakaznickyDisplej displej;
    private PokladniZasuvka zasuvka;

    private ManazerZarizeni() {
        progres = new ProgresDialog(Pos.getInstance().getAplikacniOkno());
    }

    public static ManazerZarizeni getInstance() {
        ManazerZarizeni manazer = ManazerZarizeni.instance;
        if (manazer == null) {
            synchronized (ManazerZarizeni.class) {
                manazer = ManazerZarizeni.instance;
                if (manazer == null) {
                    ManazerZarizeni.instance = manazer = new ManazerZarizeni();
                }
            }
        }
        return manazer;
    }

    public Tiskarna getTiskarna() {
        return tiskarna;
    }

    public void setTiskarna(Tiskarna tiskarna) {
        this.tiskarna = tiskarna;
    }

    public PlatebniTerminal getTerminal() {
        return terminal;
    }

    public void setTerminal(PlatebniTerminal terminal) {
        this.terminal = terminal;
    }

    public Skener getSkener() {
        return skener;
    }

    public void setSkener(Skener skener) {
        this.skener = skener;
    }

    public ZakaznickyDisplej getDisplej() {
        return displej;
    }

    public void setDisplej(ZakaznickyDisplej displej) {
        this.displej = displej;
    }

    public PokladniZasuvka getZasuvka() {
        return zasuvka;
    }

    public void setZasuvka(PokladniZasuvka zasuvka) {
        this.zasuvka = zasuvka;
    }

    public void inicializujZarizeni() {

        terminal = new PlatebniTerminal();
        terminal.setVirtualni(true);

        terminal.inicializujTerminal();
    }
}
