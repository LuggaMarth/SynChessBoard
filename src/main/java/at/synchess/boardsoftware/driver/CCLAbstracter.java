package at.synchess.boardsoftware.driver;

import at.synchess.boardsoftware.driver.connection.IDriverConnection;
import at.synchess.boardsoftware.enums.StepDirection;
import at.synchess.boardsoftware.exceptions.CCLException;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * CCLAbstracter: Class to represent serial commands as methods
 */
public class CCLAbstracter {
    public static final int HALF_FIELD_STP = 500;
    public static final int FULL_FIELD_STP = 1000;

    private final IDriverConnection driverConnection;

    private static final String COMMAND_AVAILABLE = "A";

    private static final String COMMAND_READ = "R";
    private static final String COMMAND_READ_CENTER = "B";
    private static final String COMMAND_READ_OUT_WHITE = "W";
    private static final String COMMAND_READ_OUT_BLACK = "O";

    private static final String COMMAND_STEP = "S";
    private static final String COMMAND_HOME = "H";
    private static final String COMMAND_MAGNET = "M";

    private static final char RESPONSE_OKAY = 'X';

    private static final int TIMEOUT_READ_ALL = 15000;
    private static final int TIMEOUT_HOME = 20000;
    private static final int TIMEOUT_SYN = 5000;

    public CCLAbstracter(IDriverConnection driverConnection) {
        this.driverConnection = driverConnection;
    }

    public IDriverConnection getDriverConnection() {
        return driverConnection;
    }

    //---------------------------------- Utility Commands ----------------------------------//

    /**
     * isDriverAvailable(): Checks if the driver is available!
     * CCLSyntax: A
     * @return true if available otherwise false
     * @throws SerialPortTimeoutException If no answer
     * @throws SerialPortException If no answer
     */
    public boolean isDriverAvailable() throws SerialPortTimeoutException, SerialPortException {
        driverConnection.write(COMMAND_AVAILABLE);
        String returnValue = driverConnection.read(1, TIMEOUT_SYN);
        return returnValue.charAt(0) == RESPONSE_OKAY;
    }

    /**
     * home(): Return CoreXY to (0 0)
     */
    public void home() throws CCLException {
        sendWithOkayCheck(COMMAND_HOME, 1, TIMEOUT_HOME);
    }

    //--------------------------------------------------------------------------------------//


    //---------------------------------- Reading Commands ----------------------------------//
    public String readCenter() throws SerialPortException, SerialPortTimeoutException {
        // read board from arduino
        driverConnection.write(COMMAND_READ_CENTER);
        return driverConnection.read(64, TIMEOUT_READ_ALL);
    }

    public String readOutWhite() throws SerialPortException, SerialPortTimeoutException {
        // read board from arduino
        driverConnection.write(COMMAND_READ_OUT_WHITE);
        return driverConnection.read(32, TIMEOUT_READ_ALL);
    }

    public String readOutBlack() throws SerialPortException, SerialPortTimeoutException {
        // read board from arduino
        driverConnection.write(COMMAND_READ_OUT_BLACK);
        return driverConnection.read(32, TIMEOUT_READ_ALL);
    }
    //--------------------------------------------------------------------------------------//

    //---------------------------------- Step Commands ----------------------------------//
    public void stepLeft(int steps) throws CCLException {
        sendWithOkayCheck("SL" + steps, TIMEOUT_HOME, 1);
    }
    public void stepRight(int steps) throws CCLException {
        sendWithOkayCheck("SR" + steps, TIMEOUT_HOME, 1);
    }
    public void stepUp(int steps) throws CCLException {
        sendWithOkayCheck("SU" + steps, TIMEOUT_HOME, 1);
    }
    public void stepDown(int steps) throws CCLException {
        sendWithOkayCheck("SD" + steps, TIMEOUT_HOME, 1);
    }
    public void step(StepDirection direction, int steps) throws CCLException {
        if (steps < 0) throw new CCLException("StepCount cannot be less than 0!");

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

        sendWithOkayCheck(commandToSend, 1, TIMEOUT_HOME);
    }
    //-----------------------------------------------------------------------------------//

    //---------------------------------- Magnet Commands ----------------------------------//
    public void magnetOn() throws CCLException {
        sendWithOkayCheck(COMMAND_MAGNET + "1", 1, 1000); // TODO: TIMEOUT
    }

    public void magnetOff() throws CCLException {
        sendWithOkayCheck(COMMAND_MAGNET + "0", 1, 1000); // TODO: TIMEOUT
    }
    //-----------------------------------------------------------------------------------//


    //---------------------------------- Raw Commands ----------------------------------//
    /**
     * executeSplitCommand(): Executes raw commands on the hardware. Use this with caution due to
     * raw command handling!
     * @param command sends the given command directly to the serial port
     * @deprecated
     */
    public String executeSplitCommand(String command, int timeout, int answerByteCount) throws CCLException {
        String returnValue = "";
        String[] commandsToSend;

        try {
            // split command
            if(command.length() > 50) {
                commandsToSend = command.split(";");

                for (int i = 0; i < commandsToSend.length / 10 + 1; i++) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = i*10; j < 10+i*10; j++) {
                        if(j < commandsToSend.length){
                            sb.append(commandsToSend[j]);
                            sb.append(";");
                        }
                    }

                    driverConnection.write(sb.toString());
                    returnValue = driverConnection.read(answerByteCount, timeout);

                    if(returnValue.charAt(0) != RESPONSE_OKAY) {
                        throw new CCLException("Not today, pal :c");
                    }
                }
            }

            else {
                // write command
                driverConnection.write(command);

                // await response
                returnValue = driverConnection.read(answerByteCount, timeout);
            }
        } catch (SerialPortException | SerialPortTimeoutException e) {
            throw new CCLException("Not today, pal :c");
        }

        return returnValue;
    }

    //-----------------------------------------------------------------------------------//

    //---------------------------------- Send to Arduino ----------------------------------//
    private void sendWithOkayCheck(String command, int bytec, int timeout) throws CCLException {
        try {
            driverConnection.write(command);
            String retval = driverConnection.read(bytec, timeout);

            if ((retval != null ? retval.charAt(0) : '_') != RESPONSE_OKAY) {
                throw new CCLException("Something went wrong executing the command " + command);
            }
        }
        catch(SerialPortException | SerialPortTimeoutException e) {
            throw new CCLException(e.getMessage());
        }
    }
}
