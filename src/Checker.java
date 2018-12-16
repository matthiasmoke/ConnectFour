/**
 * Represents a checker in the game
 */
public class Checker implements Cloneable{

    private Player owner;
    private Coordinates2D position;

    public Checker(Coordinates2D position, Player owner) {
        this.position = position;
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public Coordinates2D getPosition() {
        return position;
    }

    @Override
    public Checker clone() {
        Checker copy;

        try {
            copy = (Checker) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error(ex);
        }
        return copy;
    }
}
