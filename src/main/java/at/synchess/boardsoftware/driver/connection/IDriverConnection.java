package at.synchess.boardsoftware.driver.connection;

import jssc.SerialPortException;

public interface IDriverConnection {
    void open() throws SerialPortException;
    void close() throws SerialPortException;
    void write(String data) throws SerialPortException;
    String readString() throws SerialPortException;
    void flushSerialPort();
}
