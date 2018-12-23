import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to represent the game-board
 */
public class ConnectFour implements Board {

    private Checker[][] currBoard = new Checker[ROWS][COLS];
    private GroupManager groups;
    private Player[] players = new Player[2];
    private Player currentPlayer;
    private int boardValue = 50;
    private int level;
    private boolean botGame;

    int turns = 0; // for test purposes

    private List<ConnectFour> gameTree = new LinkedList<>();

    /**
     * Constructor for one player (botgame)
     */
    public ConnectFour() {
        players[0] = new Player('X');
        currentPlayer = players[0];
        players[1] = new Player('O');
        groups = new GroupManager(players[0], players[1]);
        botGame = true;
    }

    /**
     * Constructor for two players (multiplayer)
     *
     * @param player1 Beginning player.
     * @param player2 Second player.
     */
    public ConnectFour(Player player1, Player player2) {
        players[0] = player1;
        currentPlayer = players[0];
        players[1] = player2;
        groups = new GroupManager(players[0], players[1]);
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
        if (turns >= 1)
            switchPlayer();

        int column = col - 1;
        ConnectFour b = (ConnectFour) this.clone();

        for (int i = 0; i < ROWS; i++) {
            if (currBoard[i][column] == null) {
                b.currBoard[i][column]
                        = new Checker(new Coordinates2D(i, column),
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
        return null;
    }

    @Override
    public Player getSlot(int row, int col) {
        return null;
    }

    @Override
    public Board clone() {
        ConnectFour copy;
        try {
            copy = (ConnectFour) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error(ex);
        }

        //deep copy game board
        copy.currBoard = currBoard.clone();
        for (int i = 0; i < currBoard.length; i++) {
            copy.currBoard[i] = currBoard[i].clone();
            for (int j = 0; j < currBoard[i].length; j++) {
                if (currBoard[i][j] != null) {
                    copy.currBoard[i][j] = currBoard[i][j].clone();
                }
            }
        }

        //copy player array and group lists
        copy.players = players.clone();

        return copy;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        String newLine = "";

        for (int row = ROWS - 1; row >= 0; row--) {

            b.append(newLine);
            for (int col = 0; col < COLS; col++) {

                Checker currSlot = currBoard[row][col];

                if (currSlot == null) {
                    b.append(".");
                } else {
                    b.append(currSlot.getOwner().getSymbol());
                }
            }
            newLine = "\n";
        }
        return b.toString();
    }

    /**
     * Check if there are new groups for all group-types
     *
     * @param checker Checker to search groups for
     */
    public void groupSearch(Checker checker) {
        findDiagonalFallingMembers(checker);
        findDiagonalRisingMembers(checker);
        findHorizontalNeighbours(checker);
        findVerticalNeighbours(checker);
    }

    /**
     * Searches for the vertical neighbours of the given checker
     *
     * @param checker Checker for search.
     */
    private void findVerticalNeighbours(Checker checker) {

        List<Coordinates2D> surrounding = new ArrayList<>(2);
        int actRow = checker.getPosition().getRow();
        int actCol = checker.getPosition().getColumn();

        // calculate neighbour coordinates
        Coordinates2D underneath = new Coordinates2D(actRow - 1, actCol);
        Coordinates2D above = new Coordinates2D(actRow + 1, actCol);

        // add checker underneath if possible
        if (positionNotOutOfBounds(underneath)) {
            surrounding.add(underneath);
        }

        // add checker above if possible
        if (positionNotOutOfBounds(above)) {
            surrounding.add(above);
        }

        groups.check(checker, surrounding, GroupType.VERTICAL);
    }

    /**
     * Searches for the right and left neighbours of the given checker.
     *
     * @param checker Checker for search.
     */
    private void findHorizontalNeighbours(Checker checker) {

        List<Coordinates2D> surrounding = new ArrayList<>(2);
        int actRow = checker.getPosition().getRow();
        int actCol = checker.getPosition().getColumn();

        Coordinates2D left = new Coordinates2D
                (actRow, actCol - 1);

        Coordinates2D right = new Coordinates2D
                (actRow, actCol + 1);

        // add left checker if possible
        if (positionNotOutOfBounds(left)) {
            surrounding.add(left);
        }

        // add right checker if possible
        if (positionNotOutOfBounds(right)) {
            surrounding.add(right);
        }

        groups.check(checker, surrounding, GroupType.HORIZONTAL);
    }

    /**
     * Searches for the neighbours on a rising diagonal through the checker
     *
     * @param checker Checker for search.
     */
    private void findDiagonalRisingMembers(Checker checker) {

        List<Coordinates2D> surrounding = new ArrayList<>(2);
        int actRow = checker.getPosition().getRow();
        int actCol = checker.getPosition().getColumn();

        Coordinates2D topRight = new Coordinates2D
                (actRow + 1, actCol + 1);

        Coordinates2D bottomLeft = new Coordinates2D
                (actRow + 1, actCol - 1);

        // if possible get checker from:
        // -> right top
        if (positionNotOutOfBounds(topRight)) {
            surrounding.add(topRight);
        }

        // -> left bottom
        if (positionNotOutOfBounds(bottomLeft)) {
            surrounding.add(bottomLeft);
        }

        groups.check(checker, surrounding, GroupType.DIAGONALRISING);
    }

    /**
     * Searches for the neighbours on a falling diagonal through the checker
     *
     * @param checker Checker for search.
     */
    private void findDiagonalFallingMembers(Checker checker) {

        List<Coordinates2D> surrounding = new ArrayList<>(2);
        int actRow = checker.getPosition().getRow();
        int actCol = checker.getPosition().getColumn();

        Coordinates2D topLeft = new Coordinates2D
                (actRow + 1, actCol - 1);

        Coordinates2D bottomRight = new Coordinates2D
                (actRow - 1, actCol + 1);

        // if possible get checker from
        // -> top left
        if (positionNotOutOfBounds(topLeft)) {
            surrounding.add(topLeft);
        }

        // -> bottom right
        if (positionNotOutOfBounds(bottomRight)) {
            surrounding.add(bottomRight);
        }

        groups.check(checker, surrounding, GroupType.DIAGONALFALLING);
    }

    private boolean positionNotOutOfBounds(Coordinates2D position) {
        int row = position.getRow();
        int col = position.getColumn();

        return (row < currBoard.length
                && col < currBoard[0].length
                && row > 0 && col > 0);
    }

    /**
     * Calculates Q value by using the formula given in the task-specification.
     *
     * @return Q value for number of Checkers in board.
     */
    private int getCheckerValue() {
        int valueP1 = 0;
        int valueP2 = 0;

        //counting checkers of each player for each column
        for (int i = 1; i <= COLS; i++) {

            int checkersP1 = 0; //Number of checkers in column for player 1
            int checkersP2 = 0;
            for (int row = 0; row < ROWS; row++) {
                Checker currChecker = currBoard[row][i - 1];
                if (currChecker != null) {
                    Player owner = currChecker.getOwner();

                    //To whom does checker belong to?
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

    /**
     * Calculates Q value by using the formula given in the task-specification.
     *
     * @return P value for groups of each player.
     */
    private int getGroupValue() {
        return 0;
        //TODO
    }

    /**
     * Switches between players if the game is no botgame
     */
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
