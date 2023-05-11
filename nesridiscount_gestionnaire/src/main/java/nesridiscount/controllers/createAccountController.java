package nesridiscount.controllers;

import javafx.fxml.FXML;    
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import nesridiscount.app.session.Session.Role;
import nesridiscount.app.ui_util.UiAlert;
import nesridiscount.models.model.Model;
import nesridiscount.models.model.UsersModel;
import nesridiscount.models.util.Condition;
import nesridiscount.models.util.Condition.Separator;

public class CreateAccountController {
    @FXML
    private TextField password;

    @FXML
    private TextField username;
    
    @FXML
    private RadioButton superChoice;

    @FXML
    private RadioButton adminChoice;

    @FXML
    private RadioButton limitedChoice;

    private ToggleGroup choiceButtons;

    @FXML   
    void createAccount(MouseEvent event) {
        String username = this.username.getText();
        String password = this.password.getText();
        
        RadioButton choosedRoleButton = (RadioButton) this.choiceButtons.getSelectedToggle();

        int usernameLen = username.length();
        int passwordLen = password.length();

        // vérification de la validité du formulaire
        if(usernameLen <= 50 && usernameLen >= 1 && passwordLen <= 30 && passwordLen >= 8){
            // vérification de non existance d'utilisateur
            if(Model.find(UsersModel.class,new Condition[]{new Condition<String>("username",username,Separator.NULL)} ).size() != 0){
                UiAlert.newAlert(AlertType.ERROR,"Mauvais nom d'utilisateur","Le nom d'utilisateur donné est déjà utilisé sur un compte !").show();
                
                return;
            }

            int role = Role.Inter.roleId;

            if(choosedRoleButton == this.adminChoice)
                role = Role.Admin.roleId;
            else if(choosedRoleButton == this.superChoice)
                role = Role.Special.roleId;
            try{
                if(new UsersModel(username,password,role).create() ){
                    this.username.clear();
                    this.password.clear();
                    
                    UiAlert.newAlert(AlertType.INFORMATION,"Compte crée","Le compte de " + username + " a bien été crée").show();
                }
                else throw new Exception();
            }
            catch(Exception e){
                UiAlert.newAlert(AlertType.ERROR,"Erreur technique","Une erreur s'est produite lors de la création du compte,veuillez retenter").show();
            }
        }   
        else UiAlert.newAlert(AlertType.ERROR,"Formulaire incorrect","Veuillez vérifier les données du formulaire").show();
    }

    @FXML
    void initialize(){
        // linkage des bouttons radio de choix de rôle
        this.choiceButtons = new ToggleGroup();

        this.adminChoice.setToggleGroup(this.choiceButtons);
        this.superChoice.setToggleGroup(this.choiceButtons);
        this.limitedChoice.setToggleGroup(this.choiceButtons);

        this.limitedChoice.setSelected(true);
    }
}
