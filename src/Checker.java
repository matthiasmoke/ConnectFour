public class Checker {

    private Player owner;
    private Coordinates2D position;

    public Checker(Coordinates2D postion, Player owner) {
        this.position = postion;
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public Coordinates2D getPosition() {
        return position;
    }
}
