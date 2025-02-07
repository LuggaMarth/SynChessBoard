package at.synchess.boardsoftware.front.controller;

import at.synchess.boardsoftware.front.model.AppManager;
import at.synchess.utils.ChessBoard;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import java.io.IOException;


public class HostController {

    private AppManager appManager;
    @FXML private Label ipLbl;
    @FXML private VBox lineLogoHolder;
    @FXML private ImageView logoImg;
    @FXML private Line line;
    @FXML private ChoiceBox<String> dropDownTimer;
    @FXML private Spinner<Integer> spinnerMinutes;
    @FXML private Spinner<Integer> spinnerSeconds;
    @FXML private Button btnSubmit;

    private BooleanProperty submitable;






    /**
     *  show(): Sets root of scene to the GameControllerView
     * @param primaryStage
     * @param logic AppLogic that evoked this method
     * @throws IOException If FXML File couldn't be opened
     */
    public static void show(Stage primaryStage, AppManager logic) throws IOException {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(GameController.class.getResource("/view/hostView.fxml"));
        Parent root = loader.load();

        // get controller from fxml file and basic setup
        HostController controller = loader.getController();
        controller.setAppManager(logic);

        controller.dropDownTimer.getItems().addAll("Fixed","Fischer","Bronstein","Sanduhr");
        controller.dropDownTimer.setValue("Fixed");
        SpinnerValueFactory<Integer> seconds = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        SpinnerValueFactory<Integer> minutes = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, 10);
        controller.spinnerSeconds.setValueFactory(seconds);
        controller.spinnerMinutes.setValueFactory(minutes);


        controller.spinnerSeconds.disableProperty().bind(controller.dropDownTimer.valueProperty().isEqualTo("Fixed").or(controller.dropDownTimer.valueProperty().isEqualTo("Sanduhr")));
        controller.btnSubmit.disableProperty().bind(Bindings.createBooleanBinding(
                () -> {
                    if(controller.dropDownTimer.getValue() == "Bronstein" || controller.dropDownTimer.getValue() == "Fischer")
                        return controller.spinnerSeconds.getValue() <= 0 || controller.spinnerMinutes.getValue() <= 0;
                    else return controller.spinnerMinutes.getValue() <= 0;
                },
                controller.dropDownTimer.valueProperty(), controller.spinnerSeconds.valueProperty(), controller.spinnerMinutes.valueProperty()
        ));

        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }


    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }


    @FXML
    void onSubmit(ActionEvent event) {

    }


}
