package at.synchess.boardsoftware.front.controller;

import at.synchess.boardsoftware.Main;
import at.synchess.boardsoftware.driver.SynChessDriver;
import at.synchess.boardsoftware.enums.ChessBoardSector;
import at.synchess.boardsoftware.exceptions.AppManagerException;
import at.synchess.boardsoftware.exceptions.CCLException;
import at.synchess.boardsoftware.front.model.AppManager;
import at.synchess.boardsoftware.utils.NetworkManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Opens Dev Menu, used for Testing and Debugging
 */
public class DevScreenController {
    private AppManager manager;
    private Stage primaryStage;
    private SynChessDriver driver;

    /**
     *  show(): Currently doesn't do shit
     * @param primaryStage
     * @param logic AppLogic that evoked this method
     * @throws IOException If FXML File couldn't be opened
     */
    public static void show(Stage primaryStage, AppManager logic, SynChessDriver driver) throws IOException {
        // Load FXML
        System.out.println(DevScreenController.class.getResource("/view/devView.fxml"));
        FXMLLoader loader = new FXMLLoader(DevScreenController.class.getResource("/view/devView.fxml"));
        Parent root = loader.load();

        // get controller from fxml file and basic setup
        DevScreenController controller = loader.getController();
        controller.setManager(logic);
        controller.setPrimaryStage(primaryStage);
        controller.setDriver(driver);

        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    public void setManager(AppManager manager) {
        this.manager = manager;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setDriver(SynChessDriver driver) {
        this.driver = driver;
    }

    @FXML
    private ChoiceBox<Integer> cbF1X;
    @FXML
    private ChoiceBox<Integer> cbF1Y;
    @FXML
    private ChoiceBox<Integer> cbF2X;
    @FXML
    private ChoiceBox<Integer> cbF2Y;
    @FXML
    private ChoiceBox<String> cbNewPiece;
    @FXML
    private ChoiceBox<Integer> cbRPFX;
    @FXML
    private ChoiceBox<Integer> cbRPFY;
    @FXML
    private ChoiceBox<Integer> cbRevivePFX;
    @FXML
    private ChoiceBox<Integer> cbRevivePFY;
    @FXML
    private ChoiceBox<String> scanSektor;
    @FXML
    private ChoiceBox<String> sectorRemovePiece;
    @FXML
    private ChoiceBox<String> sectorRevive;
    @FXML
    private Label ipLbl;
    @FXML
    private Line line;
    @FXML
    private VBox lineLogoHolder;
    @FXML
    private ImageView logoImg;

    @FXML
    public void initialize() {
        ObservableList<Integer> values = FXCollections.observableList(List.of(0, 1, 2, 3, 4, 5, 6, 7));
        ObservableList<String> sectors = FXCollections.observableList(List.of("Schwarz", "Feld", "Weiß"));

        cbF1X.setItems(values);
        cbF1Y.setItems(values);
        cbF2X.setItems(values);
        cbF2Y.setItems(values);
        cbRPFX.setItems(values);
        cbRPFY.setItems(values);
        cbRevivePFX.setItems(values);
        cbRevivePFY.setItems(values);

        cbNewPiece.setItems(FXCollections.observableList(List.of("Bauer", "Turm", "Pferd", "Läufer", "König", "Königin")));

        scanSektor.setItems(sectors);
        sectorRevive.setItems(sectors);
        sectorRemovePiece.setItems(sectors);

        // pre-set ip label text on network address
        ipLbl.setText(NetworkManager.getIpV4AddressAsString(Main.INTERFACE_NAME));
        line.endXProperty().bind(lineLogoHolder.widthProperty().subtract(25));
    }

    @FXML
    void onBack(ActionEvent event) throws AppManagerException {
        manager.showTitleScreen();
    }

    @FXML
    void onMove(ActionEvent event) throws CCLException {
        if (cbF1X.valueProperty().get() != null && cbF2X.valueProperty().get() != null && cbF1Y.valueProperty().get() != null && cbF2Y.valueProperty().get() != null) {
            driver.movePiece(cbF1X.getValue(),  cbF1Y.getValue(), cbF2X.getValue(), cbF2Y.getValue());
        }
    }

    @FXML
    void onRemovePiece(ActionEvent event) throws CCLException {
        ChessBoardSector sector = switch (sectorRemovePiece.getValue()) {
            case "Weiß" -> ChessBoardSector.OUT_WHITE;
            default -> ChessBoardSector.OUT_BLACK;
        };

        System.out.println(sectorRemovePiece.getValue());
        System.out.println(sector);
        System.out.println(cbRPFX.getValue());
        System.out.println(cbRPFY.getValue());

        if(cbRPFX.getValue() != null && cbRPFY.getValue() != null) {
            driver.removePiece(cbRPFX.getValue(), cbRPFY.getValue(), sector);
        }
    }

    @FXML
    void onRevivePiece(ActionEvent event) {

    }

    @FXML
    void onScan(ActionEvent event) throws CCLException {
        if (scanSektor.getValue() != null) {
            switch (scanSektor.getValue()) {
                case "Schwarz":
                    driver.scan(ChessBoardSector.OUT_BLACK);
                    break;
                case "Feld":
                    driver.scan(ChessBoardSector.CENTER_BOARD);
                    break;
                case "Weiß":
                    driver.scan(ChessBoardSector.OUT_WHITE);
                    break;
            }
        }
    }

}
