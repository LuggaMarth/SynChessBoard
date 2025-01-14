package at.synchess.boardsoftware.front.controller;

import at.synchess.boardsoftware.Main;
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
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

/**
 * Controller for the title screen
 *
 * @author Luca Marth
 */
public class TitleScreenController {
    private AppManager appManager;

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

    // ***** METHODS for actions
    @FXML
    public void OnActionTurnOffButton(ActionEvent event) {
        RaspiManager.shutdownRaspberry();
    }

    @FXML
    public void OnActionDeveloperButton(ActionEvent event) {
        try {
            appManager.showDeveloperScreen();
        } catch (IOException e) {
            System.err.println("Couldn't show developer screen");
            e.printStackTrace();
        }
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
        switchSelection(Selection.JOIN);
    }

    @FXML
    public void OnHost(ActionEvent event) {

    }

    @FXML
    private void OnEnterCodeJoin(ActionEvent e) {
        loadCodeMenu();
    }

    @FXML
    private void OnBrowseGames(ActionEvent e) {

    }

    @FXML
    private void OnBrowseReplays(ActionEvent e) {

    }

    @FXML
    private void OnEnterCodeHost(ActionEvent e) {

    }

    @FXML
    void onBackButtonPressed(ActionEvent event) {
        switchSelection(Selection.NONE);
    }


    // ***********

    /**
     * switchSelection(): switches the content and the onAction methods on the
     * buttons.
     * @param s selection to be switched to
     */
    public void switchSelection(Selection s) {
        firstButton.setVisible(true);
        secondButton.setVisible(true);
        backButton.setVisible(true);

        switch (s){
            case NONE:
                firstButton.setText("Play");
                secondButton.setText("Replay");
                backButton.setVisible(false);

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
                secondButton.setOnAction(this::OnBrowseReplays);
            case JOIN:
                firstButton.setText("Enter Code");
                secondButton.setText("Browse Games");

                firstButton.setOnAction(this::OnEnterCodeJoin);
                secondButton.setOnAction(this::OnBrowseGames);
                break;
        }
    }

    private void loadCodeMenu(){
        try {
            appManager.showCodeScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // pre-set ip label text on network address
        ipLbl.setText(NetworkManager.getIpV4AddressAsString(Main.INTERFACE_NAME));

        turnOffButton.setGraphic(GlobalController.getFontIcon("fas-power-off", 16, Color.WHITE));
        developerButton.setGraphic(GlobalController.getFontIcon("fas-code", 16, Color.WHITE));
        backButton.setGraphic(GlobalController.getFontIcon("fas-long-arrow-alt-left", 32, Color.WHITE));

        line.endXProperty().bind(lineLogoHolder.widthProperty().subtract(25));
    }
}