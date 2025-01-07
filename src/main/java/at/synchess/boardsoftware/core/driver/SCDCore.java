package at.synchess.boardsoftware.core.driver;

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

    private static final String COMMAND_READ = "R";
    private static final String COMMAND_SYN = "A";
    private static final char COMMAND_ACKNOWLEDGED = 'X';
    private static final String COMMAND_STEP = "S";
    private static final String COMMAND_READALL = "B";
    private static final String COMMAND_HOME = "H";

    private static final int TIMEOUT_READ_ALL = 10000;
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
        String retval = executeCommand(commandToSend, TIMEOUT_READ_ALL);

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
    public char[] readBoard() throws SynChessCoreException {
        String board = "";

        // read board from arduino
        String retval = executeCommand(COMMAND_READALL, TIMEOUT_HOME);

        if(retval == null || retval.length() < CHESS_BOARD_SIZE) throw new SynChessCoreException("Something went wrong executing the command " + COMMAND_READALL);

        return board.toCharArray();
    }

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
        /*String retval = executeCommand(commandToSend, TIMEOUT_READ_ALL);

        if((retval != null ? retval.charAt(0) : '_') != COMMAND_ACKNOWLEDGED) {
            throw new SynChessCoreException("Something went wrong executing the command " + commandToSend);
        }
         */
    }

    /**
     * home(): Return CoreXY to (0 0)
     */
    public void home() throws SynChessCoreException {
        String retval = executeCommand(COMMAND_HOME, TIMEOUT_HOME);

        if((retval != null ? retval.charAt(0) : '_') != COMMAND_ACKNOWLEDGED) {
            throw new SynChessCoreException("Something went wrong executing the command " + COMMAND_HOME);
        }
    }

    /**
     * executeRawCommand(): Executes raw commands on the hardware. Use this with caution due to
     * raw command handling!
     * @param command sends the given command directly to the serial port
     */
    public String executeCommand(String command, int timeout) throws SynChessCoreException {
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
                    returnValue = serialPort.readString(1, timeout);

                    if(returnValue.charAt(0) != COMMAND_ACKNOWLEDGED) {
                        throw new SynChessCoreException("Not today, pal :c");
                    }
                }
            }

            else {
                // write command
                serialPort.writeString(command);

                // await response
                returnValue = serialPort.readString(1, timeout);
            }
        } catch (SerialPortException | SerialPortTimeoutException e) {
            throw new SynChessCoreException("Not today, pal :c");
        }

        return returnValue;
    }

    public boolean isArduinoAvailable() throws SynChessCoreException {
        String returnval = "";

        try {
             returnval = executeCommand(COMMAND_SYN, TIMEOUT_READ_ALL);
        } catch (SynChessCoreException e) {
            throw new SynChessCoreException("Not today, pal :c");
        }

        return (returnval.charAt(0) == COMMAND_ACKNOWLEDGED);
    }

    /**
     * finalize(): Destructor of this object
     *
     * @throws Throwable throws stuff
     */
    @Override
    protected void finalize() throws Throwable {
        // close the serial port
        serialPort.closePort();

        // finish routine
        super.finalize();
    }
}
