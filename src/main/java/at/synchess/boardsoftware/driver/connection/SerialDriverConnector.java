package at.synchess.boardsoftware.driver.connection;

import at.synchess.boardsoftware.driver.SynChessDriver;
import at.synchess.boardsoftware.exceptions.SynChessDriverException;
import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialDriverConnector implements IDriverConnection {
    private final String ARDUINO_PORT_NAME = "/dev/ttyUSB0";
    private SerialPort serialPort;

    public SerialDriverConnector() throws SynChessDriverException {
        open();
    }

    @Override
    public void open() throws SynChessDriverException {
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

            flush();
        } catch (SerialPortException e) {
            throw new SynChessDriverException(e.getMessage());
        }
    }

    /**
     * close(): closes the connection to the Arduino
     */
    @Override
    public void close() throws SynChessDriverException {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            throw new SynChessDriverException(e.getMessage());
        }
    }

    /**
     * write(): Executes raw commands on the hardware. Use this with caution due to
     * raw command handling!
     * @param data sends the given command directly to the serial port
     */
    @Override
    public void write(String data) throws SynChessDriverException {
        try {
            serialPort.writeString(data);
        } catch (SerialPortException e) {
            throw new SynChessDriverException(e.getMessage());
        }
    }

    @Override
    public String read() throws SynChessDriverException {
        try {
            return serialPort.readString();
        } catch (SerialPortException e) {
            throw new SynChessDriverException(e.getMessage());
        }
    }


    @Override
    public void flush() throws SynChessDriverException {
        try {
            while (serialPort.getInputBufferBytesCount() > 0) {
                serialPort.readBytes(serialPort.getInputBufferBytesCount());
            }
        } catch (SerialPortException e) {
            throw new SynChessDriverException(e.getMessage());
        }
    }

}
