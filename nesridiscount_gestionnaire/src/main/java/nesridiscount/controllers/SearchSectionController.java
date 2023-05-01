package nesridiscount.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import nesridiscount.app.process.SearchPiecesProcess;
import nesridiscount.app.process.SearchPiecesProcess.PieceFilter;
import nesridiscount.app.ui_util.UiAlert;
import nesridiscount.app.util.FileExporter;
import nesridiscount.models.model.PiecesModel;

public class SearchSectionController extends Controller{
    @FXML
    private CheckBox enterpriseNameFilter;

    @FXML
    private CheckBox enterpriseRefFilter;

    @FXML
    private CheckBox internalRefFilter;

    @FXML
    private CheckBox nameFilter;

    @FXML
    private TableView<PiecesModel> resultArray;

    @FXML
    private TableColumn<String,Integer> resultId;

    @FXML
    private TableColumn<String,Integer> resultQuantity;

    @FXML
    private TableColumn<String,String> resultInternalRef;

    @FXML
    private TableColumn<String,String> resultLocation;

    @FXML
    private TableColumn<String,String> resultManufacturerName;

    @FXML
    private TableColumn<String,String> resultManufacturerRef;

    @FXML
    private TableColumn<String,String> resultPieceName;

    @FXML
    private TextField searchbar;

    private SearchPiecesProcess searchProcess;

    @FXML
    void scrollToTop(){
        ScrollPane pane = Controller.getPane();

        pane.setVvalue(pane.getVmin() );
    }

    @FXML
    void exportResults(MouseEvent e){
        ObservableList<PiecesModel> items = this.resultArray.getItems();
        
        // vérification d'existance de résultats
        if(items.size() == 0){
            UiAlert.newAlert(AlertType.INFORMATION,"Echec d'export","Il n'y a aucun résultat à exporter").show();

            return;
        }

        // création du contenu csv
        String csvData = PiecesModel.toStringHeader() + "\n";

        for(PiecesModel pieceModel : items) csvData += pieceModel.toString() + "\n";
        
        UiAlert.newAlert(
            AlertType.INFORMATION,
            "Export des résultats",
            FileExporter.export(csvData,".csv") ? 
                "Votre fichier a bien été sauvegardé" : 
                "Une erreur s'est produite lors de la sauvegarde de votre fichier veuillez retenter ou relancer l'application !").show();
    }

    @FXML
    void searchEvent(KeyEvent event) {
        try{
            this.searchProcess.execEntireProcess(this.searchbar.getText() + event.getText() );
        }
        catch(Exception e){}
    }

    @FXML
    void enterpriseNameFilterChange(MouseEvent event) {
        try{
            this.searchProcess.setFilterState(PieceFilter.ENTERPRISE_NAME,this.enterpriseNameFilter.isSelected() );
        }
        catch(Exception e){}
    }   

    @FXML
    void externalRefFilterChange(MouseEvent event) {
        try{
            this.searchProcess.setFilterState(PieceFilter.ENTERPRISE_REF,this.enterpriseRefFilter.isSelected() );
        }
        catch(Exception e){}
    }

    @FXML
    void internalRefFilterChange(MouseEvent event) {
        try{
            this.searchProcess.setFilterState(PieceFilter.INTERNAL_REF,this.internalRefFilter.isSelected() );
        }
        catch(Exception e){}
    }

    @FXML
    void nameFilterChange(MouseEvent event) {
        try{
            this.searchProcess.setFilterState(PieceFilter.NAME,this.nameFilter.isSelected() );
        }
        catch(Exception e){}
    }


    @FXML
    void initialize(){
        Label holder = new Label("Aucun résultat");

        holder.getStyleClass().add("holder");

        this.resultArray.setPlaceholder(holder);
        
        try{
            this.resultId.setCellValueFactory(new PropertyValueFactory<>("id") );
            this.resultInternalRef.setCellValueFactory(new PropertyValueFactory<>("internalRef") );
            this.resultLocation.setCellValueFactory(new PropertyValueFactory<>("location") );
            this.resultManufacturerName.setCellValueFactory(new PropertyValueFactory<>("enterpriseName") );
            this.resultManufacturerRef.setCellValueFactory(new PropertyValueFactory<>("externalRef") );
            this.resultPieceName.setCellValueFactory(new PropertyValueFactory<>("pieceName") );
            this.resultQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity") );
        }
        catch(Exception e){}

        // création du processus de recherche
        this.searchProcess = new SearchPiecesProcess(this.resultArray);
    }
}
