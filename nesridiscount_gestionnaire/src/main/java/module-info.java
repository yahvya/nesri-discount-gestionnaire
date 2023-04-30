module nesridiscount {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires spring.security.crypto;
    requires json.simple;
    requires de.jensd.fx.glyphs.fontawesome;

    opens nesridiscount.controllers to javafx.fxml;
    exports nesridiscount;
}
