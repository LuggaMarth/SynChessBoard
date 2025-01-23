package at.synchess.boardsoftware;

import at.synchess.boardsoftware.exceptions.SynChessCoreException;
import at.synchess.boardsoftware.front.model.AppManager;
import at.synchess.boardsoftware.front.model.ControllerUtils;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main: Handles the start of the JavaFX - Application.
 * @author Kilian Nussbaumer & Luca Marth
 */
public class Main extends Application {
    public static String INTERFACE_NAME = "eth0";
    public AppManager appManager;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            appManager = new AppManager(primaryStage);
            appManager.showTitleScreen();
        } catch (SynChessCoreException s){
            ControllerUtils.showSafeAlert(s.getMessage());
        }
    }

    @Override
    public void stop() {
        appManager.closeRoutine();
    }
}