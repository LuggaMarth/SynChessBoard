package at.synchess.boardsoftware.driver;

import at.synchess.boardsoftware.enums.StepDirection;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class CCLAbstracter {
    public static final int HALF_FIELD_STP = 150;
    public static final int FULL_FIELD_STP = 300;

    private static final String COMMAND_AVAILABLE = "A";
    private static final String COMMAND_READ_CENTER = "R2";
    private static final String COMMAND_READ_OUT_WHITE = "R3";
    private static final String COMMAND_READ_OUT_BLACK = "R1";
    private static final String COMMAND_STEP = "S";
    private static final String COMMAND_HOME = "H";
    private static final String COMMAND_MAGNET = "M";


    //---------------------------------- Utility Commands ----------------------------------//

    /**
     * isDriverAvailable(): Checks if the driver is available!
     * CCLSyntax: A
     * @return String
     */
    public String isDriverAvailable() {
        return COMMAND_AVAILABLE + ";";
    }

    /**
     * home(): Return CoreXY to (0 0)
     */
    public String home() {
        return COMMAND_HOME + ";";
    }

    //--------------------------------------------------------------------------------------//


    //---------------------------------- Reading Commands ----------------------------------//
    public String readCenter() {
        return COMMAND_READ_CENTER + ";";
    }

    public String readOutWhite() {
        return COMMAND_READ_OUT_WHITE + ";";
    }

    public String readOutBlack() {
        return COMMAND_READ_OUT_BLACK + ";";
    }
    //--------------------------------------------------------------------------------------//


    //---------------------------------- Step Commands ----------------------------------//
    public String stepLeft(int steps) {
        return COMMAND_STEP + "L" + steps + ";";
    }

    public String stepRight(int steps) {
        return COMMAND_STEP + "R" + steps + ";";
    }

    public String stepUp(int steps) {
        return COMMAND_STEP + "U" + steps + ";";
    }

    public String stepDown(int steps) {
        return COMMAND_STEP + "D" + steps + ";";
    }

    public String step(StepDirection direction, int steps) {
        String commandToSend = COMMAND_STEP;

        switch (direction) {
            case UP:
                commandToSend += "U" + steps + ";";
                break;
            case DOWN:
                commandToSend += "D" + steps + ";";
                break;
            case LEFT:
                commandToSend += "L" + steps + ";";
                break;
            case RIGHT:
                commandToSend += "R" + steps + ";";
                break;
        }

        return commandToSend;
    }
    //-----------------------------------------------------------------------------------//


    //---------------------------------- Magnet Commands ----------------------------------//
    public String magnetOn() {
        return COMMAND_MAGNET + "1";
    }

    public String magnetOff() {
        return COMMAND_MAGNET + "0";
    }
    //-----------------------------------------------------------------------------------//
}
