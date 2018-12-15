public class Coordinates2D {

    private int posRow;
    private int posCol;

    public Coordinates2D(int col, int row) {
        posRow = row;
        posCol = col;
    }

    public int getRow() {
        return posRow;
    }

    public int getColumn() {
        return posCol;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", posCol, posRow);
    }
}
