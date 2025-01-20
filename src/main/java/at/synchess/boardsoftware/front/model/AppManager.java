package at.synchess.boardsoftware.front.model;

import at.synchess.boardsoftware.core.driver.SCDCommandLayer;
import at.synchess.boardsoftware.exceptions.AppManagerException;
import at.synchess.boardsoftware.front.controller.*;
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
    public void showTitleScreen() throws AppManagerException {
        try {
            TitleScreenController.show(getPrimaryStage(), this);
        } catch (IOException e) {
            throw new AppManagerException(TitleScreenController.class);
        }
    }

    /**
     * showDeveloperScreen(): Shows the developer Screen
     *
     * @throws IOException
     */
    public void showDeveloperScreen() throws AppManagerException {
        try {
            DevScreenController.show(getPrimaryStage(), this);
        } catch (IOException e) {
            throw new AppManagerException(DevScreenController.class);
        }
    }

    public void showCodeScreen() throws AppManagerException {
        try {
            CodeScreenController.show(getPrimaryStage(), this);
        } catch (IOException e) {
            throw new AppManagerException(CodeScreenController.class);
        }
    }

    public ChessClient getClient() {
        return client;
    }

    public void showGameList() throws AppManagerException {
        try {
            GameListController.show(getPrimaryStage(), this, client);
        } catch (IOException e) {
            throw new AppManagerException(GameListController.class);
        }

    }

    public void showGame(int gameId) throws AppManagerException {
        try {
            GameController.show(getPrimaryStage(), this, gameId);
        } catch (IOException e) {
            throw new AppManagerException(GameController.class);
        }
    }
}
