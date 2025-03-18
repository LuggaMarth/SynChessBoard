package at.synchess.boardsoftware;

import at.synchess.boardsoftware.driver.CCLAbstracter;
import at.synchess.boardsoftware.driver.SynChessDriver;
import at.synchess.boardsoftware.driver.connection.SerialDriverConnector;
import at.synchess.boardsoftware.enums.ChessBoardSector;
import at.synchess.boardsoftware.exceptions.CCLException;
import at.synchess.boardsoftware.front.model.AppManager;

import java.util.Scanner;

/**
 * Main: Handles the start of the JavaFX - Application.
 * @author Kilian Nussbaumer & Luca Marth
 */
public class Main {//extends Application {
    public static String INTERFACE_NAME = "eth0";
    public AppManager appManager;

    public static void main(String[] args) {
        String command;
        Scanner sc = new Scanner(System.in);
        CCLAbstracter cclAbstracter = new CCLAbstracter();
        SynChessDriver driver = new SynChessDriver(cclAbstracter, new SerialDriverConnector());

        do {
            System.out.print("> ");
            command = sc.nextLine();

            String[] contents = command.split(" ");

            try {
                switch (contents[0]) {
                    case "M" ->
                            driver.movePiece(Integer.parseInt(contents[1]), Integer.parseInt(contents[2]), Integer.parseInt(contents[3]), Integer.parseInt(contents[4]));
                    case "H" -> driver.home();
                    case "sc1" -> printCharArray(driver.scan(ChessBoardSector.OUT_BLACK));
                    case "sc2" -> printCharArray(driver.scan(ChessBoardSector.CENTER_BOARD));
                    case "sc3" -> printCharArray(driver.scan(ChessBoardSector.OUT_WHITE));
                    case "rf" -> driver.removePiece(Integer.parseInt(contents[1]), Integer.parseInt(contents[2]), ChessBoardSector.OUT_BLACK);
                    case "af" -> driver.revivePiece(Integer.parseInt(contents[1]), Integer.parseInt(contents[2]), contents[3].charAt(0), ChessBoardSector.OUT_BLACK);
                }
            } catch (CCLException e) {
                e.printStackTrace();
            }
        } while(!command.equals("exit"));
        //launch(args);
    }

    private static void printCharArray(char[] arr) {
        for (char c : arr) {
            System.out.print(c);
        }

        System.out.println();
    }

    /*
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            appManager = new AppManager(primaryStage);
            appManager.showTitleScreen();
        } catch (CCLException s){
            ControllerUtils.showSafeAlert(s.getMessage());
        }
    }

    @Override
    public void stop() {
        //appManager.closeRoutine();
    }
    */
}