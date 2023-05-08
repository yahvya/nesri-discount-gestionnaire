package nesridiscount.app.ui_util;

import java.time.format.DateTimeFormatter;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    private TextField technicianName;
    
    private TextArea missionDescription;

    private DateTimePicker missionDate;

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

        this.technicianName.setPromptText("Entrez le nom du technician");
        this.technicianName.setPrefWidth(320);
        this.technicianName.setPrefHeight(40);

        this.missionDescription = new TextArea();

        this.missionDescription.setPromptText("Entrez la description de la mission");
        this.missionDescription.setWrapText(true);
        this.missionDescription.setPrefWidth(620);
        this.missionDescription.setMinHeight(110);

        this.missionDate = new DateTimePicker();

        this.missionDate.setPromptText("Date et heure de l'intervation");

        Button deleteButton = new Button("Supprimer");

        deleteButton.setOnMouseClicked((e) -> this.delete() );

        this.form = new VBox(30,deleteButton,this.technicianName,this.missionDate,this.missionDescription);

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
                    this.missionDate.getDateTimeValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") ),
                    technicianName
                );
            }
            catch(Exception e){}
        }

        return model;
    }
}
