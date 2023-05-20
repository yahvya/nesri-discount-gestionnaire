module nesridiscount {
    requires transitive javafx.controls;
    requires transitive json.simple;
    requires tornadofx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires spring.security.crypto;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;
    requires javafx.web;

    opens nesridiscount.models.model to javafx.base;
    opens nesridiscount.controllers to javafx.fxml;

    exports nesridiscount;
    exports nesridiscount.app.util;
}
