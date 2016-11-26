package cells.coordinates;

public class Coords1D implements CellCoordinates {
    public Coords1D(int x) {
        this.x = x;
    }

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
