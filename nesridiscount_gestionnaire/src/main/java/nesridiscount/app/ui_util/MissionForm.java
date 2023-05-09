package nesridiscount.app.ui_util;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nesridiscount.app.util.Action;
import nesridiscount.app.util.Disappear;
import nesridiscount.models.model.MissionsModel;
import tornadofx.control.DateTimePicker;

/**
 * formulaire de création d'une mission
 */
public class MissionForm {
    private VBox container;

    private VBox form;

    private HBox buttonsRow;

    private TextField technicianName;
    
    private TextArea missionDescription;

    private DateTimePicker missionDate;

    private Button deleteButton;

    private Action toDoOnDelete = null;

    public MissionForm(VBox container){
        this.container = container;
    }

    /**
     * crée le formulaire
     * @return les données du formulaire
     */
    public VBox createForm(){
        this.technicianName = new TextField();

        this.technicianName.setPromptText("Entrez le nom du technicien");
        this.technicianName.setPrefWidth(320);
        this.technicianName.setPrefHeight(40);

        this.missionDescription = new TextArea();

        this.missionDescription.setPromptText("Entrez la description de la mission");
        this.missionDescription.setWrapText(true);
        this.missionDescription.setPrefWidth(620);
        this.missionDescription.setMinHeight(110);

        this.missionDate = new DateTimePicker();

        this.missionDate.setPromptText("Date et heure de l'intervation");

        this.deleteButton = new Button("Supprimer");

        this.deleteButton.setOnMouseClicked((e) -> this.delete() );

        this.buttonsRow = new HBox(10,this.deleteButton);

        this.form = new VBox(30,this.buttonsRow,this.technicianName,this.missionDate,this.missionDescription);

        return this.form;
    }

    /**
     * supprime le formulaire
     */
    public void delete(){
        Disappear.disappear(this.form,() -> {
            this.container.getChildren().remove(this.form);
            
            if(this.toDoOnDelete != null) this.toDoOnDelete.doAction();
        },Duration.millis(400) );
    }

    public MissionForm setToDoOnDelete(Action toDoOnDelete){
        this.toDoOnDelete = toDoOnDelete;
        
        return this;
    }

    public TextField getTechnicianNameField(){
        return this.technicianName;
    }

    public TextArea getMissionDescriptionField(){
        return this.missionDescription;
    }

    public DateTimePicker getMissionDatePicker(){
        return this.missionDate;
    }

    public HBox getButtonsRow(){
        return this.buttonsRow;
    }

    public Button getDeleteButton(){
        return this.deleteButton;
    }

    /**
     * crée le model à partir du formulaire
     * @return le model crée ou null en cas d'échec
     */
    public MissionsModel getMission(){
        MissionsModel model = null;

        String technicianName = this.technicianName.getText();
        String missionDescription = this.missionDescription.getText();

        int technicianNameLen = technicianName.length();
    
        if(technicianNameLen > 1 && technicianNameLen < 50 && missionDescription.length() > 1){
            try{
                model = new MissionsModel(
                    missionDescription,
                    this.missionDate.getDateTimeValue().format(MissionsModel.momentFormatter),
                    technicianName
                );
            }
            catch(Exception e){}
        }

        return model;
    }
}
