module nesridiscount {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires spring.security.crypto;

    opens nesridiscount.controllers to javafx.fxml;
    exports nesridiscount;
}
