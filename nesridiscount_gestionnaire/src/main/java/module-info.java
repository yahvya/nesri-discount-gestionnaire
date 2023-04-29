module nesridiscount {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens nesridiscount to javafx.fxml;
    exports nesridiscount;
}
