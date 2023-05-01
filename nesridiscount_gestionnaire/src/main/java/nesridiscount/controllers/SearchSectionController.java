package nesridiscount.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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

    @FXML
    void initialize(){
        Label holder = new Label("Aucun résultat");

        holder.getStyleClass().add("holder");

        this.resultArray.setPlaceholder(holder);
    }

    @FXML
    void scrollToTop(){
        ScrollPane pane = Controller.getPane();

        pane.setVvalue(pane.getVmin() );
    }
}
