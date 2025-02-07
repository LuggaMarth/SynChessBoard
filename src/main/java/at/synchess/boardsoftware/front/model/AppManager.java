package at.synchess.boardsoftware.front.model;

import at.synchess.boardsoftware.driver.SCDCommandLayer;
import at.synchess.boardsoftware.exceptions.AppManagerException;
import at.synchess.boardsoftware.exceptions.SynChessCoreException;
import at.synchess.boardsoftware.front.controller.*;
import javafx.stage.Stage;
import jssc.SerialPortException;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

/**
 * Class for managing the App and gluing all the scenes and
 * controllers together
 *
 * @author Kilian Nussbaumer
 */
public class AppManager {
    private final SCDCommandLayer driver;
    private final Stage primaryStage;
    private final ChessClient client;

    public AppManager(Stage primaryStage) throws SynChessCoreException {
        this.primaryStage = primaryStage;


        driver = new SCDCommandLayer();
        try {
            client = new ChessClient("LocalHost");
        } catch (IOException | MqttException e){
           throw new SynChessCoreException("Couldn't connect to client");
        }

        primaryStage.setOnCloseRequest(event ->{
            try {
                client.close();
            } catch (IOException | MqttException e) {
                ControllerUtils.showSafeAlert("Couldn't close properly");
            }
        });
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

    public void showGame(int gameId) throws AppManagerException, MqttException {
        try {
            GameController.show(getPrimaryStage(), this, gameId);
        } catch (IOException e) {
            throw new AppManagerException(GameController.class);
        }
    }


    public void showHostScreen() throws AppManagerException {
        try {
            HostController.show(getPrimaryStage(), this);
        } catch (IOException e) {
            throw new AppManagerException(HostController.class);
        }
    }

    /**
     * closeRoutine(): Gets called once the application is shut down
     */
    public void closeRoutine() {
        // close serial connection
        //try {
        //    //driver.close();
        //} catch (SerialPortException e) {
        //    ControllerUtils.showSafeAlert("Couldn't finish close routine!");
        //}
    }
}
