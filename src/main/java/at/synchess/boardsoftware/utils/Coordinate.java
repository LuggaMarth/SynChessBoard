package at.synchess.boardsoftware.utils;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate() {
        this(0,0);
    }
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCoordinates(int x, int y) {
        setX(x);
        setY(y);
    }
}
