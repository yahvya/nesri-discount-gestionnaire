package nesridiscount.app.ui_util;

import java.sql.ResultSet;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nesridiscount.app.util.Action;
import nesridiscount.app.util.Disappear;
import nesridiscount.models.model.Model;
import nesridiscount.models.model.PiecesModel;

/**
 * formulaire de création de pièce
 */
public class PieceForm {
    private static final int min = 0;
    private static final int max = 1000000000;
    private static final int initial = 1;
    private static final double step = 0.1;

    private VBox container;
    private VBox form;

    private TextField pieceName;
    private TextField location;
    private TextField internalRef;
    private TextField externalRef;
    private Spinner<Double> sellPrice;
    private Spinner<Double> buyPrice;
    
    private Spinner<Integer> quantity;

    private String creatorName;

    private Action toDoOnDelete;

    public PieceForm(VBox container){
        this.container = container;
        this.toDoOnDelete = null;
        this.creatorName = null;
    }

    /**
     * crée le formulaire de création de la pièce 
     * @return le formulaire
     */
    public VBox createForm(){
        this.pieceName = this.createTextField("Entrez le nom de la pièce");
        this.location = this.createTextField("Entrez l'emplacement de la pièce");
        this.internalRef = this.createTextField("Référence interne de la pièce");
        this.externalRef = this.createTextField("Référence fabriquant de la pièce");
        this.quantity = new Spinner<Integer>(
            PieceForm.min,
            PieceForm.max,
            PieceForm.initial
        );

        this.quantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                PieceForm.min,
                PieceForm.max,
                PieceForm.initial
            )
        );
        this.quantity.setEditable(true);

        HBox refLine = new HBox(20,this.internalRef,this.externalRef);

        ChoiceBox<String> creatorsBox = new ChoiceBox<>();

        ObservableList<String> creatorsList = creatorsBox.getItems();

        // récupération des fabriquants
        try{
            ResultSet results = Model.customResultQuery(PiecesModel.class,"select distinct(enterpriseName) from PiecesModel");

            PiecesModel tmpModel = new PiecesModel();

            // récupération des créateurs
            while(results.next() ){
                tmpModel.getColumnsManagers().forEach((key,manager) -> manager.setFieldFrom(results) );

                creatorsList.add(tmpModel.getEnterpriseName().toUpperCase() );
            }
        }
        catch(Exception e){}

        creatorsBox.setOnAction((event) -> this.creatorName = creatorsBox.getValue() );

        TextField creatorNameField = this.createTextField("Entrez le nom du fabriquant");

        creatorNameField.setOnKeyTyped((e) -> this.creatorName = creatorNameField.getText() );

        ToggleGroup group = new ToggleGroup();

        RadioButton addNewRadio = new RadioButton("Ajouter un nouveau fabriquant");
        RadioButton useExistantRadio = new RadioButton("Utiliser un fabriquant existant");

        addNewRadio.setToggleGroup(group);
        addNewRadio.setSelected(true);
        
        useExistantRadio.setToggleGroup(group);

        group.selectedToggleProperty().addListener((e) -> {
            Toggle selectedToggle = group.getSelectedToggle();

            ObservableList<Node> children = this.form.getChildren();

            // ajout d'un nouvel élement else affichage de la liste des fabriquants existant
            if(selectedToggle == addNewRadio){
                try{
                    int index = children.indexOf(creatorsBox);
                    
                    children.remove(creatorsBox);

                    children.add(index,creatorNameField);

                    this.creatorName = creatorNameField.getText();
                }
                catch(Exception exception){}

            }
            else{
                try{
                    int index = children.indexOf(creatorNameField);

                    children.remove(creatorNameField);

                    children.add(index,creatorsBox);

                    this.creatorName = creatorsBox.getValue();
                }
                catch(Exception exception){}
            }
        });

        HBox choiceRow = new HBox(20);

        choiceRow.getChildren().addAll(addNewRadio,useExistantRadio);
        choiceRow.getStyleClass().add("bordered");
        choiceRow.setAlignment(Pos.CENTER_LEFT);

        Button deleteButton = new Button();

        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TRASH,"17.0");

        icon.getStyleClass().add("icon");

        Alert confirmationAlert = UiAlert.newAlert(AlertType.CONFIRMATION,"Confirmer la suppression","Êtes vous sûr de vouloir abandonner la création de cette pièce ?");

        deleteButton.setGraphic(icon);
        deleteButton.setTooltip(new Tooltip("Supprimer la pièce de la liste") );
        deleteButton.setOnMouseClicked((e) -> {
            confirmationAlert.showAndWait();

            if(confirmationAlert.getResult() == ButtonType.OK){
                Disappear.disappear(this.form,() -> {
                    this.container.getChildren().remove(this.form);
                    
                    if(this.toDoOnDelete != null) this.toDoOnDelete.doAction();
                },Duration.millis(400) );
            }
        });

        HBox quantityLine = new HBox(20,this.quantity,deleteButton);
        
        quantityLine.setAlignment(Pos.CENTER_LEFT);

        this.sellPrice = new Spinner<>(
            PieceForm.min,
            PieceForm.max,
            PieceForm.initial,
            PieceForm.step
        );
        this.buyPrice = new Spinner<>(
            PieceForm.min,
            PieceForm.max,
            PieceForm.initial,
            PieceForm.step
        );
        
        this.sellPrice.setEditable(true);
        this.buyPrice.setEditable(true);

        this.sellPrice.setPromptText("Prix de vente");
        this.buyPrice.setPromptText("Prix d'achat");
        this.sellPrice.setTooltip(new Tooltip("Prix de vente") );
        this.buyPrice.setTooltip(new Tooltip("Prix d'achat") );

        HBox pricesLine = new HBox(20,this.sellPrice,this.buyPrice);

        this.form = new VBox(20,
            this.pieceName,
            this.location,
            refLine,
            choiceRow,
            creatorNameField,
            new Label("Quantité"),
            quantityLine,
            new Label("Prix de vente et prix d'achat"),
            pricesLine
        );

        this.form.getStyleClass().add("new-form");

        return this.form;
    }

    /**
     * crée le model pièce à partir du formulaire
     * @return le model crée ou null en cas d'erreur dans le formulaire
     */
    public PiecesModel getPiece(){
        try{
            String pieceName = this.pieceName.getText();
            String externalRef = this.externalRef.getText();
            String internalRef = this.internalRef.getText();
            String location = this.location.getText();

            // vérification de la validité des données
            if(
                this.creatorName == null || !this.inLimit(this.creatorName.length(),1,255) ||
                !this.inLimit(pieceName.length(),1,255) ||
                !this.inLimit(externalRef.length(),1,255) ||
                !this.inLimit(internalRef.length(),1,255) ||
                !this.inLimit(location.length(),2,255)
            ) throw new Exception();

            
            return new PiecesModel(this.quantity.getValue(),pieceName,this.creatorName,externalRef,internalRef,location,this.buyPrice.getValue(),this.sellPrice.getValue() );
        }
        catch(Exception e){
            return null;
        }
    }

    /**
     * supprime le formulaire
     */
    public void delete(){
        Disappear.disappear(this.form,() -> this.container.getChildren().remove(this.form),Duration.millis(400) );
    }

    public void setToDoOnDelete(Action toDoOnDelete){
        this.toDoOnDelete = toDoOnDelete;
    }

    /**
     * 
     * @param len
     * @param min
     * @param max
     * @return si la taille se trouve dans les limites inclus
     */
    private boolean inLimit(int len,int min,int max){
        return len >= min && len <= max;
    }

    /**
     * crée un textfield custom app
     * @param placeholder
     * @return le textfield
     */
    private TextField createTextField(String placeholder){
        TextField field = new TextField();

        field.setPromptText(placeholder);
        field.setMinWidth(300);

        return field;
    }
}
