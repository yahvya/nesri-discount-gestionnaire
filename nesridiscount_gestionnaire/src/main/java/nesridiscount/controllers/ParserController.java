package nesridiscount.controllers;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import nesridiscount.App;
import nesridiscount.app.ui_util.FileParseResult;
import nesridiscount.app.ui_util.ParserForm;
import nesridiscount.app.ui_util.UiAlert;
import nesridiscount.app.util.Disappear;
import nesridiscount.app.util.PriceFileParser;

public class ParserController extends Controller {
    private static int LIMIT = 5;

    @FXML
    private VBox formsContainer;

    @FXML
    private FlowPane parsedFiles;

    private ArrayList<ParserForm> forms;

    private int toWait;

    private boolean pageIsLocked = false;

    @FXML
    void addFile(MouseEvent event) {
        if(this.pageIsLocked){
            this.showLockMessage();

            return;
        }

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
        if(this.pageIsLocked){
            this.showLockMessage();

            return;
        }

        this.toWait = this.forms.size();

        if(this.toWait == 0){
            UiAlert.newAlert(AlertType.INFORMATION,"Pas de fichier trouvé","Il n'y a aucun fichier à traiter").show();

            return;
        }

        ObservableList<Node> stateContainerChildren = this.parsedFiles.getChildren(); 
        ObservableList<Node> formsChildren = this.formsContainer.getChildren();

        this.setToWait(this.toWait);

        // lancement du traitement des fichiers
        this.forms.forEach(form -> {
            File selectedFile = form.getSelectedFile();

            FileParseResult result = new FileParseResult(this.parsedFiles,selectedFile);

            // ajout de l'afficheur de résultat dans la page
            stateContainerChildren.add(result.createResult() );

            // suppression du formulaire
            VBox formContainer = form.getParent();

            Disappear.disappear(formContainer,() -> formsChildren.remove(form.getParent() ),Duration.millis(700) );

            try{

                PriceFileParser parser = new PriceFileParser(selectedFile,form.getColumnsToParse(),form.getColumnsFormula() );

                parser.setToDoOnSuccess(() -> {
                    Platform.runLater(() -> {
                        result.setAsParsedIn(parser.getCsvContent() );
    
                        this.setToWait(this.toWait - 1);
                    });
                });

                parser.setToDoOnFail(() -> {
                    Platform.runLater(() -> {
                        result.setAsFailed();
    
                        this.setToWait(this.toWait - 1);
                    });
                } );

                // lancement du parseur
                parser.start();
            }
            catch(Exception e){
                result.setAsFailed();

                this.setToWait(this.toWait - 1);
            }
        });

        this.forms.clear();
        this.lockPage();
    }

    @FXML
    void initialize(){
        this.forms = new ArrayList<>();
    }

    /**
     * défini le nombre d'élement à attendre et met à jour l'interface
     * @param newValue
     */
    synchronized private void setToWait(int newValue){
        this.toWait = newValue;

        // mise à jour de l'interface

        if(this.toWait == 0) this.unlockPage();
    }

    /**
     * bloque la page
     */
    private void lockPage(){
        this.pageIsLocked = true;
    }

    /**
     * débloque la page
     */
    private void unlockPage(){
        this.pageIsLocked = false;
    }   

    /**
     * affiche le message de patienter
     */
    private void showLockMessage(){
        UiAlert.newAlert(AlertType.INFORMATION,"Action en cours","Une action est déjà en cours veuillez attendre la fin").show();
    }
}
