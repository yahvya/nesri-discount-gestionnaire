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

    private static String currentSection = null;

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
        if(fxml == Controller.currentSection) return;

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

                Controller.currentSection = fxml;
            }
            catch(Exception e){}
        }   

        try{
            ObservableList<String> sceneStylesheets = App.getStage().getScene().getStylesheets();
            
            // ajout des nouveaux fichiers style
            loadedSection.getStylesheets().forEach(stylesheet -> {
                if(!Controller.newAddedStylesheets.contains(stylesheet) ){
                    sceneStylesheets.add(stylesheet);
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
    
    /**
     * 
     * @return le parent app
     */
    public static ScrollPane getPane(){
        return Controller.pane;
    }
}
