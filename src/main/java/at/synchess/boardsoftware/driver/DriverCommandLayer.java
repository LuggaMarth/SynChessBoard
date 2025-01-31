package at.synchess.boardsoftware.driver;

import at.synchess.boardsoftware.driver.connection.IDriverConnection;
import at.synchess.boardsoftware.enums.ChessBoardSector;
import at.synchess.boardsoftware.enums.StepDirection;
import at.synchess.boardsoftware.exceptions.SynChessCoreException;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class DriverCommandLayer {
    private final IDriverConnection driverConnection;

    private static final String COMMAND_SYN = "A";
    private static final char COMMAND_ACKNOWLEDGED = 'X';

    private static final String COMMAND_READ = "R";
    private static final String COMMAND_READ_CENTER = "B";
    private static final String COMMAND_READ_OUT_WHITE = "W";
    private static final String COMMAND_READ_OUT_BLACK = "O";

    private static final String COMMAND_STEP = "S";
    private static final String COMMAND_HOME = "H";
    private static final String COMMAND_MAGNET_ON = "M";
    private static final String COMMAND_MAGNET_OFF = "N";

    private static final int TIMEOUT_READ_ALL = 15000;
    private static final int TIMEOUT_HOME = 20000;

    public DriverCommandLayer(IDriverConnection driverConnection) {
        this.driverConnection = driverConnection;
    }

    /**
     * readBoard(): Returns the whole chess board as a char array
     *
     * @return chess board
     */
    public char[] readBoard(ChessBoardSector section) throws SynChessCoreException {
        String command;
        int bytec;

        if(section == ChessBoardSector.OUT_WHITE) {
            command = COMMAND_READ_OUT_WHITE;
            bytec = 32;
        } else if (section == ChessBoardSector.OUT_BLACK) {
            command = COMMAND_READ_OUT_BLACK;
            bytec = 32;
        } else {
            command = COMMAND_READ_CENTER;
            bytec = 64;
        }

        // read board from arduino
        String board = null;
        try {
            driverConnection.write(command);
            board = driverConnection.read(bytec, TIMEOUT_READ_ALL);
        } catch (SerialPortException | SerialPortTimeoutException e) {
            throw new SynChessCoreException(e.getMessage());
        }

        if(board == null || board.length() < bytec) throw new SynChessCoreException("Something went wrong executing the command " + COMMAND_READ_CENTER);

        return board.toCharArray();
    }

    /**
     * step(): Moves the CoreXY in the desired direction.
     * @param direction In which direction to move
     * @param steps How many steps
     */
    private void step(StepDirection direction, int steps) throws SynChessCoreException {
        if (steps < 0) throw new SynChessCoreException("StepCount cannot be less than 0!");

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

        // send to arduino
        sendWithoutReturnValue(commandToSend, TIMEOUT_HOME, 1);
    }

    public boolean switchMagnet(boolean state) throws SynChessCoreException {
        String retval;

        try {
            if (state) {
                driverConnection.write(COMMAND_MAGNET_ON);
                retval = driverConnection.read(TIMEOUT_HOME, 1);
            } else {
                driverConnection.write(COMMAND_MAGNET_OFF);
                retval = driverConnection.read(TIMEOUT_HOME, 1);
            }
        } catch(SerialPortException | SerialPortTimeoutException e) {
            throw new SynChessCoreException(e.getMessage());
        }

        return retval.charAt(0) == COMMAND_ACKNOWLEDGED;
    }

    /**
     * home(): Return CoreXY to (0 0)
     */
    public void home() throws SynChessCoreException {
        sendWithoutReturnValue(COMMAND_HOME, 1, TIMEOUT_HOME);
    }

    /**
     * isArduinoAvailable(): Checks, whether the Arduino is available.
     * @return true or false
     * @throws SynChessCoreException if stuff goes wrong
     */
    public boolean isArduinoAvailable() throws SynChessCoreException {
        String returnval;

        try {
            driverConnection.write(COMMAND_SYN);

            returnval = driverConnection.read(1, TIMEOUT_READ_ALL);
        } catch (SerialPortException | SerialPortTimeoutException e) {
            throw new SynChessCoreException(e.getMessage());
        }

        return (returnval.charAt(0) == COMMAND_ACKNOWLEDGED);
    }

    /**
     * executeRawCommand(): Executes raw commands on the hardware. Use this with caution due to
     * raw command handling!
     * @param command sends the given command directly to the serial port
     */
    public String executeSplittedCommand(String command, int timeout, int answerByteCount) throws SynChessCoreException {
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

                    if(returnValue.charAt(0) != COMMAND_ACKNOWLEDGED) {
                        throw new SynChessCoreException("Not today, pal :c");
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
            throw new SynChessCoreException("Not today, pal :c");
        }

        return returnValue;
    }

    private void sendWithoutReturnValue(String command, int bytec, int timeout) throws SynChessCoreException {
        try {
            driverConnection.write(command);
            String retval = driverConnection.read(bytec, timeout);

            if ((retval != null ? retval.charAt(0) : '_') != COMMAND_ACKNOWLEDGED) {
                throw new SynChessCoreException("Something went wrong executing the command " + command);
            }
        }
        catch(SerialPortException | SerialPortTimeoutException e) {
            throw new SynChessCoreException(e.getMessage());
        }
    }
}
