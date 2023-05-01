package nesridiscount.controllers;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import nesridiscount.app.ui_util.UiAlert;
import nesridiscount.models.model.PiecesModel;

public class SearchSectionController {

    @FXML
    private TableView<?> resultArray;

    @FXML
    private TableColumn<?, ?> resultId;

    @FXML
    private TableColumn<?, ?> resultInternalRef;

    @FXML
    private TableColumn<?, ?> resultLocation;

    @FXML
    private TableColumn<?, ?> resultManufacturerName;

    @FXML
    private TableColumn<?, ?> resultManufacturerRef;

    @FXML
    private TableColumn<?, ?> resultPieceName;

    @FXML
    private TableColumn<?, ?> resultQuantity;

    private ArrayList<PiecesModel> results;

    @FXML
    void scrollToTop(){
        ScrollPane pane = Controller.getPane();

        pane.setVvalue(pane.getVmin() );
    }

    @FXML
    void exportResults(MouseEvent e){
        // vérification d'existance de résultats
        if(this.results.size() == 0){
            UiAlert.newAlert(AlertType.INFORMATION,"Echec d'export","Il n'y a aucun résultat à exporter").show();

            return;
        }
    }

    @FXML
    void initialize(){
        Label holder = new Label("Aucun résultat");

        holder.getStyleClass().add("holder");

        this.resultArray.setPlaceholder(holder);
        this.results = new ArrayList<>();
    }
}
