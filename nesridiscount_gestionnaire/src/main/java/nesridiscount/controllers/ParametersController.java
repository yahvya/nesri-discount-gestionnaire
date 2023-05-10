package nesridiscount.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import nesridiscount.App;
import nesridiscount.app.ui_util.UiAlert;

public class ParametersController {
    private ParameterSection currentSection = ParameterSection.NULL;

    @FXML
    private VBox pageContainer;

    @FXML
    private Label createAccountLabel;
    
    @FXML
    private Label manageAccountLabel;

    private VBox currentSectionContent;

    private static Alert sectionLoadingFailAlert = UiAlert.newAlert(AlertType.ERROR,"Erreur de chargement","Une erreur s'est produite lors du chargement de la section");

    @FXML
    void switchToCreateAccount(MouseEvent event) {
        this.switchToSection(ParameterSection.CREATE_ACCOUNT,"parameter-create-account-section");
    }

    @FXML
    void switchToManageAccount(MouseEvent event) {
        // this.switchToSection(ParameterSection.MANAGE_ACCOUNT,"");
    }

    @FXML
    void initialize(){
        this.currentSectionContent = null;

        this.switchToCreateAccount(null);
    }

    /**
     * supprime la section actuellement affiché et affiche la nouvelle section
     * @param section
     * @param fxml
     */
    private void switchToSection(ParameterSection section,String fxml){
        if(section == this.currentSection) return;

        Parent loadedSectionParent = App.loadFXML(fxml);

        if(loadedSectionParent == null){
            ParametersController.sectionLoadingFailAlert.show();

            return;
        }

        ObservableList<Node> children = this.pageContainer.getChildren();

        if(this.currentSectionContent != null) children.remove(this.currentSectionContent);

        VBox loadedSection = (VBox) loadedSectionParent;

        this.currentSectionContent = loadedSection;
        
        children.add(loadedSection);
    }

    /**
     * représente les sections
     */
    enum ParameterSection{
        CREATE_ACCOUNT,
        MANAGE_ACCOUNT,
        NULL
    };
}
