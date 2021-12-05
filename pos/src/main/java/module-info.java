module cz.jbenak.npos.pos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires org.postgresql.jdbc;
    requires jasperreports;
    requires org.apache.commons.lang3;
    requires com.google.gson;
    requires openeet.lite;

    exports cz.jbenak.npos.pos.system;
    exports cz.jbenak.npos.pos.system.util;

    opens cz.jbenak.npos.pos.system to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.menu to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.platba.dialogKVraceni to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.platba.platebniOkno to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.platba.platebniOkno.polozka to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.prihlaseni to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.registrace.hlavni to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.registrace.hlavni.kartaSortimentu to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.registrace.hlavni.menu.dokladOtevren to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.registrace.hlavni.menu.registracePredvolby to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.registrace.hlavni.menu.registracePripravena to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.registrace.hlavni.polozkaDokladu to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.registrace.hlavni.seznamSortimentu to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.registrace.hlavni.vyhledaniSortimentu to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.registrace.hlavni.zobrazeniSkupinSortimentu to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.registrace.skupinySlev to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.sdilene.dialogy.progres to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.sdilene.zadaniPopisu to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.sdilene.zadavaciDialog to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.start.stav to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.utility.kalkulacka to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.zakaznik.detail to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.zakaznik.registracniFunkce to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.zakaznik.seznam to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.zakaznik.vyhledani to javafx.fxml;
    opens cz.jbenak.npos.pos.gui.zamek to javafx.fxml;
}