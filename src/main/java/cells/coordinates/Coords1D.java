package cells.coordinates;

/**
 * The <code>Coords1D</code> class represents cell position in a one-dimensional automaton.
 */
public class Coords1D implements CellCoordinates {
    /**
     * Sole constructor of this class creating cell with given coordinate.
     * @param x cell's coordinate - position in a row of cells
     */
    public Coords1D(int x) {
        this.x = x;
    }

    /**
     * Gets the only coordinate of the cell.
     * @return the coordinate representing position of the cell in the automaton
     * @see automatons.Automaton1Dim
     */
    public int getX() {
        return x;
    }

    @Override
    public int hashCode() {
        return x;
    }

    @Override
    public boolean equals(Object other) {
        Coords1D otherCoords1D = (Coords1D) other;
        if(this.getX() == otherCoords1D.getX())
            return true;
        else
            return false;
    }

    private int x;
}
