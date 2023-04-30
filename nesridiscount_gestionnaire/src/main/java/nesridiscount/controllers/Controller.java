package nesridiscount.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import nesridiscount.App;

/**
 * parent des controllers
 */
public abstract class Controller {
    private static BorderPane pane;

    /**
     * affiche la page d'accueil
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
    
    public static void setPane(BorderPane pane){
        Controller.pane = pane;
    }
}
