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

    private Label activeLabel;

    private static Alert sectionLoadingFailAlert = UiAlert.newAlert(AlertType.ERROR,"Erreur de chargement","Une erreur s'est produite lors du chargement de la section");

    @FXML
    void switchToCreateAccount(MouseEvent event) {
        this.switchToSection(ParameterSection.CREATE_ACCOUNT,"parameter-create-account-section",this.createAccountLabel);
    }

    @FXML
    void switchToManageAccount(MouseEvent event) {
        // this.switchToSection(ParameterSection.MANAGE_ACCOUNT,"");
    }

    @FXML
    void initialize(){
        this.currentSectionContent = null;
        this.activeLabel = this.createAccountLabel;

        this.switchToCreateAccount(null);
    }

    /**
     * supprime la section actuellement affiché et affiche la nouvelle section
     * @param section
     * @param fxml
     * @param linkedLabel
     */
    private void switchToSection(ParameterSection section,String fxml,Label linkedLabel){
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

        this.activeLabel.getStyleClass().remove("active");
        this.activeLabel = linkedLabel;
        this.activeLabel.getStyleClass().add("active");
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
