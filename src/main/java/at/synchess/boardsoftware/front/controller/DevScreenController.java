package at.synchess.boardsoftware.front.controller;

import at.synchess.boardsoftware.front.model.AppManager;
import at.synchess.boardsoftware.front.model.ControllerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DevScreenController {
    private AppManager manager;

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
     * show(): Shows the current scene
     * @param primaryStage Stage to be shown on
     * @param logic main logic of the app
     * @throws IOException if file could not be found
     */
    public static void show(Stage primaryStage, AppManager logic) throws IOException {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(DevScreenController.class.getResource("/view/devView.fxml"));
        Parent root = loader.load();

        // get controller from fxml file and basic setup
        DevScreenController controller = loader.getController();
        controller.setManager(logic);

        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    public void setManager(AppManager manager) {
        this.manager = manager;
    }

    @FXML
    void onActionBackButton(ActionEvent event) {
        try {
            manager.showTitleScreen();
        } catch (IOException e) {
            e.printStackTrace();
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
