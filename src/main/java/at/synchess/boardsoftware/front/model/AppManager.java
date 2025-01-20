package at.synchess.boardsoftware.front.model;

import at.synchess.boardsoftware.core.driver.SCDCommandLayer;
import at.synchess.boardsoftware.front.controller.CodeScreenController;
import at.synchess.boardsoftware.front.controller.GameController;
import at.synchess.boardsoftware.front.controller.GameListController;
import at.synchess.boardsoftware.front.controller.TitleScreenController;
import javafx.scene.Scene;
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
    private ChessClient client;

    public AppManager(Stage primaryStage) {
        this.primaryStage = primaryStage;


        driver = new SCDCommandLayer();
        client = new ChessClient("LocalHost");
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

    /**
     * showDeveloperScreen(): Shows the developer Screen
     * @throws IOException
     */
    public void showDeveloperScreen() throws IOException {

    }

    public void showCodeScreen()throws IOException {
        CodeScreenController.show(getPrimaryStage(), this);
    }

    public ChessClient getClient(){
        return client;
    }

    public void showGameList()throws IOException {
        GameListController.show(getPrimaryStage(), this, client);
    }

    public void showGame(int gameId) throws IOException {
        GameController.show(getPrimaryStage(),this, gameId);
    }
}
