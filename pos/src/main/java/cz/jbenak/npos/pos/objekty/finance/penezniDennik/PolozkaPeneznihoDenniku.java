package cz.jbenak.npos.pos.objekty.finance.penezniDennik;

import cz.jbenak.npos.pos.objekty.filialka.Filialka;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class PolozkaPeneznihoDenniku {

    private UUID id;
    private TypOperace operace;
    private Date datum;
    private BigDecimal castka;
    private Filialka filialka;
    private int cisloPokladny;
    private int cisloPokladni;
    private String cisloSmeny;
    private String poznamka;
    private UUID navaznyDoklad;

}
