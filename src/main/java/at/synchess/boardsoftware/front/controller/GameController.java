package at.synchess.boardsoftware.front.controller;

import at.synchess.boardsoftware.front.model.AppManager;

import at.synchess.boardsoftware.front.model.ChessClient;
import at.synchess.utils.ChessBoard;
import at.synchess.utils.ChessNotation;
import at.synchess.utils.Move;
import at.synchess.utils.Pieces;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.shape.Rectangle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * View during game. Displays current time, boardstate and status of the game
 */
public class GameController {

    private AppManager appManager;
    private int gameId;
    private ChessBoard chessUtils;

    Image pieceImages[];
    List<ImageView> currPieces;

    @FXML
    private Label lbStatus;
    @FXML
    private GridPane chessBoard;


    /**
     *  show(): Sets root of scene to the GameControllerView
     * @param primaryStage
     * @param logic AppLogic that evoked this method
     * @param gameId ID of game being joined
     * @throws IOException If FXML File couldn't be opened
     */
    public static void show(Stage primaryStage, AppManager logic, int gameId) throws IOException, MqttException {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(GameController.class.getResource("/view/gameView.fxml"));
        Parent root = loader.load();

        // get controller from fxml file and basic setup
        GameController controller = loader.getController();
        controller.setAppManager(logic);

        logic.getClient().subscribeToGame(gameId, controller);

        controller.chessUtils = new ChessBoard(false);
        controller.setBoard(controller.chessUtils.board);

        controller.gameId = gameId;
        controller.lbStatus.setText("Current Game: " + gameId);

        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
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


    @FXML
    void sendClicked(ActionEvent event) {

    }


    /**
     * Opened by the ChessClient, whenever a Mqtt-Announcement is received
     * @param message
     */
    //TODO: Implement ChessLibary to read and apply turns
    public void onMessageReceived(String message){
        Platform.runLater(()-> {
            String data[] = message.split(" ");
            Move m = ChessNotation.parseAnnotation(data[0]);
            //TODO: chessUtils.applyMove(m);
            displayMove(m);
        });

    }


    private void displayMove(Move m){
        washBoard();
        switch (m.getMoveType()){
            case STANDARD:
            case PROMOTION:
                setPiece(m.getPiece(), m.getTargX(),m.getTargY());
                clearTile(m.getStartX(),m.getStartY());
                markTile(m.getTargX(),m.getTargY());
                markTile(m.getStartX(),m.getStartY());
                break;
            case CASTLE:

                switch (m.getCastleType()){
                    case 0:
                        clearTile(0,4);
                        clearTile(0,5);
                        clearTile(0,6);
                        clearTile(0,7);
                        setPiece(Pieces.bROOK,0,5);
                        setPiece(Pieces.bKING,0,6);

                        break;
                    case 1:
                        clearTile(0,0);
                        clearTile(0,1);
                        clearTile(0,2);
                        clearTile(0,3);
                        clearTile(0,4);
                        setPiece(Pieces.bKING,0,1);
                        setPiece(Pieces.bROOK,0,2);
                        break;
                    case 2:
                        clearTile(7,0);
                        clearTile(7,1);
                        clearTile(7,2);
                        clearTile(7,3);
                        setPiece(Pieces.wKING,7,1);
                        setPiece(Pieces.wROOK,7,2);
                        break;
                    case 3:
                        clearTile(7,3);
                        clearTile(7,4);
                        clearTile(7,5);
                        clearTile(7,6);
                        clearTile(7,7);
                        setPiece(Pieces.wROOK,7,5);
                        setPiece(Pieces.wKING,7,6);
                        break;
                }
        }

    }


    /**
     * Sets the displayed board to the state "values"
     * IDs of the pieces are notated in the ChessUtils library
     * @param values
     */
    private void setBoard(int[][] values) {


        while (currPieces.size() != 0) {
            chessBoard.getChildren().remove(currPieces.get(0));
        }
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 8; ++x) {
                if (values[x][y] != 0) {
                    ImageView newPiece = new ImageView(pieceImages[values[x][y] - 1]);
                    newPiece.setFitHeight(45);
                    newPiece.setFitWidth(45);
                    currPieces.add(newPiece);
                    chessBoard.add(newPiece, x, y);
                }
            }
        }
    }

    private void setPiece(int piece, int x, int y){
        ImageView newPiece = new ImageView(pieceImages[piece]);
        newPiece.setFitHeight(45);
        newPiece.setFitWidth(45);
        currPieces.add(newPiece);
        chessBoard.add(newPiece, x, y);
    }

    private void clearTile(int x, int y){
        chessBoard.getChildren().removeIf(node -> {
            if ( GridPane.getRowIndex(node) == y && GridPane.getColumnIndex(node) == x)
               return node instanceof ImageView;
           else return false;
        });
    }

    private void markTile(int x, int y){
       for(Node node : chessBoard.getChildren()){
           if (node instanceof Rectangle && GridPane.getRowIndex(node) == y && GridPane.getColumnIndex(node) == x){
               ((Rectangle) node).setFill(Color.YELLOW);
           }
       }
    }

    private void washBoard(){
        for (Node node : chessBoard.getChildren()){
            if (node instanceof Rectangle){
                ((Rectangle) node).setFill(((GridPane.getRowIndex(node) + GridPane.getColumnIndex(node)) % 2 == 0) ? Color.BEIGE : Color.OLIVE);
            }
        }
    }
}