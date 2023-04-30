package nesridiscount.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import nesridiscount.App;

/**
 * parent des controllers
 */
public abstract class Controller {
    @FXML
    protected BorderPane parent;

    private static ScrollPane pane;

    private static HashMap<String,Parent> pages = new HashMap<>();

    private static ArrayList<String> newAddedStylesheets = new ArrayList<>();

    /**
     * affiche le template accueil
     */
    public static void switchToAppPage(boolean resetPage){
        Stage window = App.getStage();

        try{
            window.close();

            window = new Stage();

            App.setStage(window);
            App.setWindowConfig();

            window.setMaximized(true);
        }
        catch(Exception e){}

        window.setMaximized(true);
        window.hide();

        App.switchToScene("app");

        window.show();
    }
    
    public static void setPane(ScrollPane pane){
        Controller.pane = pane;
    }

    /** 
     * affiche la section de page dans la page application
     * @param fxml le nom du fichier controller (format : parent borderbane)
     */
    public static void switchToPage(String fxml,boolean saveSection){
        Parent loadedSection = Controller.pages.get(fxml);
        
        if(loadedSection == null) loadedSection = App.loadFXML(fxml);
    
        if(loadedSection != null){
            // sauvegarde de la section
            if(saveSection) Controller.pages.put(fxml,loadedSection);

            try{
                // vérifications du format de page
                if(loadedSection instanceof BorderPane){
                    // récupération du center du template
                    Node loadedSectionCenter = ((BorderPane) loadedSection).getCenter();

                    if(loadedSectionCenter instanceof ScrollPane){
                        Node loadedSectionContent = ((ScrollPane) loadedSectionCenter).getContent();

                        Controller.pane.setContent(loadedSectionContent);
                    }
                }   
                else App.getStage().getScene().setRoot(loadedSection);
            }
            catch(Exception e){}
        }   

        try{
            ObservableList<String> currentStyleSheets = Controller.pane.getStylesheets();

            // suppressions des css ajoutés
            Controller.newAddedStylesheets.forEach(stylesheet -> {
                currentStyleSheets.remove(stylesheet); 
                Controller.newAddedStylesheets.remove(stylesheet);
            });
            
            // ajout des nouveaux fichiers style
            loadedSection.getStylesheets().forEach(stylesheet -> {
                if(!currentStyleSheets.contains(stylesheet) ){
                    currentStyleSheets.add(stylesheet);
                    Controller.newAddedStylesheets.add(stylesheet);
                }
            });
        }
        catch(Exception e){}
    }   

    /**
     * @alias false à switchToPage
     */
    public static void switchToPage(String fxml){
        Controller.switchToPage(fxml,false);
    }   
}
