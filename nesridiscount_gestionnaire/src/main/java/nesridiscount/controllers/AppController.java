package nesridiscount.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import nesridiscount.App;
import nesridiscount.app.session.Session;
import nesridiscount.app.session.Session.Role;

public class AppController extends Controller{
    @FXML
    private ScrollPane toReplace;

    @FXML
    private Label addPiecesLabel;

    @FXML
    private VBox navbar;

    @FXML
    private Label parametersLabel;

    @FXML
    private FontAwesomeIconView search;

    @FXML
    private Label searchPiecesLabel;

    @FXML
    private Label username;

    @FXML
    void loggout(MouseEvent event) {
        Session.loggout();

        App.getStage().close();
    }

    @FXML
    void showAddPieces(MouseEvent event) {

    }

    @FXML
    void showFileParser(MouseEvent event) {
        Controller.switchToPage("parser");
    }

    @FXML
    void showHelp(MouseEvent event) {

    }

    @FXML
    void showParameters(MouseEvent event) {

    }

    @FXML
    void showSearchPieces(MouseEvent event) {
        Controller.switchToPage("search",true);
    }

    @FXML
    void switchToParser(MouseEvent event) {
        
    }

    @FXML
    void initialize(){
        username.setText(Session.getUsername().toUpperCase() + " - Connecté depuis le " + Session.getLoginTime() );

        ObservableList<Node> children = this.navbar.getChildren();

        // section à affiché
        String toShow = "search";
        boolean keepPage = true;

        // suppression des élements de navigation en fonction du role
        if(Session.getRole() == Role.Inter){
            children.removeAll(
                this.addPiecesLabel,
                this.searchPiecesLabel
            );

            toShow = "parser";
            keepPage = false;
        }

        Controller.setPane(this.toReplace);
        Controller.switchToPage(toShow,keepPage);
    }
}
