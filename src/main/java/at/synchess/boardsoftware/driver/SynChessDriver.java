package at.synchess.boardsoftware.driver;

import at.synchess.boardsoftware.driver.connection.IDriverConnection;
import at.synchess.boardsoftware.enums.ChessBoardSector;
import at.synchess.boardsoftware.enums.StepDirection;
import at.synchess.boardsoftware.exceptions.CCLException;
import jssc.SerialPortException;

public class SynChessDriver {
    private final CCLAbstracter abstracter;
    private final IDriverConnection connection;

    public SynChessDriver(CCLAbstracter abstracter, IDriverConnection connection) {
        this.abstracter = abstracter;
        this.connection = connection;
    }

    //---------------------------------- Move Figure ----------------------------------//
    /**
     * moveFigure(): Moves a figure from (x1, y1) to (x2, y2).
     * @param x1 X1-Coordinate
     * @param y1 Y1-Coordinate
     * @param x2 X2-Coordinate
     * @param y2 Y2-Coordinate
     * @throws CCLException if command could not be executed
     */
    public void movePiece(int x1, int y1, int x2, int y2) throws CCLException {
        String command = abstracter.magnetOff() +
                abstracter.home() +
                abstracter.stepDown(CCLAbstracter.STEPS_TO_FIRST_FIELD_Y + y1 * CCLAbstracter.FULL_FIELD_STP) + // going to y1
                abstracter.stepRight(CCLAbstracter.STEPS_TO_FIRST_FIELD_X + x1 * CCLAbstracter.FULL_FIELD_STP) + // going to x1
                abstracter.magnetOn() +
                abstracter.wait(300) + // just wait, so that the magnet has a grip
                movePiecePathFinder(x1, y1, x2, y2) +
                abstracter.magnetOff();

        System.out.println(command);
        //executeSplitCommand(command, 60);
    }

    /**
     * moveFigurePathFinder(): Calculates the easiest path between two points
     * @param x1 X1-Coordinate
     * @param y1 Y1-Coordinate
     * @param x2 X2-Coordinate
     * @param y2 Y2-Coordinate
     * @return step command sequence to the best path
     */
    private String movePiecePathFinder(int x1, int y1, int x2, int y2) {
        StringBuilder endCommand = new StringBuilder();

        // check which 'zone'
        // if is on the same line horizontally
        if (y1 == y2) {
            endCommand.append(abstracter.stepUp(CCLAbstracter.HALF_FIELD_STP));
            endCommand.append(abstracter.step((x1 < x2) ? StepDirection.RIGHT : StepDirection.LEFT, (Math.max(x1, x2) - Math.min(x1, x2)) * CCLAbstracter.FULL_FIELD_STP));
            endCommand.append(abstracter.stepDown(CCLAbstracter.HALF_FIELD_STP));
        }

        // if is on the same line vertically
        if (x1 == x2) {
            endCommand.append(abstracter.stepRight(CCLAbstracter.HALF_FIELD_STP));
            endCommand.append(abstracter.step((y1 < y2) ? StepDirection.DOWN : StepDirection.UP, (Math.max(y1, y2) - Math.min(y1, y2)) * CCLAbstracter.FULL_FIELD_STP));
            endCommand.append(abstracter.stepLeft(CCLAbstracter.HALF_FIELD_STP));
        }

        // if neither
        else {
            // left or right
            endCommand.append(abstracter.stepUp(CCLAbstracter.HALF_FIELD_STP));
            endCommand.append(abstracter.step((x1 < x2) ? StepDirection.RIGHT : StepDirection.LEFT, (Math.max(x1, x2) - Math.min(x1, x2) - 1) * CCLAbstracter.FULL_FIELD_STP + CCLAbstracter.HALF_FIELD_STP));

            if (y1 < y2) {
                endCommand.append(abstracter.stepDown((y2 - y1) * CCLAbstracter.FULL_FIELD_STP + CCLAbstracter.HALF_FIELD_STP));
            } else {
                endCommand.append(abstracter.stepUp((y1 - y2 - 1) * CCLAbstracter.FULL_FIELD_STP + CCLAbstracter.HALF_FIELD_STP));
            }

            endCommand.append(abstracter.step((x1 < x2) ? StepDirection.RIGHT : StepDirection.LEFT, CCLAbstracter.HALF_FIELD_STP));
        }

        return endCommand.toString();
    }

    /**
     * removePiece(): Moves a chess figure from Point (x|y) out to the next available spot in the
     * reservoir.
     *
     * @param x X - Coordinate
     * @param y Y - Coordinate
     */
    public void removePiece(int x, int y, ChessBoardSector sector) throws CCLException {
        /*char[] scannedField = scan(sector);
        int firstFreeIndex = -1, index = 0;

        // check which index is the first free one
        while(firstFreeIndex == -1 && index < scannedField.length) {
            if(scannedField[index] != '0') firstFreeIndex = index;

            index++;
        }
        */
        int firstFreeIndex = 7;
        int yfin = firstFreeIndex / 2;

        // build move command
        String command = abstracter.magnetOff() +
                abstracter.home() +
                abstracter.stepDown(CCLAbstracter.STEPS_TO_FIRST_FIELD_Y + y * CCLAbstracter.FULL_FIELD_STP) + // going to y1
                abstracter.stepRight(CCLAbstracter.STEPS_TO_FIRST_FIELD_X + x * CCLAbstracter.FULL_FIELD_STP) + // going to x1
                abstracter.magnetOn() +
                abstracter.wait(300) +
                abstracter.stepLeft(CCLAbstracter.HALF_FIELD_STP) + // take it out of the field
                abstracter.stepUp(y * CCLAbstracter.FULL_FIELD_STP + CCLAbstracter.HALF_FIELD_STP) + // step up to the first field
                abstracter.stepLeft(x * CCLAbstracter.FULL_FIELD_STP) +
                ((sector == ChessBoardSector.OUT_WHITE) ? (abstracter.stepRight(0)) : (abstracter.stepLeft(210+2*CCLAbstracter.FULL_FIELD_STP))) + // which sector to go to TODO Distanz zum ersten feld vom jeweiligen out
                ((firstFreeIndex % 2 != 0) ? (abstracter.stepRight(CCLAbstracter.FULL_FIELD_STP)) : "") + // if typ is on right, then go right
                abstracter.stepDown(yfin * CCLAbstracter.FULL_FIELD_STP + CCLAbstracter.HALF_FIELD_STP) +
                abstracter.stepRight(CCLAbstracter.HALF_FIELD_STP) +
                abstracter.magnetOff();

        System.out.println(command);
        //executeSplitCommand(command, 80);
    }

    /**
     * revivePiece(): Gets the desired piece from the specified sector and puts it to targX and targY.
     * @param x From where X
     * @param y From where Y
     * @param piece which piece
     */
    public void revivePiece(int x, int y, char piece, ChessBoardSector sector) throws CCLException {
        // remove old piece
        removePiece(x, y, sector);

        // retrieve new piece
        char[] scannedField = scan(sector);
        int indexOfPiece = -1, index = 0;

        // check where the piece is located
        while(indexOfPiece == -1 && index < scannedField.length) {
            if(scannedField[index] == piece) indexOfPiece = index;
            index++;
        }

        // if piece could not be found
        if(indexOfPiece == -1) throw new CCLException("Piece not in out!");

        // build command
        String command = abstracter.magnetOff();

        // TODO: Build the command

        // send of to arduino
        System.out.println(command);
        //executeSplitCommand(command, 60);
    }
    //---------------------------------------------------------------------------------//


    //---------------------------------- Scan Board ----------------------------------//

    /**
     * scan(): Returns a char array based on the scanned chessboard. Depending on the chess sector
     * either length = 32 or 64.
     *
     * @param sector Sector to be read
     * @return char array
     */
    public char[] scan(ChessBoardSector sector) throws CCLException {
        String command = "";
        char[] returnVal;

        switch (sector) {
            case OUT_BLACK -> command = abstracter.readOutBlack();
            case CENTER_BOARD -> command = abstracter.readCenter();
            case OUT_WHITE -> command = abstracter.readOutWhite();
        }

        String ret = sendToArduinoBlocking(abstracter.home() + command, 80);
        returnVal = ret.toCharArray();
        return returnVal;
    }
    //--------------------------------------------------------------------------------//



    //---------------------------------- Send to Arduino ----------------------------------//
    /**
     * sendToArduinoBlocking(): Sends the given command to the Arduino.
     *
     * @param command Command
     * @param timeout Timeout
     * @return read string
     * @throws CCLException no
     */
    private String sendToArduinoBlocking(String command, int timeout) throws CCLException {
        try {
            long startTime = System.currentTimeMillis();
            String retval = null;

            connection.write(command);
            connection.flushSerialPort();

            // read (*1000 for milliseconds)
            while (((System.currentTimeMillis() - startTime) < (timeout * 1000L)) && (retval == null || retval.isEmpty())) {
                retval = connection.readString();
            }

            if (retval == null) {
                throw new CCLException("Timed out!");
            }

            return retval;
        } catch (SerialPortException e) {
            throw new CCLException(e.getMessage());
        }
    }

    /**
     * executeSplitCommand(): Executes raw commands on the hardware. Use this with caution due to
     * raw command handling!
     *
     * @param command sends the given command directly to the serial port
     */
    public String executeSplitCommand(String command, int timeout) throws CCLException {
        String returnValue = "";
        String[] commandsToSend;

        // split command
        if (command.length() > 50) {
            commandsToSend = command.split(";");

            for (int i = 0; i < commandsToSend.length / 10 + 1; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = i * 10; j < 10 + i * 10; j++) {
                    if (j < commandsToSend.length) {
                        sb.append(commandsToSend[j]);
                        sb.append(";");
                    }
                }

                sendToArduinoBlocking(sb.toString(), timeout);
            }
        } else {
            // write command
            returnValue = sendToArduinoBlocking(command, timeout);
        }

        return returnValue;
    }
    //-------------------------------------------------------------------------------------//


    //---------------------------------- Util Commands ----------------------------------//

    /**
     * home(): Moves the motors back to 0/0
     */
    public void home() throws CCLException {
        sendToArduinoBlocking(abstracter.home(), 20);
    }

    /**
     * closeRoutine(): Gets called when the program is shut down
     *
     * @throws SerialPortException if port couldn't be closed
     */
    public void closeRoutine() throws SerialPortException {
        connection.close();
    }
    //-----------------------------------------------------------------------------------//
}
