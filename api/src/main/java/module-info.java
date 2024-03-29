module api {
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires lombok;
    requires jasypt;

    opens cz.jbenak.npos.api.client;
    opens cz.jbenak.npos.api.shared;
    opens cz.jbenak.npos.api.data;

    exports cz.jbenak.npos.api.client;
    exports cz.jbenak.npos.api.shared;
    exports cz.jbenak.npos.api.data;
    exports cz.jbenak.npos.api.shared.enums;
    opens cz.jbenak.npos.api.shared.enums;
}