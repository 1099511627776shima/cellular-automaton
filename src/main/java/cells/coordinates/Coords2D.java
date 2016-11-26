package cells.coordinates;

public class Coords2D implements CellCoordinates {
    public Coords2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public boolean equals(Object other) {
        Coords2D otherCoords2D = (Coords2D) other;
        if(this.getX() == otherCoords2D.getX() && this.getY() == otherCoords2D.getY())
            return true;
        else
            return false;

    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    private int x;
    private int y;
}
