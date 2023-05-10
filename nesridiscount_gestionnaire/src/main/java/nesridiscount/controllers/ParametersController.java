package nesridiscount.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ParametersController {

    @FXML
    private VBox pageContainer;

    private VBox currentSectionContent;

    @FXML
    void switchToCreateAccount(MouseEvent event) {

    }

    @FXML
    void switchToManageAccount(MouseEvent event) {

    }

    @FXML
    void initialize(){
        this.currentSectionContent = null;

        this.switchToCreateAccount(null);
    }
}
