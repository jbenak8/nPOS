module cz.jbenak.npos.boClient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.base;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.feather;
    requires org.kordamp.ikonli.fluentui;
    //requires eu.hansolo.tilesfx;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.iostreams;
    requires org.apache.logging.log4j.slf4j;
    requires org.apache.logging.log4j;
    requires MaterialFX;
    requires api;
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    uses org.apache.logging.log4j.spi.Provider;

    exports cz.jbenak.npos.boClient;
    exports cz.jbenak.npos.boClient.api;
    exports cz.jbenak.npos.boClient.exceptions;
    exports cz.jbenak.npos.boClient.gui.main;
    exports cz.jbenak.npos.boClient.gui.dialogs.login;
    exports cz.jbenak.npos.boClient.engine;
    exports cz.jbenak.npos.boClient.gui.dialogs.generic;

    opens cz.jbenak.npos.boClient to javafx.fxml;
    opens cz.jbenak.npos.boClient.api to javafx.fxml;
    opens cz.jbenak.npos.boClient.exceptions to javafx.fxml;
    opens cz.jbenak.npos.boClient.engine to javafx.fxml;
    opens cz.jbenak.npos.boClient.gui.main to javafx.fxml;
    opens cz.jbenak.npos.boClient.gui.dialogs.login to javafx.fxml;
    opens cz.jbenak.npos.boClient.gui.dialogs.generic to javafx.fxml;

}