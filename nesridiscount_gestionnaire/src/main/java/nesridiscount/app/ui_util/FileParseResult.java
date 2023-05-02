package nesridiscount.app.ui_util;

import java.io.File;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * résultat de fichier traité
 */
public class FileParseResult {
    private Parent parentContainer;
    private VBox resultContainer;

    private File selectedFile;

    public FileParseResult(Parent parentContainer,File selectedFile){
        this.selectedFile = selectedFile;
        this.parentContainer = parentContainer;
    }

    /**
     * crée le conteneur résultat en état d'attente
     * @return l'élement
     */
    public VBox createResult(){
        this.resultContainer = new VBox(10);

        return this.resultContainer;
    }

    /**
     * met à jour l'interface en autorisant le téléchargement
     * @param parsedFile
     */
    public void setAsParsedIn(File parsedFile){
        
    }   


    /**
     * défini l'élément comme échoué à parser
     */
    public void setAsFailed(){

    }
}
