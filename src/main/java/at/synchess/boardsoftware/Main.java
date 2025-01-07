package at.synchess.boardsoftware;

import at.synchess.boardsoftware.core.driver.SCDCommandLayer;
import at.synchess.boardsoftware.exceptions.SynChessCoreException;
import at.synchess.boardsoftware.front.model.AppManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;

/**
 * at.htlwels.Main: Handles the start of the JavaFX - Application.
 * @author Kilian Nussbaumer & Luca Marth
 */
public class Main extends Application {
    public AppManager appManager;

    public static void main(String[] args) {
        SCDCommandLayer scdCommandLayer = new SCDCommandLayer();
        String cmd = "";
        Scanner sc = new Scanner(System.in);

//        do {
//            System.out.print("> ");
//            cmd = sc.nextLine();
//
//            if(cmd.equals("test")) {
//                try {
//                    scdCommandLayer.getCore().executeCommand("SD200;SU100;SL100;SD100;SU200;SL120;SD200;SR70;SL140;SR70;SU200;SL120;SD200;SU200;SL100;", 10000);
//                } catch (SynChessCoreException e) {
//                    e.printStackTrace();
//                }
//            }
//        } while(!cmd.equals("exit"));

        try {
            scdCommandLayer.moveFromTo(1,1,8,6);
        } catch (SynChessCoreException e) {
            throw new RuntimeException(e);
        }

        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        appManager = new AppManager(primaryStage);
        appManager.showTitleScreen();
    }
}