module api {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    opens cz.jbenak.npos.api.client;
    opens cz.jbenak.npos.api.shared;

    exports cz.jbenak.npos.api.client;
    exports cz.jbenak.npos.api.shared;
}