package nesridiscount.app.ui_util;

import java.time.LocalDateTime;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import nesridiscount.app.util.Action;
import nesridiscount.app.util.Disappear;
import nesridiscount.models.model.MissionsModel;
import tornadofx.control.DateTimePicker;

/**
 * résultat de recherche d'une mission
 */
public class MissionSearchResult {
    private static final Alert deleteConfirmation = UiAlert.newAlert(AlertType.CONFIRMATION,"Confirmer le suppression", "Êtes vous sûr de vouloir supprimer cette mission ?");
    private static final Alert updateConfirmation = UiAlert.newAlert(AlertType.CONFIRMATION,"Confirmer la mise à jour", "Êtes vous sûr de vouloir modifier cette mission ?");
    private static final Alert deleteFailAlert = UiAlert.newAlert(AlertType.INFORMATION,"Echec de suppression","Une erreur s'est produite lors de la suppression de la mission, veuillez retenter.");
    private static final Alert badFormAlert = UiAlert.newAlert(AlertType.ERROR,"Erreur","La mission est mal formé");
    private static final Alert updateFailAlert = UiAlert.newAlert(AlertType.INFORMATION,"Echec de mise à jour","Une erreur s'est produite lors de la mise à jour de la mission, veuillez retenter.");
    private static final Alert updateSuccessAlert = UiAlert.newAlert(AlertType.INFORMATION,"Succès","La mise à jour de la mission a bien été faîtes");

    private VBox container;
    
    private VBox result;

    private MissionsModel resultModel;

    private MissionForm linkedForm;

    private Action toDoOnDelete = null;

    public MissionSearchResult(VBox container,MissionsModel resultModel){
        this.container = container;
        this.resultModel = resultModel;
    }

    /**
     * crée la ligne de résultat
     * @return le conteneur du résultat
     */
    public VBox createResult(){
        this.linkedForm = new MissionForm(this.container);

        this.result = this.linkedForm.createForm();

        TextField technicianNameField = this.linkedForm.getTechnicianNameField();
        
        TextArea missionDescription = this.linkedForm.getMissionDescriptionField();

        DateTimePicker missionDatePicker = this.linkedForm.getMissionDatePicker();

        Button deleteButton = this.linkedForm.getDeleteButton();
        Button updateButton = new Button("Modifier");

        // ajout du boutton de mise à jour
        this.linkedForm.getButtonsRow().getChildren().add(updateButton);

        // affectation des valeurs du model
        technicianNameField.setText(this.resultModel.technician);
        missionDescription.setText(this.resultModel.description);
        missionDatePicker.setDateTimeValue(LocalDateTime.parse(this.resultModel.moment,MissionsModel.momentFormatter) );

        // redéfinition de l'évenement de suppression   
        deleteButton.setOnMouseClicked((e) -> {
            MissionSearchResult.deleteConfirmation.showAndWait();
            
            if(deleteConfirmation.getResult() == ButtonType.OK){
                if(this.resultModel.delete() )
                    this.delete();
                else
                    MissionSearchResult.deleteFailAlert.show();
            }
        });

        // ajout de l'évenement de l'évenement de mise à jour des données
        updateButton.setOnMouseClicked((e) -> {
            MissionSearchResult.updateConfirmation.showAndWait();

            if(updateConfirmation.getResult() == ButtonType.OK){
                MissionsModel newModel = this.linkedForm.getMission();

                if(newModel != null){
                    newModel.id = this.resultModel.id;

                    if(newModel.update("id") )
                        MissionSearchResult.updateSuccessAlert.show();
                    else
                        MissionSearchResult.updateFailAlert.show();
                }
                else badFormAlert.show();
            }
        });

        this.result.getStyleClass().add("mission-search-result");

        return this.result;
    }

    public MissionSearchResult setToDoOnDelete(Action toDoOnDelete){
        this.toDoOnDelete = toDoOnDelete;

        return this;
    }

    public MissionSearchResult delete(){
        Disappear.disappear(this.result,() -> {
            this.container.getChildren().remove(this.result);

            if(this.toDoOnDelete != null) this.toDoOnDelete.doAction();
        },Duration.millis(400) );

        return this;
    }
}