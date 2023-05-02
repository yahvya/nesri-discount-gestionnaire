package nesridiscount.app.ui_util;

import java.io.File;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nesridiscount.app.util.Action;
import nesridiscount.app.util.Disappear;

/**
 * formulaire de la section parser
 */
public class ParserForm {
    private VBox container;
    private VBox parent;

    private TextField columnsToParse;
    private TextField columnsFormula;

    private File file;

    private Action toDoOnErase = null;

    public ParserForm(File file,VBox container){
        this.file = file;
        this.container = container;
    }

    /**
     * crée un formulaire
     * @return le formulaire crée
     */
    public VBox createForm(){
        Label path = new Label(this.file.getName() );

        path.getStyleClass().add("file-title");
        path.setPrefWidth(200);

        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH,"14.0");

        deleteIcon.getStyleClass().add("icon");
        
        Label deleteIconLabel = new Label(null,deleteIcon);

        deleteIconLabel.setTooltip(new Tooltip("Supprimer") );
        // gestion de la suppression de l'élement
        deleteIconLabel.setOnMouseClicked((e) -> this.delete(true) );
    
        // création de la ligne chemin
        HBox pathLine = new HBox(20,path,deleteIconLabel);

        // création des champs de saisi
        this.columnsToParse = new TextField();

        this.columnsToParse.setPromptText("Numéro des colonnes à traiter séparés d'une virgule");
        this.columnsToParse.getStyleClass().add("");
        VBox.setMargin(this.columnsToParse,new Insets(20,0,20,0) );

        this.columnsFormula = new TextField();

        this.columnsFormula.setPromptText("Formules lié aux colonnes séparés d'une virgule (numero:formule,)");

        // création du conteneur formulaire

        this.parent = new VBox(0,new Node[]{pathLine,this.columnsToParse,this.columnsFormula});

        this.parent.getStyleClass().add("form");
        this.parent.setPadding(new Insets(0,0,0,10) );

        VBox.setMargin(this.parent,new Insets(15,0,20,20) );

        return this.parent;
    }

    /**
     * supprime cet élement 
     * @param execToDo défini si la fonction après doit être exécuté
     * @return this
     */
    public ParserForm delete(boolean execToDo){
        Disappear.disappear(this.parent,() -> {
            this.container.getChildren().remove(this.parent); 
        
            if(this.toDoOnErase != null && execToDo){
                try{
                    this.toDoOnErase.doAction();
                }
                catch(Exception exception){}
            }
        },Duration.millis(450.0) );
        
        return this;
    }

    /**
     * défini l'action à exécuter à l'effacement 
     * @param toDo
     * @return this
     */
    public ParserForm setToDoOnErase(Action toDo){
        this.toDoOnErase = toDo;

        return this;
    }

    public VBox getParent(){
        return this.parent;
    }

    public File getSelectedFile(){
        return this.file;
    }
}
