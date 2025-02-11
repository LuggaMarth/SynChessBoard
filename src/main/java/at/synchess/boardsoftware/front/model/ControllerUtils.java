package at.synchess.boardsoftware.front.model;

import at.synchess.boardsoftware.driver.SynChessDriver;
import at.synchess.boardsoftware.exceptions.AppManagerException;
import at.synchess.boardsoftware.exceptions.CCLException;
import at.synchess.utils.ChessBoard;
import at.synchess.utils.Move;
import at.synchess.utils.Pieces;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class ControllerUtils {
    public static FontIcon getFontIcon(String name, int size, Color fill) {
        FontIcon ret = new FontIcon(name);
        ret.setIconSize(size);
        ret.setFill(fill);

        return ret;
    }

    public static void showServerAlert(String message, Stage primStage){
        Alert a = new Alert(AlertType.ERROR);
        a.setContentText("Server Error: " + message);
        a.initOwner(primStage);
        a.show();
    }

    public static void showAppManagerAlert(AppManagerException ae, Stage primStage){
        Alert a = new Alert(AlertType.ERROR);
        a.setContentText("Failed building View: " + ae.getController().getSimpleName());
        a.initOwner(primStage);
        a.show();
    }
    public static void showSafeAlert(String message){
        Alert a = new Alert(AlertType.ERROR);
        a.setContentText("Error: " + message);

        a.show();
    }

    public static void showWarning(String message, Stage primStage){
        Alert a = new Alert(AlertType.WARNING);
        a.setContentText(message);
        a.initOwner(primStage);
        a.show();
    }

    public static void commitMove(Move m, ChessBoard cb, SynChessDriver driver) throws CCLException {
        switch (m.getMoveType()){
            case STANDARD:
                if (cb.board[m.getTargX()][m.getTargY()] != Pieces.NONE){

                    driver.removeFigure(m.getTargX(), m.getTargY());
                }

                driver.moveFigure(m.getStartX(), m.getStartY(),m.getTargX(), m.getTargY());
            break;
            case CASTLE:
                switch (m.getCastleType()){
                  //TODO: später-ich-problem
                }
            break;
            case PROMOTION:
                driver.moveFigure(m.getStartX(), m.getStartY(),m.getTargX(), m.getTargY());
                driver.removeFigure(m.getTargX(),m.getTargY());
                driver.addFigure(m.getTargX(),m.getTargY(), m.getPiece());
        }
    }

}
