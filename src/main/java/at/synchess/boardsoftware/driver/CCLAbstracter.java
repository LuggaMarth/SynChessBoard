package at.synchess.boardsoftware.driver;

import at.synchess.boardsoftware.enums.StepDirection;

/**
 * Class CCLAbstracter: Has methods that abstract the methods of the driver and
 * returns fully qualified CCL commands.
 *
 * @author Luca Marth
 */
public class CCLAbstracter {
    public static final int HALF_FIELD_STP = 150;
    public static final int FULL_FIELD_STP = 300;
    public static final int STEPS_TO_FIRST_FIELD_Y = 210;
    public static final int STEPS_TO_FIRST_FIELD_X = 1225;

    private static final String COMMAND_AVAILABLE = "A";
    private static final String COMMAND_READ_CENTER = "R2";
    private static final String COMMAND_READ_OUT_WHITE = "R3";
    private static final String COMMAND_READ_OUT_BLACK = "R1";
    private static final String COMMAND_STEP = "S";
    private static final String COMMAND_HOME = "H";
    private static final String COMMAND_MAGNET = "M";
    private static final String COMMAND_WAIT= "W";

    private static final String SEPARATOR = ";";


    //---------------------------------- Utility Commands ----------------------------------//
    /**
     * home(): Return CoreXY to (0 0)
     */
    public String home() {
        return COMMAND_HOME + SEPARATOR + COMMAND_WAIT + 100 + SEPARATOR;
    }

    /**
     * wait(): Waits for a given amount of milliseconds
     * @param milliseconds time to wait
     * @return command
     */
    public String wait(int milliseconds) {
        return COMMAND_WAIT + milliseconds + SEPARATOR;
    }
    //--------------------------------------------------------------------------------------//


    //---------------------------------- Reading Commands ----------------------------------//
    public String readCenter() {
        return COMMAND_READ_CENTER + SEPARATOR;
    }

    public String readOutWhite() {
        return COMMAND_READ_OUT_WHITE + SEPARATOR;
    }

    public String readOutBlack() {
        return COMMAND_READ_OUT_BLACK + SEPARATOR;
    }
    //--------------------------------------------------------------------------------------//


    //---------------------------------- Step Commands ----------------------------------//
    public String stepLeft(int steps) {
        return step(StepDirection.LEFT, steps);
    }

    public String stepRight(int steps) {
        return step(StepDirection.RIGHT, steps);
    }

    public String stepUp(int steps) {
        return step(StepDirection.UP, steps);
    }

    public String stepDown(int steps) {
        return step(StepDirection.DOWN, steps);
    }

    public String step(StepDirection direction, int steps) {
        String commandToSend = COMMAND_STEP;

        switch (direction) {
            case UP:
                commandToSend += "U" + steps + SEPARATOR;
                break;
            case DOWN:
                commandToSend += "D" + steps + SEPARATOR;
                break;
            case LEFT:
                commandToSend += "L" + steps + SEPARATOR;
                break;
            case RIGHT:
                commandToSend += "R" + steps + SEPARATOR;
                break;
        }

        return commandToSend;
    }
    //-----------------------------------------------------------------------------------//


    //---------------------------------- Magnet Commands ----------------------------------//
    public String magnetOn() {
        return COMMAND_MAGNET + "1" + SEPARATOR;
    }

    public String magnetOff() {
        return COMMAND_MAGNET + "0" + SEPARATOR;
    }
    //-----------------------------------------------------------------------------------//
}
