import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class to represent the game-board
 */
public class ConnectFour implements Board, Cloneable {

    private Checker[][] currBoard = new Checker[ROWS][COLS];
    private GroupManager groups;
    private Player[] players = new Player[2];
    private Player currentPlayer;
    private int boardValue;
    private int level;
    private boolean gameOver = false;
    private ConnectFour[] gameTree = new ConnectFour[7];

    //private boolean botGame;

    /**
     * Default constructor for game
     */
    public ConnectFour() {
        players[0] = new Player('X');
        currentPlayer = players[0];
        players[1] = new Player('O', true);
        groups = new GroupManager(players[0], players[1]);

        //botGame = true;
    }


    /**
     * Constructor for two players (can be used for multiplayer in the future)
     *
     * @param player1 Beginning player.
     * @param player2 Second player.
     */
    public ConnectFour(Player player1, Player player2) {
        players[0] = player1;
        currentPlayer = players[0];
        players[1] = player2;
        groups = new GroupManager(players[0], players[1]);
        //botGame = false;
    }

    @Override
    public Player getFirstPlayer() {
        return players[0];
    }

    @Override
    public Board move(int col) {
        if (gameOver) {
            throw new IllegalMoveException();
        }

        if (col > COLS && col < 1) {
            throw new IllegalArgumentException();
        }

        int column = col - 1;
        ConnectFour newBoard = (ConnectFour) this.clone();

        for (int i = 0; i < ROWS; i++) {
            if (currBoard[i][column] == null) {
                Checker newChecker = new Checker(new Coordinates2D(i, column),
                        currentPlayer);

                newBoard.currBoard[i][column] = newChecker;
                newBoard.groupSearch(newChecker); //check groups for new checker
                newBoard.calculateBoardValue(); //calculate board-value for bot
                return newBoard;
            }
        }
        return null;
    }

    @Override
    public Board machineMove() {
        currentPlayer = players[1];
        gameTree = generateGameTree(this, level);
        currentPlayer = players[0];
        return null;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
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
        int arrayRow = row - 1;
        int arrayCol = col - 1;
        if (currBoard[arrayRow][arrayCol] != null) {
            return currBoard[arrayRow][arrayCol].getOwner();
        } else {
            return null;
        }
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

    private ConnectFour[] generateGameTree(ConnectFour current, int depth) {

        ConnectFour[] gameTree = new ConnectFour[7];
        for (int i = 0; i < depth; i++) {
            for (int col = 0; col < COLS; col++) {
                ConnectFour newBoard = (ConnectFour) current.move(col + 1);
                gameTree[col] = newBoard;
                gameTree[col].gameTree = generateGameTree(gameTree[col],
                        depth - 1);
            }
        }
        return gameTree;
    }

    /**
     * Check if there are new groups for all group-types
     *
     * @param checker Checker to search groups for
     */
    private void groupSearch(Checker checker) {
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
        if (isValidPosition(underneath)) {
            surrounding.add(underneath);
        }

        // add checker above if possible
        if (isValidPosition(above)) {
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
        if (isValidPosition(left)) {
            surrounding.add(left);
        }

        // add right checker if possible
        if (isValidPosition(right)) {
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
        if (isValidPosition(topRight)) {
            surrounding.add(topRight);
        }

        // -> left bottom
        if (isValidPosition(bottomLeft)) {
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
        if (isValidPosition(topLeft)) {
            surrounding.add(topLeft);
        }

        // -> bottom right
        if (isValidPosition(bottomRight)) {
            surrounding.add(bottomRight);
        }

        groups.check(checker, surrounding, GroupType.DIAGONALFALLING);
    }

    /**
     * Checks if given coordinates are on the game board and if the given
     * position is null
     *
     * @param position Position for Checker on the game board
     * @return true if position exists on the board and is not null
     */
    private boolean isValidPosition(Coordinates2D position) {
        int row = position.getRow();
        int col = position.getColumn();

        return (row < currBoard.length
                && col < currBoard[0].length
                && row > 0 && col > 0
                && currBoard[row][col] != null);
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

        //detect which player is the bot
        if (players[0].isMachine()) {
            return valueP1 - valueP2;
        } else {
            return valueP2 - valueP1;
        }
    }

    public void calculateValues() {
        int q = getCheckerValue();
        int p = groups.calculateValue();
        System.out.println(q);
        System.out.println(p);
    }

    private void calculateBoardValue() {
        boardValue = getCheckerValue() + groups.calculateValue();

        if(groups.isBotWinPossible()) {
            boardValue += 500000;
        }
    }


    /*
    /**
     * Switches between players if the game is no botgame
     *
    private void switchPlayer() {
        if (!botGame) {
            if (currentPlayer.equals(players[0])) {
                currentPlayer = players[1];
            } else {
                currentPlayer = players[0];
            }
        }
    }
    */
}
