package nesridiscount.app.ui_util;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nesridiscount.app.util.Disappear;
import nesridiscount.app.util.FileExporter;

/**
 * résultat de fichier traité
 */
public class FileParseResult {
    private FlowPane parentContainer;
    private VBox resultContainer;

    private File selectedFile;

    private HBox downloadLine;

    private Label stateLabel;

    public FileParseResult(FlowPane parentContainer,File selectedFile){
        this.selectedFile = selectedFile;
        this.parentContainer = parentContainer;
    }

    /**
     * crée le conteneur résultat en état d'attente
     * @return l'élement
     */
    public VBox createResult(){
        Label filepathLabel = new Label(this.selectedFile.getName() );

        filepathLabel.getStyleClass().add("bold");

        this.stateLabel = new Label("Attente de traitement du fichier");
        this.stateLabel.setWrapText(true);
        this.stateLabel.getStyleClass().add("sm");

        this.downloadLine = new HBox(10,this.stateLabel);

        this.downloadLine.setAlignment(Pos.CENTER);

        this.resultContainer = new VBox(10,filepathLabel,this.downloadLine);
        
        this.resultContainer.getStyleClass().add("parse-result");
        this.resultContainer.setMaxWidth(340);

        return this.resultContainer;
    }

    /**
     * met à jour l'interface en autorisant le téléchargement
     * @param csvContent
     */
    public void setAsParsedIn(String csvContent){
        this.stateLabel.setText("Le fichier peut être téléchargé");
        
        Button downloadButton = new Button("Télécharger");

        downloadButton.setTooltip(new Tooltip("Télécharger le fichier de prix traité") );
        downloadButton.setOnMouseClicked((e) -> {
            try{
                if(FileExporter.export(csvContent,"*.csv") ) 
                    this.disappear();
                else
                    UiAlert.newAlert(AlertType.INFORMATION,"Echec de sauvegarde","Le fichier n'a pas été sauvegardé, veuillez retenter").show();
            }
            catch(Exception exception){}
        });

        this.downloadLine.getChildren().add(downloadButton);
    }   


    /**
     * défini l'élément comme échoué à parser
     */
    public void setAsFailed(){
        this.stateLabel.setText("Erreur lors du traitement du fichier (abandon)");

        Button resignButton = new Button("Abandonner");

        resignButton.setOnMouseClicked((e) -> this.disappear() );
        resignButton.setTooltip(new Tooltip("Supprimer cet élement") );
        
        this.downloadLine.getChildren().add(resignButton);
    }

    /**
     * supprime l'élement
     */
    private void disappear(){
        Disappear.disappear(this.resultContainer,() -> this.parentContainer.getChildren().remove(this.resultContainer),Duration.millis(500) );
    }
}
