import java.util.Collection;

/**
 * Represents a player in the game
 */
public class Player {

    //private Color checkerColor
    private char symbol;
    private boolean isMachine;
    private boolean isWinner;
    private Collection<Coordinates2D> witness;

    /**
     * Creates a new human player.
     * @param symbol Symbol of the player.
     */
    public Player(char symbol) {
        this.symbol = symbol;
        this.isMachine = false;
    }

    /**
     * Creates a new human or machine Player.
     * @param symbol Symbol of the player.
     * @param isMachine Indicates if player is type of machine or not.
     */
    public Player(char symbol, boolean isMachine) {
        this.symbol = symbol;
        this.isMachine = isMachine;
    }

    /**
     * Gets the player-symbol.
     * @return Symbol of the player.
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Gets information about the player type.
     * @return True if player is type of machine, false if not.
     */
    public boolean isMachine() {
        return isMachine;
    }

    public void setWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWitness(Collection<Coordinates2D> witness) {
        this.witness = witness;
    }

    public Collection<Coordinates2D> getWitness() {
        return witness;
    }
}
