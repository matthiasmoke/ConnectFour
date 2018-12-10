public class Checker {

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
}
