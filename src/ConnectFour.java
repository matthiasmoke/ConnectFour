import java.util.ArrayList;
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
    private Player currentPlayer;
    private int boardValue = 50;
    private int level;
    private boolean botGame;

    int turns = 0; // for test purposes

    private List<ConnectFour> gameTree = new LinkedList<>();

    public ConnectFour(Player beginner) {
        players[0] = beginner;
        currentPlayer = players[0];
        players[1] = new Player('O');
        botGame = true;
    }

    public ConnectFour(Player player1, Player player2) {
        players[0] = player1;
        currentPlayer = players[0];
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
        if(turns >= 1)
            switchPlayer();
        int column = col - 1;
        boolean test = currBoard[0][ROWS - 1] == null;

        ConnectFour b = (ConnectFour) this.clone();

        for (int i = 0; i < ROWS; i++) {
            if (currBoard[i][column] == null) {
                b.currBoard[i][column]
                        = new Checker(new Coordinates2D(column, i),
                        currentPlayer);

                gameTree.add(b);
                currBoard = b.currBoard;
                turns += 1;
                return b;
            }
        }

        turns += 1;
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
        return getSurrounding(new Coordinates2D(3,2));
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
                    b.append(currSlot.getOwner().getSymbol());
                }
            }

            b.append("\n");
        }
        return b.toString();
    }

    private void groupSearch(Checker c) {

    }

    public Collection<Coordinates2D> getSurrounding(Coordinates2D checker) {
        List<Coordinates2D> outPut = new ArrayList<>(8);
        int actRow = checker.getRow() + 1;
        int actCol = checker.getColumn() + 1;

        if (actRow > 1) {
            outPut.add(new Coordinates2D(actRow - 1, actCol));

        } else if (actRow < 6) {
            outPut.add(new Coordinates2D(actRow + 1, actCol));

        } else if (actCol > 1) {
            outPut.add(new Coordinates2D(actRow, actCol - 1));

        } else if (actCol < 7) {
            outPut.add((new Coordinates2D(actRow, actCol + 1)));

        } else if (actCol > 1 && actRow > 1) {
            outPut.add(new Coordinates2D(actRow - 1, actCol - 1));

        } else if (actCol < 7 && actRow < 6) {
            outPut.add(new Coordinates2D(actRow + 1, actCol + 1));

        } else if (actCol > 1 && actRow < 6) {
            outPut.add(new Coordinates2D(actRow + 1, actCol - 1));

        } else if (actCol < 7 && actRow > 1) {
            outPut.add(new Coordinates2D(actRow - 1, actCol - 1));
        }

        return outPut;
    }

    private void calculateValue() {

    }

    /**
     * Calculating Q value by using the formula given in the task-specification
     *
     * @return Q value for number of Checkers in board
     */
    public int getCheckerValue() {
        int valueP1 = 0;
        int valueP2 = 0;

        //counting checkers of each player for each column
        for (int i = 1; i <= COLS; i++) {

            int checkersP1 = 0; //Number of checkers in column for player 1
            int checkersP2 = 0;
            for (int row = 0; row < ROWS; row++) {
                Checker currChecker = currBoard[row][i - 1];
                if(currChecker != null) {
                    Player owner = currChecker.getOwner();

                    if (owner.equals(players[0])) {
                        checkersP1 += 1;
                    } else if (owner.equals(players[1])) {
                        checkersP2 += 1;
                    }
                }
            }
            valueP1 += i * checkersP1;
            valueP2 += i * checkersP2;
        }

        return valueP1 - valueP2;
    }

    private boolean isColNull(int column) {
        int col;
        if (column > 0 && column <= COLS) {
            return true;
        }

        col = column - 1;

        for (int i = 0; i < ROWS; i++) {
            if (currBoard[i][col] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean isInGroups(Checker c, List<Group> groups) {
        for (Group g : groups) {
            if (g.isInGroup(c)) {
                return true;
            }
        }
        return false;
    }

    private void switchPlayer() {
        if (!botGame) {
            if (currentPlayer.equals(players[0])) {
                currentPlayer = players[1];
            } else {
                currentPlayer = players[0];
            }
        }
    }
}
