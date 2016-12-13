package cells.coordinates;

/**
 * The <code>Coords2D</code> class represents cell position in a two-dimensional automaton.
 */
public class Coords2D implements CellCoordinates {
    /**
     * Sole constructor of this class creating cell with given coordinates
     * @param x cell's x coordinate
     * @param y cell's y coordinate
     */
    public Coords2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the X coordinate of the cell.
     *
     * @return the x coordinate of the cell which is its position in a row of cells
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Y coordinate of the cell.
     *
     * @return the y coordinate of the cell which is its position in a column of cells
     */
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

    private int x;
    private int y;
}
