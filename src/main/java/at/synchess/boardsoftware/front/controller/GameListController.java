package at.synchess.boardsoftware.front.controller;

import at.synchess.boardsoftware.core.utils.RaspiManager;
import at.synchess.boardsoftware.exceptions.AppManagerException;
import at.synchess.boardsoftware.front.model.AppManager;
import at.synchess.boardsoftware.front.model.ChessClient;
import at.synchess.boardsoftware.front.model.ControllerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class GameListController {
    private AppManager appManager;
    private ChessClient client;
    private Stage primaryStage;
    private ObservableList<String> games;

    @FXML private Label ipLbl;
    @FXML private Button developerButton;
    @FXML private Button turnOffButton;
    @FXML private TilePane tilePane;
    @FXML private Button backButton;
    @FXML private VBox lineLogoHolder;
    @FXML private ImageView logoImg;
    @FXML private Line line;

    public static void show(Stage primaryStage, AppManager logic, ChessClient client) throws IOException {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(CodeScreenController.class.getResource("/view/gameList.fxml"));
        Parent root = loader.load();

        // get controller from fxml file and basic setup
        GameListController controller = loader.getController();
        controller.setAppManager(logic);
        controller.setClient(client);
        controller.setPrimaryStage(primaryStage);
        controller.reloadList();


        primaryStage.getScene().setRoot(root);
        primaryStage.setFullScreen(true);
    }

    // setter
    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }

    public void setClient(ChessClient chessClient) {
        this.client = chessClient;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // ***** METHODS for actions

    public void reloadList(){
        if (games == null) {
            games = FXCollections.observableArrayList();
        }

        try {
            games.setAll(client.getGameList(true));
        } catch (IOException e) {
            ControllerUtils.showServerAlert("Couldn't request current Games", primaryStage);
        }


            for (String game:
                 games) {
                System.out.println(game);
                Label l = new Label(game);
                l.getStyleClass().addAll("btn-secondary","btn","btn-menu","tile");
                l.setAlignment(Pos.CENTER);

                l.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1) { // Check for double-click
                        try{
                                appManager.showGame(Integer.parseInt(l.getText()));
                            } catch (AppManagerException e) {
                                ControllerUtils.showAppManagerAlert(e,primaryStage);
                            } catch (MqttException e){
                            ControllerUtils.showServerAlert("Couldn't subscribe to topic", primaryStage);
                        }
                        }


                });

                tilePane.getChildren().add(l);
            }


    }

    public void joinGame(int gameId){
        try {
            client.joinGame(gameId);
        } catch (IOException e){
            ControllerUtils.showServerAlert("Joining game went wrong", primaryStage);
        }


    }

    @FXML
    public void OnActionTurnOffButton(ActionEvent event) {
        RaspiManager.shutdownRaspberry();
    }

    @FXML
    public void OnActionDeveloperButton(ActionEvent event) {
        try {
            appManager.showDeveloperScreen();
        } catch (AppManagerException appManagerException) {
            ControllerUtils.showAppManagerAlert(appManagerException,primaryStage);
        }
    }

    @FXML
    void onBackButtonPressed(ActionEvent event) throws IOException {
        try{
        appManager.showTitleScreen();
        } catch (AppManagerException appManagerException) {
            ControllerUtils.showAppManagerAlert(appManagerException,primaryStage);
        }
    }

    public void initialize() {
        turnOffButton.setGraphic(ControllerUtils.getFontIcon("fas-power-off", 16, Color.WHITE));
        developerButton.setGraphic(ControllerUtils.getFontIcon("fas-code", 16, Color.WHITE));
        backButton.setGraphic(ControllerUtils.getFontIcon("fas-long-arrow-alt-left", 32, Color.WHITE));
    }
}
