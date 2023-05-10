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

    protected static ScrollPane pane;

    private static HashMap<String,Parent> pages = new HashMap<>();

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
    
    /**
     * modifie le pane
     * @param pane
     */
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

                    // ajout des css
                    ObservableList<String> stylesheets = Controller.pane.getStylesheets();

                    ArrayList<String> toRemove = new ArrayList<>(stylesheets);

                    // suppression des css précédemment ajouté
                    toRemove.forEach(stylesheet -> stylesheets.remove(stylesheet) );

                    // ajout des nouveaux css
                    loadedSection.getStylesheets().forEach(stylesheet -> stylesheets.add(stylesheet) );

                    Controller.currentSection = fxml;
                }   
                else{
                    App.getStage().getScene().setRoot(loadedSection);

                    Controller.currentSection = null;
                }
            }
            catch(Exception e){}
        }   
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
