package nesridiscount.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nesridiscount.App;
import nesridiscount.app.util.LoginManager;

public class LoginController extends Controller{

    @FXML
    private Button closeButton;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label errorMessage;

    private LoginManager loginManager;

    @FXML
    void closeApplication(MouseEvent event) {
        App.getStage().close();
    }

    @FXML
    void confirmLogin(MouseEvent event) {
        String username = this.username.getText();
        String password = this.password.getText();

        if(this.loginManager.crateSessionFrom(username, password) )
            this.switchToHomePage();
        else
            errorMessage.setText("Erreur - Compte non trouvé");
    }

    @FXML
    void initialize(){
        Stage window = App.getStage();

        window.centerOnScreen();
        window.initStyle(StageStyle.UNDECORATED);

        this.loginManager = new LoginManager();
    }
}
