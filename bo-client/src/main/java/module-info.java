module cz.jbenak.npos.boClient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;

    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.feather;
    requires eu.hansolo.tilesfx;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.iostreams;
    requires org.apache.logging.log4j.slf4j;
    requires org.apache.logging.log4j;
    requires MaterialFX;
    requires reactor.core;
    requires rsocket.core;
    requires io.netty.common;

    uses org.apache.logging.log4j.spi.Provider;

    exports cz.jbenak.npos.boClient;
    exports cz.jbenak.npos.boClient.gui.main;
    exports cz.jbenak.npos.boClient.gui.dialogs.login;

    opens cz.jbenak.npos.boClient to javafx.fxml;
    opens cz.jbenak.npos.boClient.gui.main to javafx.fxml;
    opens cz.jbenak.npos.boClient.gui.dialogs.login to javafx.fxml;
}