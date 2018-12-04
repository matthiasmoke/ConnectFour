import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ConnectFour implements Board {

    private Player[] players = new Player[2];
    private Checker[][] currBoard = new Checker[ROWS][COLS];
    private int boardValue = 50;
    private int level;

    private List<Checker[][]> gameTree = new LinkedList<>();

    public ConnectFour(Player beginner) {
        players[0] = beginner;
    }

    @Override
    public Player getFirstPlayer() {
        return players[0];
    }

    @Override
    public Board move(int col) {
        if (col > COLS) {
            throw new IllegalArgumentException();
        }

        Board b = null;
        int column = col - 1;
        if (currBoard[0][column] == null) {
            b = this.clone();
            for (int i = currBoard[column].length; i >= 0; i--) {
                if (currBoard[i][column] == null) {
                    currBoard[i][column] = new Checker(players[0].getSymbol());
                }
            }
        }
        return b;
    }

    @Override
    public Board machineMove() {
        return null;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
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
        ConnectFour clone = new ConnectFour(players[0]);
        clone.boardValue = this.boardValue;
        clone.currBoard = this.currBoard;
        return clone;
    }
}
