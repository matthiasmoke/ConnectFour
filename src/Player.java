import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a player in the game
 */
public class Player implements Cloneable{

    //private Color checkerColor
    private char symbol;
    private boolean isMachine;
    private boolean isWinner;
    private Collection<Coordinates2D> witness = new ArrayList<>(4);

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

    /**
     *{@inheritDoc}
     */
    @Override
    public Player clone() {
        Player clone;

        try {
            clone = (Player) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error(ex);
        }

        Collection<Coordinates2D> witnessCopy = new ArrayList<>(4);
        for (Coordinates2D coordinates : witness) {
            witnessCopy.add(coordinates);
        }
        clone.witness = witnessCopy;
        return clone;
    }
}
