package at.synchess.boardsoftware.driver.connection;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class SerialDriverConnector implements IDriverConnection {
    private static final String ARDUINO_PORT_NAME = "/dev/ttyACM0";
    private SerialPort serialPort;

    public SerialDriverConnector() {
        try {
            open();
        } catch (SerialPortException e) {
            // TODO: Kiks exception handling
            e.printStackTrace();
        }
    }

    @Override
    public void open() throws SerialPortException {
        serialPort = new SerialPort(ARDUINO_PORT_NAME);

        // open the serial port
        serialPort.openPort();

        // set parameters of the serial port
        serialPort.setParams(
                SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE
        );
    }

    /**
     * close(): closes the connection to the Arduino
     * @throws SerialPortException throws stuff
     */
    @Override
    public void close() throws SerialPortException {
        serialPort.closePort();
    }

    /**
     * write(): Executes raw commands on the hardware. Use this with caution due to
     * raw command handling!
     * @param data sends the given command directly to the serial port
     */
    @Override
    public void write(String data) throws SerialPortException {
        serialPort.writeString(data);
    }

    @Override
    public String read(int bytecount, int timeout) throws SerialPortTimeoutException, SerialPortException {
        return serialPort.readString(bytecount, timeout);
    }
}
