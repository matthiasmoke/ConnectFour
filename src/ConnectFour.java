import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ConnectFour implements Board {

    private Checker[][] currBoard = new Checker[ROWS][COLS];
    private List<Group> groups2Player1 = new LinkedList<>();
    private List<Group> groups3Player1 = new LinkedList<>();
    private List<Group> groups2Player2 = new LinkedList<>();
    private List<Group> groups3Player2 = new LinkedList<>();

    private Player[] players = new Player[2];
    private int boardValue = 50;
    private int level;
    private boolean botGame;

    private List<ConnectFour> gameTree = new LinkedList<>();

    public ConnectFour(Player beginner) {
        players[0] = beginner;
        players[1] = new Player('O');
        botGame = true;
    }

    public ConnectFour(Player player1, Player player2) {
        players[0] = player1;
        players[1] = player2;
        botGame = false;
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

        int column = col - 1;

        if (currBoard[0][column] == null) {
            ConnectFour b = (ConnectFour) this.clone();

            for (int i = 0; i < ROWS; i++) {
                if (currBoard[i][column] == null) {
                    b.currBoard[i][column]
                            = new Checker(new Coordinates2D(column, i),
                            players[0].getSymbol());

                    gameTree.add(b);
                    currBoard = b.currBoard;
                    return b;
                }
            }
        }
        return null;
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

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        for (int row = ROWS - 1; row >= 0; row--) {
            for (int col = 0; col < COLS; col++) {

                Checker currSlot = currBoard[row][col];

                if (currSlot == null) {
                    b.append(".");
                } else {
                    b.append(currSlot.getSymbol());
                }
            }

            b.append("\n");
        }
        return b.toString();
    }

    private void groupSearch(Checker c) {

    }

    private void calculateValue() {

    }

    private boolean isInGroups(Checker c, List<Group> groups) {
        for (Group g : groups) {
            if (g.isInGroup(c)) {
                return true;
            }
        }
        return false;
    }
}
