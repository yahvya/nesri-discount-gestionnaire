package nesridiscount.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebView;
import nesridiscount.app.ui_util.UiAlert;
import nesridiscount.models.model.HelpCategoriesModel;
import nesridiscount.models.model.Model;
import nesridiscount.models.util.Condition;

public class HelpController {

    @FXML
    private ChoiceBox<String> subjectChooser;

    private HashMap<String,String> helpContents;

    @FXML
    private WebView sectionViewer;

    @FXML
    void initialize(){
        ArrayList<? extends Model> helpCategoriesResult = Model.find(HelpCategoriesModel.class,new Condition[]{});

        this.helpContents = new HashMap<>();

        ObservableList<String> subjectList = this.subjectChooser.getItems();

        // récupération des categories
        for(Model resultModel : helpCategoriesResult){
            HelpCategoriesModel helpModel = (HelpCategoriesModel) resultModel;

            this.helpContents.put(helpModel.categoryName,helpModel.helpContent);
            subjectList.add(helpModel.categoryName);
        } 

        if(this.helpContents.size() == 0){
            UiAlert.newAlert(AlertType.INFORMATION,"Aucune aide","Il n'y a aucune aide à afficher actuellement").show();

            return;
        }

        this.subjectChooser.setOnAction((e) -> this.showHelpFrom(this.subjectChooser.getValue() ) );
        this.subjectChooser.getSelectionModel().select(0);
    }

    /**
     * affiche l'aide de la section choisie
     * @param sectionToShow
     */
    private void showHelpFrom(String sectionToShow){
        String helpContent = this.helpContents.get(sectionToShow);

        if(helpContent == null){
            UiAlert.newAlert(AlertType.ERROR,"Non trouvé","Aide non trouvé pour cette catégorie").show();

            return;
        }

        this.sectionViewer.getEngine().loadContent(helpContent);
    }
}
