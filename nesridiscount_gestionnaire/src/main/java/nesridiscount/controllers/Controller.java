package nesridiscount.controllers;

import nesridiscount.App;

/**
 * parent des controllers
 */
public abstract class Controller {
    /**
     * affiche la page d'accueil
     */
    public static void switchToAppPage(){
        App.switchToScene("app");
    }
}
