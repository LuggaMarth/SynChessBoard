package at.synchess.boardsoftware.front.controller;

import at.synchess.boardsoftware.Main;
import at.synchess.boardsoftware.utils.RaspiManager;
import at.synchess.boardsoftware.enums.Selection;
import at.synchess.boardsoftware.exceptions.AppManagerException;
import at.synchess.boardsoftware.front.model.AppManager;
import at.synchess.boardsoftware.utils.NetworkManager;
import at.synchess.boardsoftware.front.model.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Controller for the title screen
 *
 * @author Luca Marth
 */
public class TitleScreenController {
    private AppManager appManager;
    private Stage primaryStage;
    private final String[][] buttonConf = new String[][]{
            {"Play","Replay","OnPlay","OnReplay"}, //NONE
            {"Join","Host","OnJoin","OnHost"}, //PLAY
            {"Enter Code","Browse Replays","OnEnterCodeReplays","OnBrowseReplays"}, //REPLAY
            {"Enter Code","Browse Games","OnEnterCodeJoin","OnBrowseGames"}, //JOIN
            };

    @FXML private Label ipLbl;
    @FXML private Button turnOffButton;
    @FXML private Button developerButton;
    @FXML private Button firstButton;
    @FXML private Button secondButton;
    @FXML private Button backButton;
    @FXML private Line line;
    @FXML private VBox lineLogoHolder;

    /**
     * show(): Shows the current scene
     * @param primaryStage Stage to be shown on
     * @param logic main logic of the app
     * @throws IOException if file could not be found
     */
    public static void show(Stage primaryStage, AppManager logic) throws IOException {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(TitleScreenController.class.getResource("/view/titleScreen.fxml"));
        Parent root = loader.load();

        // get controller from fxml file and basic setup
        TitleScreenController controller = loader.getController();
        controller.setAppManager(logic);
        controller.setPrimaryStage(primaryStage);


        primaryStage.setTitle("SynChess - You Won't see this");
        primaryStage.setFullScreen(true);

        if (primaryStage.getScene() != null)
        primaryStage.getScene().setRoot(root);
        else primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    // setter
    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // ***** METHODS for actions
    @FXML public void OnActionTurnOffButton(ActionEvent event) {
        RaspiManager.shutdownRaspberry();
    }
    @FXML public void OnActionDeveloperButton(ActionEvent event) {
        try {
            appManager.showDeveloperScreen();
        } catch (AppManagerException appManagerException) {
            ControllerUtils.showAppManagerAlert(appManagerException,primaryStage);
        }
    }
    @FXML public void OnPlay(ActionEvent event) {
        switchSelection(Selection.PLAY);
    }
    @FXML public void OnReplay(ActionEvent event) {
        switchSelection(Selection.REPLAY);
    }
    @FXML public void OnJoin(ActionEvent event) {
        switchSelection(Selection.JOIN);
    }
    @FXML public void OnHost(ActionEvent event) {loadHostMenu();}
    @FXML public void OnEnterCodeJoin(ActionEvent e) {
        loadCodeMenu();
    }
    @FXML public void OnBrowseGames(ActionEvent e) {
        try {
            appManager.showGameList();
            } catch (AppManagerException appManagerException) {
            ControllerUtils.showAppManagerAlert(appManagerException,primaryStage);
            }

    }
    @FXML public void OnBrowseReplays(ActionEvent e) {

    }
    @FXML public void OnEnterCodeHost(ActionEvent e) {

    }

    @FXML
    void onBackButtonPressed(ActionEvent event) {
        switchSelection(Selection.NONE);
    }


    // ***********

    /**
     * switchSelection(): switches the content and the onAction methods on the
     * buttons. Uses the table buttonConfig
     * @param s selection to be switched to
     */
    public void switchSelection(Selection s) {
        firstButton.setVisible(true);
        secondButton.setVisible(true);
        backButton.setVisible(s != Selection.NONE);

        int index = s.ordinal();
        firstButton.setText(buttonConf[index][0]);
        secondButton.setText(buttonConf[index][1]);




        firstButton.setOnAction(event -> {
                        try {
                        (this.getClass().getMethod(buttonConf[index][2], new Class[]{ActionEvent.class})).invoke(this, new ActionEvent());
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            //Technically can't occur I think?
                            e.printStackTrace();
                            ControllerUtils.showServerAlert("Code's haunted", primaryStage);
                        }
                    });
        secondButton.setOnAction(event -> {
            try {
                (this.getClass().getMethod(buttonConf[index][3],new Class[]{ActionEvent.class})).invoke(this, new ActionEvent());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
                //Technically can't occur I think?
                e.printStackTrace();
                ControllerUtils.showServerAlert("Code's haunted", primaryStage);
            }
        });


    }

    private void loadCodeMenu(){
        try {
            appManager.showCodeScreen();
        } catch (AppManagerException appManagerException) {
            ControllerUtils.showAppManagerAlert(appManagerException,primaryStage);
        }
    }

    private void loadHostMenu(){
        try {
            appManager.showHostScreen();
        } catch (AppManagerException appManagerException) {
            ControllerUtils.showAppManagerAlert(appManagerException,primaryStage);
        }
    }

    @FXML
    public void initialize() {
        // pre-set ip label text on network address
        ipLbl.setText(NetworkManager.getIpV4AddressAsString(Main.INTERFACE_NAME));

        turnOffButton.setGraphic(ControllerUtils.getFontIcon("fas-power-off", 16, Color.WHITE));
        developerButton.setGraphic(ControllerUtils.getFontIcon("fas-code", 16, Color.WHITE));
        backButton.setGraphic(ControllerUtils.getFontIcon("fas-long-arrow-alt-left", 32, Color.WHITE));

        line.endXProperty().bind(lineLogoHolder.widthProperty().subtract(25));
    }
}