package nesridiscount;

import java.net.URL;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import nesridiscount.app.session.Session;
import nesridiscount.app.util.Action;
import nesridiscount.controllers.Controller;

/**
 * gestionnaire de pièces détachés nesri discount
 */
public class App extends Application {
    private static Class<?> classLoader;

    private static Scene scene;

    private static Stage stage;
    
    private static HashMap<String,Parent> savedPages;

    private static HashMap<Object,Action> toStopOnClose = new HashMap<>();

    private static Image appIcon;

    public static void main(String[] args){
        App.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        App.classLoader = this.getClass();
        App.savedPages = new HashMap<>();
        App.setStage(primaryStage);
        App.appIcon = new Image(App.loadResource("/icons/favicon.png").toString() );

        Parent parent = App.loadFXML("login");

        // chargement de la page d'accueil
        App.scene = new Scene(parent);

        // configuration de la fenêtre
        App.setWindowConfig();

        if(Session.loadSession() )  Controller.switchToAppPage(true);
    }   

    /**
     * configure la fenêtre ajouté
     */
    public static void setWindowConfig(){
        App.stage.setTitle("Nesri discount - Gestionnaire");
        App.stage.getIcons().add(App.appIcon);
        App.stage.setScene(App.scene);
        App.stage.show();
    }

    /**
     * charge un fichier fxml
     * @param fxmlFilename nom du fichier fxml
     * @return
     */
    public static Parent loadFXML(String fxmlFilename){
        try{
            FXMLLoader loader = new FXMLLoader(App.loadResource("/fxml/" + fxmlFilename + ".fxml") );

            return loader.load();
        }
        catch(Exception e){
            return null;
        }
    }
    
    /**
     * @param resourcePath chemin de la ressource
     * @return l'url de la resource ou null
     */
    public static URL loadResource(String resourcePath){
        try{
            return App.classLoader.getResource(resourcePath);
        }
        catch(Exception e){
            return null;
        }
    }

    /**
     * affiche la page donnée
     * @param fxmlFilename
     * @param saveScene défini si la scène doit être re utilisé
     * @return Parent le parent sélectionné
     */
    public static Parent switchToScene(String fxmlFilename,boolean saveScene){
        Parent fxml = App.savedPages.get(fxmlFilename);

        if(fxml == null) fxml = App.loadFXML(fxmlFilename);

        if(saveScene) App.savedPages.put(fxmlFilename,fxml);

        if(fxml != null) App.scene.setRoot(fxml);

        return fxml;
    }

    /**
     * @alias false à switchToScene
     */
    public static Parent switchToScene(String fxmlFilename){
        return App.switchToScene(fxmlFilename,false);
    }

    /**
     * enregistre une action a faire à la fermeture
     * @param on
     * @param toDo
     * @return l'action passé
     */
    public static Action registerAction(Object on,Action toDo){
        App.toStopOnClose.put(on,toDo);
        
        return toDo;
    }  
    
    /**
     * supprime une action a faire à la fermeture
     * @param on
     */
    public static void unregisterAction(Object on){
        App.toStopOnClose.remove(on);
    }  

    /**
     * affecte le stage
     * @param stage
     */
    public static void setStage(Stage stage){
        App.stage = stage;

        // exécution des actions à faire à la fermeture
        stage.setOnCloseRequest((e) -> {
            App.toStopOnClose.forEach((on,action) -> {
                try{
                    action.doAction();
                }
                catch(Exception exception){}
            } );
        });
    }

    /**
     * 
     * @return la fenêtre actuelle de l'application
     */
    public static Stage getStage(){
        return App.stage;
    }

    /**
     * 
     * @return l'icone de l'application
     */
    public static Image getAppIcon(){
        return App.appIcon;
    }
}