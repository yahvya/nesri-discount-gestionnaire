package nesridiscount.controllers;

import javafx.stage.Stage;
import nesridiscount.App;

/**
 * parent des controllers
 */
public abstract class Controller {
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
}
