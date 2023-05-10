package nesridiscount.app.util;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * animation de disparition
 */
public class Disappear {
    /**
     * fais disparaitre un élement
     * @param toHide
     * @param toDoAfter action à exécuter après la disparition
     * @param duration durée d'animation
     */
    public static void disappear(Node toHide,Action toDoAfter,Duration duration){
        FadeTransition animation = new FadeTransition();

        animation.setNode(toHide);
        animation.setDuration(duration);
        animation.setFromValue(1.0);
        animation.setToValue(0.0);
        animation.setCycleCount(1);
        animation.setOnFinished((e) -> {
            if(toDoAfter != null){
                try{
                    toDoAfter.doAction();
                }
                catch(Exception exception){}
            }
        });

        animation.play();
    }
}
