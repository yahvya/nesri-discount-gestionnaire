package nesridiscount.controllers;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nesridiscount.app.session.Session.Role;
import nesridiscount.app.ui_util.UiAlert;
import nesridiscount.app.util.Disappear;
import nesridiscount.models.model.Model;
import nesridiscount.models.model.UsersModel;
import nesridiscount.models.util.Condition;
import nesridiscount.models.util.Condition.Separator;
import nesridiscount.models.util.Condition.Type;

public class ManageAccountController {
    @FXML
    private VBox resultsContainer;

    @FXML
    private TextField searchbar;

    @FXML
    void searchAccounts(MouseEvent event) {
        String search = this.searchbar.getText();

        if(search.length() == 0){
            UiAlert.newAlert(AlertType.INFORMATION,"Recherche invalide","Veuillez saisir le nom d'utilisateur à cherché").show();
            
            return;
        }

        try{    
            // recherche par nom d'utilisateur
            ArrayList<? extends Model> results = Model.find(UsersModel.class,new Condition[]{
                new Condition<Integer>("role",Role.Admin.roleId,Type.NOT_EQUAL,Separator.AND),
                new Condition<String>("username",search,Separator.NULL)
            });

            @SuppressWarnings("unchecked")
            ArrayList<UsersModel> resultsCast = (ArrayList<UsersModel>) results;

            this.showResults(resultsCast);
        }
        catch(Exception e){}
    }

    @FXML
    void showAll(MouseEvent event){
        try{    
            ArrayList<? extends Model> results = Model.find(UsersModel.class,new Condition[]{
                new Condition<Integer>("role",Role.Admin.roleId,Type.NOT_EQUAL,Separator.NULL)
            });

            @SuppressWarnings("unchecked")
            ArrayList<UsersModel> resultsCast = (ArrayList<UsersModel>) results;

            this.showResults(resultsCast);
        }
        catch(Exception e){}
    }

    @FXML
    void initialize(){
        this.showAll(null);
    } 

    /**
     * affiche les résultats
     * @param results
     */
    private void showResults(ArrayList<UsersModel> results){
        ObservableList<Node> children = this.resultsContainer.getChildren();

        children.clear();

        // création des résultats
        results.forEach((result) -> {
            TextField usernameField = new TextField(result.username);
            TextField newPasswordField = new TextField();

            usernameField.setPromptText("Choisir un nom d'utilisateur");
            newPasswordField.setPromptText("Choisir un nouveau mot de passe (8-30)");
            newPasswordField.setPrefWidth(300);

            ChoiceBox<Role> roleChoiceBox = new ChoiceBox<>();

            roleChoiceBox.getItems().addAll(Role.values() );
            roleChoiceBox.setPrefWidth(100);
            roleChoiceBox.getSelectionModel().select(Role.getById(result.role) );

            Button updateButton = new Button("Mettre à jour");
            Button deleteButton = new Button("Supprimer l'utilisateur");

            HBox resultLine = new HBox(20,usernameField,newPasswordField,roleChoiceBox,updateButton,deleteButton);

            // suppression du compte
            deleteButton.setOnMouseClicked((e) -> {
                Alert confirmationAlert = UiAlert.newAlert(AlertType.CONFIRMATION,"Confirmation","Voulez vous supprimer le compte ?");

                confirmationAlert.showAndWait();

                if(confirmationAlert.getResult() == ButtonType.OK){
                    if(result.delete("id") ){
                        // mise en attente d'appui pour permettre la vision de l'animation de disparition
                        UiAlert.newAlert(
                            AlertType.INFORMATION,
                            "Compte supprimé",
                            String.join(" ",
                                "Le compte de ",
                                result.username,
                                "a bien été supprimé"
                            )
                        ).showAndWait();
    
                        Disappear.disappear(resultLine,() -> children.remove(resultLine),Duration.millis(400) );
                    }
                    else UiAlert.newAlert(AlertType.ERROR,"Erreur technique","Une erreur s'est produite lors de la suppression du compte");
                }
            });

            // mise à jour du compte
            updateButton.setOnMouseClicked((e) -> {
                String username = usernameField.getText();
                String password = newPasswordField.getText();

                int usernameLen = username.length();
                int passwordLen = password.length();

                // vérification de la validité du formulaire
                if(usernameLen <= 50 && usernameLen >= 1 && (passwordLen == 0 || (passwordLen <= 30 && passwordLen >= 8) ) ){
                    // vérification de non existance d'utilisateur
                    if(username.compareTo(result.username) != 0 && Model.find(UsersModel.class,new Condition[]{new Condition<String>("username",username,Separator.NULL)} ).size() != 0){
                        UiAlert.newAlert(AlertType.ERROR,"Mauvais nom d'utilisateur","Le nom d'utilisateur donné est déjà utilisé sur un compte !").show();
                        
                        return;
                    }
                    else{
                        int role = roleChoiceBox.getValue().roleId;

                        try{
                            String previousName =  result.username;

                            result.username = username;
                            result.role = role;

                            // si la taille du mot de passe n'est pas nulle alors nouveau mot de passe affecté
                            if(passwordLen != 0) result.setPassword(password); 

                            if(result.update("id") )
                                UiAlert.newAlert(AlertType.INFORMATION,"Compte mis à jour","Le compte de " + previousName + " a bien été mis à jour").show();
                            else 
                                throw new Exception();
                        }
                        catch(Exception exception){
                            UiAlert.newAlert(AlertType.ERROR,"Erreur technique","Une erreur s'est produite lors de la mise à jour du compte,veuillez retenter").show();
                        }
                    }   
                }   
                else UiAlert.newAlert(AlertType.ERROR,"Formulaire incorrect","Veuillez vérifier les données du formulaire").show();
            });

            children.add(resultLine);
        });
    }   
}
