package nesridiscount.controllers;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import nesridiscount.App;
import nesridiscount.app.ui_util.ParserForm;
import nesridiscount.app.ui_util.UiAlert;

public class ParserController extends Controller {
    private static int LIMIT = 5;

    @FXML
    private VBox formsContainer;

    @FXML
    private FlowPane parsedFiles;

    private ArrayList<ParserForm> forms;

    @FXML
    void addFile(MouseEvent event) {
        if(this.forms.size() + 1 > ParserController.LIMIT){
            UiAlert.newAlert(AlertType.INFORMATION,"Limite atteinte","Vous avez atteint le nombre de fichiers autorisés à la fois").show();
            
            return;
        }

        // sélection du fichier à traité
        FileChooser chooser = new FileChooser();

        chooser.setTitle("Choisissez le fichier");
        chooser.getExtensionFilters().add(new ExtensionFilter("Extensions supportés","*.csv","*.xlxs") );
        chooser.setInitialDirectory(FileSystemView.getFileSystemView().getDefaultDirectory() );

        File choosedFile = chooser.showOpenDialog(App.getStage() );

        // ajout du fichier dans les élements à traiter
        if(choosedFile != null){
            ParserForm form = new ParserForm(choosedFile,this.formsContainer);

            form.setToDoOnErase(() -> this.forms.remove(form) );

            this.forms.add(0,form);

            this.formsContainer.getChildren().add(form.createForm() );
        }
    }

    @FXML
    void parseFiles(MouseEvent event) {

    }

    @FXML
    void initialize(){
        this.forms = new ArrayList<>();
    }
}
