package at.synchess.boardsoftware.front.controller;

import at.synchess.boardsoftware.Main;
import at.synchess.boardsoftware.core.utils.NetworkManager;
import at.synchess.boardsoftware.core.utils.RaspiManager;
import at.synchess.boardsoftware.exceptions.AppManagerException;
import at.synchess.boardsoftware.front.model.AppManager;
import at.synchess.boardsoftware.front.model.ControllerUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

/** CodeScreenView: Players can put in a Gamecode to join a game (or replay one)
 *
 */
public class CodeScreenController {
    private AppManager appManager;
    private Stage primaryStage;

    @FXML
    private Label ipLbl;
    @FXML
    private Button developerButton;
    @FXML
    private Button turnOffButton;
    @FXML
    private Button backButton;
    @FXML
    private VBox lineLogoHolder;
    @FXML
    private ImageView logoImg;
    @FXML
    private Line line;
    @FXML
    private Label code;

    /**
     *  show(): Sets root of scene to the CodeScreenView
     * @param primaryStage
     * @param logic AppLogic that evoked this method
     * @throws IOException If FXML File couldn't be opened
     */

    public static void show(Stage primaryStage, AppManager logic) throws IOException {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(CodeScreenController.class.getResource("/view/codeScreen.fxml"));
        Parent root = loader.load();

        // get controller from fxml file and basic setup
        CodeScreenController controller = loader.getController();
        controller.setAppManager(logic);
        controller.setPrimaryStage(primaryStage);

        primaryStage.getScene().setRoot(root);
        Platform.runLater(() -> primaryStage.setFullScreen(true));
    }

    // setters
    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    //OnClick Methods
    @FXML
    public void OnActionTurnOffButton(ActionEvent event) {
        RaspiManager.shutdownRaspberry();
    }

    @FXML
    public void OnActionDeveloperButton(ActionEvent event) {
        try {
            appManager.showDeveloperScreen();
        } catch (AppManagerException e) {
            ControllerUtils.showAppManagerAlert(e,primaryStage);
        }
    }

    @FXML
    void onBackButtonPressed(ActionEvent event) throws IOException {
        try {
            appManager.showTitleScreen();
        } catch (AppManagerException e) {
            ControllerUtils.showAppManagerAlert(e,primaryStage);
        }
    }

    @FXML
    void numClick(ActionEvent event) {
        Button b = (Button) event.getSource();
        enterNumber(Integer.parseInt(b.getText()));
    }

    private void enterNumber(int i) {
        if (code.getText().length() < 8)
            code.setText(code.getText() + i);
    }

    @FXML
    void deleteNum(ActionEvent event) {
        if (!code.getText().isEmpty())
            code.setText(code.getText().substring(0, code.getText().length() - 1));
    }

    @FXML
    void sendCode(ActionEvent event) {
        try {
            int NumCode = Integer.parseInt(code.getText());
            if (appManager.getClient().joinGame(NumCode) != 0){
                appManager.showGame(NumCode);
            }
            else ControllerUtils.showWarning("Game not found", primaryStage);
        } catch (IOException | MqttException e) {
         ControllerUtils.showServerAlert("Couldn't connect properly", primaryStage);
        } catch (AppManagerException ae){
            ControllerUtils.showAppManagerAlert(ae, primaryStage);
        }

    }

    @FXML
    public void initialize() {
        // pre-set ip label text on network address
        ipLbl.setText((NetworkManager.getIpV4AddressAsString(Main.INTERFACE_NAME).isEmpty()) ? "127.0.0.1" : NetworkManager.getIpV4AddressAsString(Main.INTERFACE_NAME));

        turnOffButton.setGraphic(ControllerUtils.getFontIcon("fas-power-off", 16, Color.WHITE));
        developerButton.setGraphic(ControllerUtils.getFontIcon("fas-code", 16, Color.WHITE));
        backButton.setGraphic(ControllerUtils.getFontIcon("fas-long-arrow-alt-left", 32, Color.WHITE));

        line.endXProperty().bind(lineLogoHolder.widthProperty().subtract(25));
    }
}
