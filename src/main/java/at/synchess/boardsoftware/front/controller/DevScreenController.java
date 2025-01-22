package at.synchess.boardsoftware.front.controller;

import at.synchess.boardsoftware.exceptions.AppManagerException;
import at.synchess.boardsoftware.front.model.AppManager;
import at.synchess.boardsoftware.front.model.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Opens Dev Menu, used for Testing and Debugging
 */
public class DevScreenController {
    private AppManager manager;
    private Stage primaryStage;

    @FXML private Button backButton;
    @FXML private ToggleGroup commandSelector;
    @FXML private ToggleGroup commandSelector1;
    @FXML private Label ipLbl;
    @FXML private Line line;
    @FXML private VBox lineLogoHolder;
    @FXML private ImageView logoImg;
    @FXML private RadioButton rbAvailable;
    @FXML private RadioButton rbHome;
    @FXML private RadioButton rbRead;
    @FXML private RadioButton rbReadBoard;
    @FXML private RadioButton rbStep;
    @FXML private Button saveButton;
    @FXML private TextField tfInterface;
    @FXML private TextField tfRead;
    @FXML private TextField tfStep;

    /**
     *  show(): Currently doesn't do shit
     * @param primaryStage
     * @param logic AppLogic that evoked this method
     * @throws IOException If FXML File couldn't be opened
     */
    public static void show(Stage primaryStage, AppManager logic) throws IOException {
        throw new IOException();
        /*
        // Load FXML

        FXMLLoader loader = new FXMLLoader(DevScreenController.class.getResource("/view/devView.fxml"));
        Parent root = loader.load();

        // get controller from fxml file and basic setup
        DevScreenController controller = loader.getController();
        controller.setManager(logic);
        controller.setPrimaryStage(primaryStage);

        primaryStage.getScene().setRoot(root);
        primaryStage.show();

         */
    }

    public void setManager(AppManager manager) {
        this.manager = manager;
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    @FXML
    void onActionBackButton(ActionEvent event) {
        try {
            manager.showTitleScreen();
        } catch (AppManagerException appManagerException) {
            ControllerUtils.showAppManagerAlert(appManagerException,primaryStage);
        }
    }

    @FXML
    void onActionSaveButton(ActionEvent event) {
        try(FileInputStream fis = new FileInputStream("/config/synchess.config")) {
            Properties p = new Properties();
            p.load(fis);

            p.setProperty("interface", tfInterface.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        backButton.setGraphic(ControllerUtils.getFontIcon("fas-long-arrow-alt-left", 32, Color.WHITE));
    }
}
