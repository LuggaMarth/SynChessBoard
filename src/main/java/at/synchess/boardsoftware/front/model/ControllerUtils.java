package at.synchess.boardsoftware.front.model;

import at.synchess.boardsoftware.exceptions.AppManagerException;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class ControllerUtils {
    public static FontIcon getFontIcon(String name, int size, Color fill) {
        FontIcon ret = new FontIcon(name);
        ret.setIconSize(size);
        ret.setFill(fill);

        return ret;
    }

    public static void showServerAlert(String message, Stage primStage){
        Alert a = new Alert(AlertType.ERROR);
        a.setContentText("Server Error: " + message);
        a.initOwner(primStage);
        a.show();
    }

    public static void showAppManagerAlert(AppManagerException ae, Stage primStage){
        Alert a = new Alert(AlertType.ERROR);
        a.setContentText("Failed building View: " + ae.getController().getSimpleName());
        a.initOwner(primStage);
        a.show();
    }
    public static void showSafeAlert(String message){
        Alert a = new Alert(AlertType.ERROR);
        a.setContentText("Error: " + message);

        a.show();
    }

    public static void showWarning(String message, Stage primStage){
        Alert a = new Alert(AlertType.WARNING);
        a.setContentText(message);
        a.initOwner(primStage);
        a.show();
    }

}
