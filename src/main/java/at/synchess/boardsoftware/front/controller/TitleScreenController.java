package at.synchess.boardsoftware.front.controller;

import at.synchess.boardsoftware.core.utils.RaspiManager;
import at.synchess.boardsoftware.enums.Selection;
import at.synchess.boardsoftware.front.model.AppManager;
import at.synchess.boardsoftware.core.utils.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the title screen
 *
 * @author Luca Marth
 */
public class TitleScreenController {
    private static final String INTERFACE_NAME = "enp0s3";

    private AppManager appManager;

    @FXML private Label ipLbl;
    @FXML private Button turnOffButton;
    @FXML private Button developerButton;
    @FXML private Button firstButton;
    @FXML private Button secondButton;

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

        // init scene and stage
        Scene s = new Scene(root);

        primaryStage.setTitle("SynChess - You Won't see this");
        //primaryStage.setFullScreen(true);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    // setter
    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }

    // ***** METHODS for actions
    @FXML
    public void OnActionTurnOffButton(ActionEvent event) {
        RaspiManager.shutdownRaspberry();
    }

    @FXML
    public void OnActionDeveloperButton(ActionEvent event) {

    }

    @FXML
    public void OnPlay(ActionEvent event) {
        switchSelection(Selection.PLAY);
    }

    @FXML
    public void OnReplay(ActionEvent event) {
        switchSelection(Selection.REPLAY);
    }

    @FXML
    public void OnJoin(ActionEvent event) {

    }

    @FXML
    public void OnHost(ActionEvent event) {

    }

    @FXML
    private void OnEnterCodeJoin(ActionEvent e) {

    }

    @FXML
    private void OnBrowseGames(ActionEvent e) {

    }

    @FXML
    private void OnEnterCodeHost(ActionEvent e) {

    }


    // ***********

    /**
     * switchSelection(): switches the content and the onAction methods on the
     * buttons.
     * @param s selection to be switched to
     */
    public void switchSelection(Selection s) {
        firstButton.setVisible(true);
        secondButton.setVisible(false);

        switch (s){
            case NONE:
                firstButton.setText("Play");
                secondButton.setText("Replay");

                firstButton.setOnAction(this::OnPlay);
                secondButton.setOnAction(this::OnReplay);
                break;
            case PLAY:
                firstButton.setText("Join");
                secondButton.setText("Host");

                firstButton.setOnAction(this::OnJoin);
                secondButton.setOnAction(this::OnHost);
                break;
            case REPLAY:
                firstButton.setText("Enter Code");
                secondButton.setText("Browse Games");

                firstButton.setOnAction(this::OnEnterCodeHost);
                secondButton.setOnAction(this::OnBrowseGames);
            case JOIN:
                firstButton.setText("Enter Code");
                secondButton.setVisible(false);

                firstButton.setOnAction(this::OnEnterCodeJoin);
                break;
        }
    }

    @FXML
    public void initialize() {
        // pre-set ip label text on network address
        ipLbl.setText((NetworkManager.getIpV4AddressAsString(INTERFACE_NAME).isEmpty()) ? "127.0.0.1" : NetworkManager.getIpV4AddressAsString(INTERFACE_NAME));

        // set button icons
//        FontAwesomeIconView turnoff = new FontAwesomeIconView(FontAwesomeIcon.POWER_OFF);
//        FontAwesomeIconView dev = new FontAwesomeIconView(FontAwesomeIcon.CODE);
//
//        turnoff.setSize("16px");
//        turnoff.setFill(Color.WHITE);
//        dev.setSize("16px");
//        dev.setFill(Color.WHITE);
//
//        turnOffButton.setGraphic(turnoff);
//        developerButton.setGraphic(dev);
    }
}