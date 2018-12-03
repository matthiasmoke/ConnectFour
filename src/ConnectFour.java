import java.util.Collection;
import java.util.LinkedList;

public class ConnectFour implements Board {

    private Collection<Checker> gameHistory = new LinkedList<>();
    private Player[] players = new Player[2];

    public ConnectFour(Player beginner) {
        players[0] = beginner;
    }

    @Override
    public Player getFirstPlayer() {
        if (gameHistory.size() % 2 == 0) {
            return players[0];
        } else {
            return players[1];
        }
    }

    @Override
    public Board move(int col) {
        return null;
    }

    @Override
    public Board machineMove() {
        return null;
    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public Player getWinner() {
        return null;
    }

    @Override
    public Collection<Coordinates2D> getWitness() {
        return null;
    }

    @Override
    public Player getSlot(int row, int col) {
        return null;
    }

    @Override
    public Board clone() {
        return null;
    }
}
