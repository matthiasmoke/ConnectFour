public class Checker {

    private char symbol;
    private Coordinates2D position;

    public Checker(Coordinates2D postion, char symbol) {
        this.position = postion;
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public Coordinates2D getPosition() {
        return position;
    }
}
