module nesridiscount {
    requires transitive json.simple;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires spring.security.crypto;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;

    opens nesridiscount.models.model to javafx.base;
    opens nesridiscount.controllers to javafx.fxml;

    exports nesridiscount;
    exports nesridiscount.app.util;
}
