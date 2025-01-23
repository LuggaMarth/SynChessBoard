package at.synchess.boardsoftware.core.driver;

import at.synchess.boardsoftware.core.driver.enums.ChessBoardSector;
import at.synchess.boardsoftware.core.driver.enums.StepDirection;
import at.synchess.boardsoftware.exceptions.SynChessCoreException;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * SCDCore implements the communication to the hardware driver
 *
 * @author Luca Marth
 */
public class SCDCore {
    public static final int CHESS_BOARD_SIZE = 64;
    public static final int HALF_FIELD_STP = 500;
    public static final int FULL_FIELD_STP = 1000;

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

    private static final String ARDUINO_PORT_NAME = "/dev/ttyACM0";

    private final SerialPort serialPort;

    /**
     * SCDCore(): Constructor
     */
    protected SCDCore() {
        // create serial port object
        serialPort = new SerialPort(ARDUINO_PORT_NAME);

        try {
            // open the serial port
            serialPort.openPort();

            // set parameters of the serial port
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE
            );
        } catch (SerialPortException e) {
            System.out.println("ERROR: Could not open serial port " + ARDUINO_PORT_NAME);
        }
    }

    // *********************** Scan Board Methods ***********************
    /**
     * readChessField(): Reads the value of a specific chess field.
     *
     * @param id selector for the field
     * @return chess figure value (documentation) or 0 for empty
     */
    public char readChessField(int id) throws SynChessCoreException {
        String commandToSend = COMMAND_READ;

        // formation of command "Rxx;"
        if (id < 10) {
            commandToSend += "0" + id;
        } else {
            commandToSend += id;
        }
        commandToSend += ";";

        // send to arduino
        String retval = executeCommand(commandToSend, TIMEOUT_READ_ALL, 1);

        if(retval == null) {
            throw new SynChessCoreException("Something went wrong executing the command " + commandToSend);
        }

        return retval.charAt(0);
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
        String board = executeCommand(command, TIMEOUT_HOME, bytec);

        if(board == null || board.length() < CHESS_BOARD_SIZE) throw new SynChessCoreException("Something went wrong executing the command " + COMMAND_READ_CENTER);

        return board.toCharArray();
    }
    // ******************************************************************


    // *********************** Move figure methods ***********************
    /**
     * step(): Moves the CoreXY in the desired direction.
     * @param direction In which direction to move
     * @param steps How many steps
     */
    public void step(StepDirection direction, int steps) throws SynChessCoreException {
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

        // DEBUG ONLY
        System.out.print(commandToSend);

        // send to arduino
        /*String retval = executeCommand(commandToSend, TIMEOUT_READ_ALL, 1);

        if((retval != null ? retval.charAt(0) : '_') != COMMAND_ACKNOWLEDGED) {
            throw new SynChessCoreException("Something went wrong executing the command " + commandToSend);
        }
         */
    }

    public boolean switchMagnet(boolean state) throws SynChessCoreException {
        String retval;

        if (state) {
            retval = executeCommand(COMMAND_MAGNET_ON, TIMEOUT_HOME, 1);
        } else {
            retval = executeCommand(COMMAND_MAGNET_OFF, TIMEOUT_HOME, 1);
        }

        return retval.charAt(0) == COMMAND_ACKNOWLEDGED;
    }
    // ******************************************************************


    // *********************** Utility Commands ***********************
    /**
     * home(): Return CoreXY to (0 0)
     */
    public void home() throws SynChessCoreException {
        String retval = executeCommand(COMMAND_HOME, TIMEOUT_HOME, 1);

        if((retval != null ? retval.charAt(0) : '_') != COMMAND_ACKNOWLEDGED) {
            throw new SynChessCoreException("Something went wrong executing the command " + COMMAND_HOME);
        }
    }

    /**
     * isArduinoAvailable(): Checks, whether the Arduino is available.
     * @return true or false
     * @throws SynChessCoreException if stuff goes wrong
     */
    public boolean isArduinoAvailable() throws SynChessCoreException {
        String returnval;

        try {
            returnval = executeCommand(COMMAND_SYN, TIMEOUT_READ_ALL, 1);
        } catch (SynChessCoreException e) {
            throw new SynChessCoreException("Not today, pal :c");
        }

        return (returnval.charAt(0) == COMMAND_ACKNOWLEDGED);
    }

    /**
     * close(): closes the connection to the Arduino
     * @throws SerialPortException throws stuff
     */
    public void close() throws SerialPortException {
        serialPort.closePort();
    }
    // ******************************************************************

    // *********************** Send commands ***********************
    /**
     * executeRawCommand(): Executes raw commands on the hardware. Use this with caution due to
     * raw command handling!
     * @param command sends the given command directly to the serial port
     */
    public String executeCommand(String command, int timeout, int answerByteCount) throws SynChessCoreException {
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

                    serialPort.writeString(sb.toString());
                    returnValue = serialPort.readString(answerByteCount, timeout);

                    if(returnValue.charAt(0) != COMMAND_ACKNOWLEDGED) {
                        throw new SynChessCoreException("Not today, pal :c");
                    }
                }
            }

            else {
                // write command
                serialPort.writeString(command);

                // await response
                returnValue = serialPort.readString(answerByteCount, timeout);
            }
        } catch (SerialPortException | SerialPortTimeoutException e) {
            throw new SynChessCoreException("Not today, pal :c");
        }

        return returnValue;
    }
    // ******************************************************************
}
