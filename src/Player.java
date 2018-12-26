/**
 * Represents a player in the game
 */
public class Player {

    //private Color checkerColor
    private char symbol;
    private boolean isMachine;

    public Player(char symbol) {
        this.symbol = symbol;
        this.isMachine = false;
    }

    public Player(char symbol, boolean isMachine) {
        this.symbol = symbol;
        this.isMachine = isMachine;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isMachine() {
        return isMachine;
    }

}
