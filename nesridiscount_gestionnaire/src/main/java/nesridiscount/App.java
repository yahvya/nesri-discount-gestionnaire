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

/**
 * gestionnaire de pièces détachés nesri discount
 */
public class App extends Application {
    private static Class<?> classLoader;

    private static Scene scene;

    private static Stage stage;
    
    private static HashMap<String,Parent> savedPages;

    public static void main(String[] args){
        App.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        App.classLoader = this.getClass();
        App.stage = primaryStage;
        App.savedPages = new HashMap<>();

        // chargement de la page d'accueil
        App.scene = new Scene(App.loadFXML(!Session.loadSession() ? "login" : "app") );

        // configuration de la fenêtre
        primaryStage.setTitle("Nesri discount - Gestionnaire");
        primaryStage.getIcons().add(new Image(App.loadResource("/icons/favicon.png").toString() ) );
        primaryStage.setScene(App.scene);
        primaryStage.show();
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
     */
    public static void switchToScene(String fxmlFilename,boolean saveScene){
        Parent fxml = App.savedPages.get(fxmlFilename);

        if(fxml == null) fxml = App.loadFXML(fxmlFilename);

        if(saveScene) App.savedPages.put(fxmlFilename,fxml);

        if(fxml != null) App.scene.setRoot(fxml);
    }

    /**
     * @alias false à switchToScene
     */
    public static void switchToScene(String fxmlFilename){
        App.switchToScene(fxmlFilename,false);
    }

    public static Stage getStage(){
        return App.stage;
    }
}