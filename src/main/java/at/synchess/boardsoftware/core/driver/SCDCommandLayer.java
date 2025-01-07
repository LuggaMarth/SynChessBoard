package at.synchess.boardsoftware.core.driver;

import at.synchess.boardsoftware.exceptions.SynChessCoreException;

/**
 * SCDCommandLayer(): Abstracts the driver connection layer and calls the commands
 * on that one.
 *
 * @author Luca Marth
 */
public class SCDCommandLayer {
    private final SCDCore core;

    // constructor
    public SCDCommandLayer() {
        this.core = new SCDCore();
    }

    // getter
    public SCDCore getCore() {
        return core;
    }

    /**
     * moveFromTo(): Moves a chess figure from (X1 | Y1) to (X2 | Y2)
     * @param x1 X - Coordinate Point 1
     * @param y1 Y - Coordinate Point 1
     * @param x2 X - Coordinate Point 2
     * @param y2 Y - Coordinate Point 2
     */
    public void moveFromTo(int x1, int y1, int x2, int y2) throws SynChessCoreException {
        // check which 'zone'
        // if is on the same line horizontally
        if(y1 == y2) {
            core.step(StepDirection.UP, SCDCore.HALF_FIELD_STP);
            core.step((x1 < x2) ? StepDirection.RIGHT : StepDirection.LEFT, (Math.max(x1, x2) - Math.min(x1, x2)) * SCDCore.FULL_FIELD_STP);
            core.step(StepDirection.DOWN, SCDCore.HALF_FIELD_STP);
        }

        // if is on the same line vertically
        if(x1 == x2) {
            core.step(StepDirection.RIGHT, SCDCore.HALF_FIELD_STP);
            core.step((y1 < y2) ? StepDirection.DOWN : StepDirection.UP, (Math.max(y1, y2) - Math.min(y1, y2)) * SCDCore.FULL_FIELD_STP);
            core.step(StepDirection.LEFT, SCDCore.HALF_FIELD_STP);
        }

        // if neither
        else {
            // left or right
            core.step(StepDirection.UP, SCDCore.HALF_FIELD_STP);
            core.step((x1 < x2) ? StepDirection.RIGHT : StepDirection.LEFT, (Math.max(x1, x2) - Math.min(x1, x2) - 1) * SCDCore.FULL_FIELD_STP + SCDCore.HALF_FIELD_STP);

            if(y1 < y2) {
                core.step(StepDirection.DOWN, (y2 - y1) * SCDCore.FULL_FIELD_STP + SCDCore.HALF_FIELD_STP);
            } else {
                core.step(StepDirection.UP, (y1 - y2 - 1) * SCDCore.FULL_FIELD_STP + SCDCore.HALF_FIELD_STP);
            }

            core.step((x1 < x2) ? StepDirection.RIGHT : StepDirection.LEFT, SCDCore.HALF_FIELD_STP);
        }
    }

    /**
     * readChessBoard(): Reads the values of the chessboard and returns it as a
     * char array.
     * @return field read in chess board
     */
    public char[] readChessBoard() {
        char[] field;

        try {
            field = getCore().readBoard();
        } catch (SynChessCoreException e) {
            field = null;
        }

        return field;
    }

    /**
     * moveOutFrom(): Moves a chess figure from Point (x|y) out to the next available spot in the
     * reservoir.
     * @param x X - Coordinate
     * @param y Y - Coordinate
     */
    public void moveOutFrom(int x, int y, int piece) {
        if(piece % 2 == 0) {
            // black
        } else {
            // white
        }
    }

    /**
     * home(): Moves the motors back to 0/0
     */
    public void home() {
        try {
            getCore().home();
        } catch (SynChessCoreException e) {
            System.err.println("Yep, didn't work out pal :c");
        }
    }
}
