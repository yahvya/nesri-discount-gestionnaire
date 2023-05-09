package nesridiscount.controllers;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import nesridiscount.app.ui_util.PieceForm;
import nesridiscount.app.ui_util.UiAlert;
import nesridiscount.models.model.PiecesModel;

public class AddPiecesController {

    private ArrayList<PieceForm> forms;

    @FXML
    private VBox container;


    @FXML
    void addNewPiece(MouseEvent event) {
        PieceForm form = new PieceForm(this.container);

        form.setToDoOnDelete(() -> this.forms.remove(form) );

        this.forms.add(form);

        this.container.getChildren().add(2,form.createForm() );
    }       

    @FXML
    void confirmPiecesCreation(MouseEvent event) {
        if(this.forms.size() == 0){
            UiAlert.newAlert(AlertType.INFORMATION,"Echec de création","Veuillez ajouter au moins une pièce").show();

            return;
        }

        ArrayList<PiecesModel> toCreate = new ArrayList<>();

        int index = 0;

        // récupération des model lié au formulaire
        for(PieceForm form : this.forms){
            PiecesModel model = form.getPiece();

            index++;

            if(model == null){
                UiAlert.newAlert(
                    AlertType.ERROR,
                    "Echec de création",
                    "La pièce numéro (" + Integer.toString(index) + ") est mal formé"
                ).show();
                
                continue;
            }

            toCreate.add(model);
            
            form.delete();
        }

        for(PiecesModel model : toCreate){
            if(!model.create() ){
                UiAlert.newAlert(
                    AlertType.ERROR,
                    "Echec de création",
                    String.join(" ","Une erreur technique s'est produite lors de la création de la pièce (",model.getPieceName(),")")
                ).show();

                index++;
            }
        }
    }   

    @FXML
    void initialize(){
        this.forms = new ArrayList<>();

        this.addNewPiece(null);
    }
}
