package at.synchess.boardsoftware.driver;

import at.synchess.boardsoftware.enums.ChessBoardSector;
import at.synchess.boardsoftware.enums.StepDirection;
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


    // *********************** Move figure methods ***********************


    // ******************************************************************


    // *********************** Utility Commands ***********************




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
