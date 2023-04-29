package nesridiscount.controllers;

import nesridiscount.App;

/**
 * parent des controllers
 */
public abstract class Controller {
    /**
     * affiche la page d'accueil
     */
    public void switchToHomePage(){
        App.switchToScene("home");
    }
}
