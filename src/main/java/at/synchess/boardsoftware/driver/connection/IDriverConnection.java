package at.synchess.boardsoftware.driver.connection;

import at.synchess.boardsoftware.exceptions.SynChessDriverException;

public interface IDriverConnection {
    void open() throws SynChessDriverException;
    void close() throws SynChessDriverException;
    void write(String data) throws SynChessDriverException;
    String read() throws SynChessDriverException;
    void flush() throws SynChessDriverException;
}
