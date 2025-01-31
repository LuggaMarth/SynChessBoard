package at.synchess.boardsoftware.utils;

import java.io.IOException;

/**
 * Manages internal functions like shutting down the raspberry
 *
 * @author Luca Marth
 */
public class RaspiManager {
    public static void shutdownRaspberry() {
        try {
            Process p = Runtime.getRuntime().exec("sudo shutdown now");
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
