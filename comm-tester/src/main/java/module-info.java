module comm.tester {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.base;
    requires api;
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.iostreams;
    requires org.apache.logging.log4j.slf4j;
    requires jasypt;

    uses org.apache.logging.log4j.spi.Provider;

    exports cz.jbenak.npos.commTester to javafx.graphics;
    exports cz.jbenak.npos.commTester.gui.main to javafx.fxml;
}