package at.synchess.boardsoftware.driver.connection;

import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public interface IDriverConnection {
    void open() throws SerialPortException;
    void close() throws SerialPortException;
    void write(String data) throws SerialPortException;
    String read(int bytecount, int timeout) throws SerialPortTimeoutException, SerialPortException;
}
