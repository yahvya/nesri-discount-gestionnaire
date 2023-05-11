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
    private FontAwesomeIconView addPiece;

    @FXML
    private Label addPiecesLabel;

    @FXML
    private Label calendarLabel;

    @FXML
    private VBox navbar;

    @FXML
    private Label parametersLabel;

    @FXML
    private FontAwesomeIconView search;

    @FXML
    private Label searchPiecesLabel;

    @FXML
    private ScrollPane toReplace;

    @FXML
    private Label username;

    @FXML
    void loggout(MouseEvent event) {
        Session.loggout();

        App.getStage().close();
    }

    @FXML
    void switchToAddPieces(MouseEvent event) {
        Controller.switchToPage("add-pieces");
    }

    @FXML
    void switchToFileParser(MouseEvent event) {
        Controller.switchToPage("parser",true);
    }

    @FXML
    void switchToHelp(MouseEvent event) {
        Controller.switchToPage("help",true);
    }

    @FXML
    void switchToParameters(MouseEvent event) {
        Controller.switchToPage("parameters");
    }

    @FXML
    void switchToSearchPieces(MouseEvent event) {
        Controller.switchToPage("search",true);
    }

    @FXML
    void switchToCalendar(MouseEvent event) {
        Controller.switchToPage("mission-calendar");
    }

    @FXML
    void initialize(){
        username.setText(Session.getUsername().toUpperCase() + " - Connecté depuis le " + Session.getLoginTime() );

        ObservableList<Node> children = this.navbar.getChildren();

        // section à affiché
        String toShow = "search";
        boolean keepPage = true;

        Role userRole = Session.getRole();

        // suppression des élements de navigation en fonction du role
        if(userRole == Role.Inter){
            children.removeAll(
                this.addPiecesLabel,
                this.searchPiecesLabel
            );

            toShow = "parser";
        }

        if(userRole != Role.Admin){
            children.removeAll(
                this.calendarLabel,
                this.parametersLabel
            );
        }

        Controller.setPane(this.toReplace);
        Controller.switchToPage(toShow,keepPage);
    }
}
