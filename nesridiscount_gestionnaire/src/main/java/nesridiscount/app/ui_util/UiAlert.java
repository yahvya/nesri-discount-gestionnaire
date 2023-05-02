package nesridiscount.app.ui_util;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nesridiscount.App;

/**
 * gestion des alert
 */
public class UiAlert {
    /**
     * crée un nouvel alert de l'application
     * @param type
     * @param title
     * @param content
     * @return l'alerte crée
     */
    public static Alert newAlert(AlertType type,String title,String content){
        Alert createdAlert = new Alert(type,content);
        
        createdAlert.setHeaderText(title);

        try{
            DialogPane dialogPane = createdAlert.getDialogPane();

            Stage alertStage = (Stage) dialogPane.getScene().getWindow();

            dialogPane.setMinHeight(Region.USE_PREF_SIZE);

            alertStage.initStyle(StageStyle.UNDECORATED);
            alertStage.getIcons().add(App.getAppIcon() );
            alertStage.getScene().getStylesheets().add(App.loadResource("/css-font/app.css").toString() );
        }
        catch(Exception e){}

        return createdAlert;
    }
}
