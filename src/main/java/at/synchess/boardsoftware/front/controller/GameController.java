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
    private Label gameBanner;
    @FXML
    private GridPane chessBoard;


    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }


    public static void show(Stage primaryStage, AppManager logic, int gameId) throws IOException {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(GameController.class.getResource("/view/gameView.fxml"));
        Parent root = loader.load();

        // get controller from fxml file and basic setup
        GameController controller = loader.getController();
        controller.setAppManager(logic);

        controller.chessUtils = new ChessUtils(false);
        controller.setBoard(controller.chessUtils.board);
        controller.gameId = gameId;
        controller.gameBanner.setText("Current Game: " + gameId);

        // init scene and stage
        Scene s = new Scene(root);

        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    @FXML
    public void initialize() {


        currPieces = new ArrayList<>();
        // Initialize the chess pieces on the board
        pieceImages = new Image[]{
                new Image(getClass().getResource("../view/pieces/white_Pawn.png").toString()),
                new Image(getClass().getResource("../view/pieces/black_Pawn.png").toString()),
                new Image(getClass().getResource("../view/pieces/white_Rook.png").toString()),
                new Image(getClass().getResource("../view/pieces/black_Rook.png").toString()),
                new Image(getClass().getResource("../view/pieces/white_Knight.png").toString()),
                new Image(getClass().getResource("../view/pieces/black_Knight.png").toString()),
                new Image(getClass().getResource("../view/pieces/white_Bishop.png").toString()),
                new Image(getClass().getResource("../view/pieces/black_Bishop.png").toString()),
                new Image(getClass().getResource("../view/pieces/white_Queen.png").toString()),
                new Image(getClass().getResource("../view/pieces/black_Queen.png").toString()),
                new Image(getClass().getResource("../view/pieces/white_King.png").toString()),
                new Image(getClass().getResource("../view/pieces/black_King.png").toString())};


    }

    private void setupInitialPieces() {
        for (int i = 0; i < 8; i++) {
        }
    }

    private void setBoard(int[][] values) {

        while (currPieces.size() != 0) {
            chessBoard.getChildren().remove(currPieces.get(0));
        }
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (values[i][j] != 0) {
                    ImageView newPiece = new ImageView(pieceImages[values[i][j] - 1]);
                    newPiece.setFitHeight(50);
                    newPiece.setFitWidth(50);
                    currPieces.add(newPiece);
                    chessBoard.add(newPiece, i, j);
                }
            }
        }
    }
}