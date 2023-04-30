package nesridiscount.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nesridiscount.app.session.Session;

public class AppController {

    @FXML
    private Label username;

    @FXML
    void switchToParser(MouseEvent event) {

    }

    @FXML
    void initialize(){
        username.setText(Session.getUsername().toUpperCase() + " - Connecté depuis le " + Session.getLoginTime() );
    }
}
