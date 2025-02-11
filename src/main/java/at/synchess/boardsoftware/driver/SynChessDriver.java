package at.synchess.boardsoftware.driver;

import at.synchess.boardsoftware.enums.StepDirection;
import at.synchess.boardsoftware.exceptions.CCLException;
import at.synchess.boardsoftware.utils.Coordinate;
import jssc.SerialPortException;

public class SynChessDriver {
    private final CCLAbstracter abstracter;
    private final Coordinate headPosition;

    public SynChessDriver(CCLAbstracter abstracter) {
        this.abstracter = abstracter;

        headPosition = new Coordinate();
    }

    public void moveFigure(int x1, int y1, int x2, int y2) throws CCLException {
        // TODO: go to x1, y1
        headPosition.setCoordinates(x1, y1);

        abstracter.magnetOn();
        moveFigurePathFinder(x1, y1, x2, y2);
        abstracter.magnetOff();

        headPosition.setCoordinates(x2, y2);
        // TODO: Home after like 3 times
    }

    private void moveFigurePathFinder(int x1, int y1, int x2, int y2) throws CCLException {
        // check which 'zone'
        // if is on the same line horizontally
        if(y1 == y2) {
            abstracter.stepUp(CCLAbstracter.HALF_FIELD_STP);
            abstracter.step((x1 < x2) ? StepDirection.RIGHT : StepDirection.LEFT, (Math.max(x1, x2) - Math.min(x1, x2)) * CCLAbstracter.FULL_FIELD_STP);
            abstracter.stepDown(CCLAbstracter.HALF_FIELD_STP);
        }

        // if is on the same line vertically
        if(x1 == x2) {
            abstracter.stepRight(CCLAbstracter.HALF_FIELD_STP);
            abstracter.step((y1 < y2) ? StepDirection.DOWN : StepDirection.UP, (Math.max(y1, y2) - Math.min(y1, y2)) * CCLAbstracter.FULL_FIELD_STP);
            abstracter.stepLeft(CCLAbstracter.HALF_FIELD_STP);
        }

        // if neither
        else {
            // left or right
            abstracter.stepUp(CCLAbstracter.HALF_FIELD_STP);
            abstracter.step((x1 < x2) ? StepDirection.RIGHT : StepDirection.LEFT, (Math.max(x1, x2) - Math.min(x1, x2) - 1) * CCLAbstracter.FULL_FIELD_STP + CCLAbstracter.HALF_FIELD_STP);

            if(y1 < y2) {
                abstracter.stepDown((y2 - y1) * CCLAbstracter.FULL_FIELD_STP + CCLAbstracter.HALF_FIELD_STP);
            } else {
                abstracter.stepUp((y1 - y2 - 1) * CCLAbstracter.FULL_FIELD_STP + CCLAbstracter.HALF_FIELD_STP);
            }

            abstracter.step((x1 < x2) ? StepDirection.RIGHT : StepDirection.LEFT, CCLAbstracter.HALF_FIELD_STP);
        }
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
            abstracter.home();
        } catch (CCLException e) {
            System.err.println("Yep, didn't work out pal :c");
        }
    }

    public void closeRoutine() throws SerialPortException {
        abstracter.getDriverConnection().close();
    }

    public CCLAbstracter getAbstracter() {
        return abstracter;
    }

    public void removeFigure(int targX, int targY) {
        //TODO: puts figure to the side
    }

    public void addFigure(int targX, int targY, int piece) {
        //TODO: Adds figure
    }
}
