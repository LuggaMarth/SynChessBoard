package at.synchess.boardsoftware.front.controller;

import at.synchess.boardsoftware.front.model.AppManager;
import at.synchess.utils.ChessUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private AppManager appManager;
    private int gameId;
    private ChessUtils chessUtils;

    Image pieceImages[];
    List<ImageView> currPieces;

    @FXML
    private Label lbStatus;
    @FXML
    private GridPane chessBoard;


    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }

    public static void show(Stage primaryStage, AppManager logic, int gameId) throws IOException, MqttException {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(GameController.class.getResource("/view/gameView.fxml"));
        Parent root = loader.load();

        // get controller from fxml file and basic setup
        GameController controller = loader.getController();
        controller.setAppManager(logic);

        logic.getClient().subscribeToGame(gameId, controller);

        controller.chessUtils = new ChessUtils(false);
        controller.setBoard(controller.chessUtils.board);
        controller.gameId = gameId;
        controller.lbStatus.setText("Current Game: " + gameId);

        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    @FXML
    public void initialize() {


        currPieces = new ArrayList<>();
        // Initialize the chess pieces on the board
        pieceImages = new Image[]{
                new Image(getClass().getResource("/images/pieces/white_Pawn.png").toString()),
                new Image(getClass().getResource("/images/pieces/black_Pawn.png").toString()),
                new Image(getClass().getResource("/images/pieces/white_Rook.png").toString()),
                new Image(getClass().getResource("/images/pieces/black_Rook.png").toString()),
                new Image(getClass().getResource("/images/pieces/white_Knight.png").toString()),
                new Image(getClass().getResource("/images/pieces/black_Knight.png").toString()),
                new Image(getClass().getResource("/images/pieces/white_Bishop.png").toString()),
                new Image(getClass().getResource("/images/pieces/black_Bishop.png").toString()),
                new Image(getClass().getResource("/images/pieces/white_Queen.png").toString()),
                new Image(getClass().getResource("/images/pieces/black_Queen.png").toString()),
                new Image(getClass().getResource("/images/pieces/white_King.png").toString()),
                new Image(getClass().getResource("/images/pieces/black_King.png").toString())};


    }

    private void setupInitialPieces() {
        for (int i = 0; i < 8; i++) {
        }
    }

    public void onMessageReceived(String message){
        Platform.runLater(()-> {
            lbStatus.setText("> " + message);
        });

    }

    private void setBoard(int[][] values) {

        while (currPieces.size() != 0) {
            chessBoard.getChildren().remove(currPieces.get(0));
        }
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 8; ++x) {
                if (values[x][y] != 0) {
                    ImageView newPiece = new ImageView(pieceImages[values[x][y] - 1]);
                    newPiece.setFitHeight(50);
                    newPiece.setFitWidth(50);
                    currPieces.add(newPiece);
                    chessBoard.add(newPiece, x, y);
                }
            }
        }
    }
}