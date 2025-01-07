package at.synchess.boardsoftware.front.model;

import at.synchess.boardsoftware.core.driver.SCDCommandLayer;
import at.synchess.boardsoftware.front.controller.TitleScreenController;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class for managing the App and gluing all the scenes and
 * controllers together
 *
 * @author Luca Marth
 */
public class AppManager {
    private final SCDCommandLayer driver;
    private final Stage primaryStage;

    public AppManager(Stage primaryStage) {
        this.primaryStage = primaryStage;

        driver = new SCDCommandLayer();
    }

    // getter for primary stage
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * showTitleScreen(): Shows the main menu.
     */
    public void showTitleScreen() throws IOException {
        TitleScreenController.show(getPrimaryStage(), this);
    }

    

}
