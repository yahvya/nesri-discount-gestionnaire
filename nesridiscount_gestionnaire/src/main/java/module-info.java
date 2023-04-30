module nesridiscount {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires spring.security.crypto;
    requires de.jensd.fx.glyphs.fontawesome;
    requires json.simple;

    opens nesridiscount.controllers to javafx.fxml;
    exports nesridiscount;
}
