import java.util.*;

/**
 * Class to represent the game-board
 */
public class ConnectFour implements Board, Cloneable {

    private Checker[][] currBoard = new Checker[ROWS][COLS];
    private GroupManager groups;
    private Player[] players = new Player[2];
    private Player currentPlayer;
    private int boardValue;
    private int level = 4;
    private boolean gameOver = false;
    private ConnectFour[] gameTree = new ConnectFour[7];
    int p;
    int q;

    /**
     * Default constructor for game.
     * Automatically sets players.
     */
    public ConnectFour() {
        players[0] = new Player('X');
        currentPlayer = players[0];
        players[1] = new Player('O', true);
        groups = new GroupManager(players[0], players[1]);
    }


    /**
     * Constructor for two players
     *
     * @param player1 Beginning player.
     * @param player2 Second player.
     */
    public ConnectFour(Player player1, Player player2) {
        players[0] = player1;
        currentPlayer = players[0];
        players[1] = player2;
        groups = new GroupManager(players[0], players[1]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getFirstPlayer() {
        return players[0];
    }

    /**
     * {@inheritDoc}
     */
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
                groupSearch(newChecker); //check groups for new checker
                return newBoard;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Board machineMove() {

        // switch current player to machine
        switchPlayer(true);
        gameTree = generateGameTree(this, level);
        calculateValues(gameTree, level);

        int largest = 0;
        for (int i = COLS - 1; i >= 0; i--) {
            if (gameTree[largest].boardValue <= gameTree[i].boardValue) {
                largest = i;
            }
        }

        ConnectFour machineMove = (ConnectFour) move(largest + 1);
        gameTree = null;
        //switch current player to human
        machineMove.switchPlayer(false);

        return machineMove;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getWinner() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Coordinates2D> getWitness() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

        //deep copy groups
        copy.groups = groups.clone();
        return copy;
    }

    /**
     * {@inheritDoc}
     */
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
        boolean machineDraw;
        for (int i = 0; i < depth; i++) {
            //detect if generated move is made by machine or human
            machineDraw = isMachineDraw(depth);
            current.switchPlayer(machineDraw);
            for (int col = 0; col < COLS; col++) {
                ConnectFour newBoard = (ConnectFour) current.move(col + 1);
                gameTree[col] = newBoard;
                gameTree[col].gameTree = generateGameTree(gameTree[col],
                        depth - 1);
            }
        }
        return gameTree;
    }

    private boolean isMachineDraw(int depth) {
        if (getFirstPlayer().isMachine()) {
            return depth % 2 == 0;
        } else {
            return depth % 2 == 1;
        }
    }

    private void calculateValues(ConnectFour[] currentGameTree, int depth) {
        if (depth > 0) {
            boolean machineDraw = isMachineDraw(depth);
            for (int i = 0; i < COLS; i++) {
                calculateValues(currentGameTree[i].gameTree, depth - 1);
                currentGameTree[i].calculateBoardValue(machineDraw);
            }
        }
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
        if (isValidNeighbour(underneath, checker)) {
            surrounding.add(underneath);
        }

        // add checker above if possible
        if (isValidNeighbour(above, checker)) {
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
        if (isValidNeighbour(left, checker)) {
            surrounding.add(left);
        }

        // add right checker if possible
        if (isValidNeighbour(right, checker)) {
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
                (actRow - 1, actCol - 1);

        // if possible get checker from:
        // -> right top
        if (isValidNeighbour(topRight, checker)) {
            surrounding.add(topRight);
        }

        // -> left bottom
        if (isValidNeighbour(bottomLeft, checker)) {
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
        if (isValidNeighbour(topLeft, checker)) {
            surrounding.add(topLeft);
        }

        // -> bottom right
        if (isValidNeighbour(bottomRight, checker)) {
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
    private boolean isValidNeighbour(Coordinates2D position, Checker checker) {
        int row = position.getRow();
        int col = position.getColumn();

        return (row < currBoard.length
                && col < currBoard[0].length
                && row >= 0 && col >= 0
                && currBoard[row][col] != null
                && currBoard[row][col].getOwner().equals(checker.getOwner()));
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
        for (int i = 1; i < COLS - 1; i++) {

            int checkersP1 = 0; //Number of checkers in column for player 1
            int checkersP2 = 0;
            for (int row = 0; row < ROWS; row++) {
                Checker currChecker = currBoard[row][i];
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

            int multiplicator = i;

            if (i == 4) {
                multiplicator = 2;
            }

            if (i == 5) {
                multiplicator = 1;
            }

            valueP1 += multiplicator * checkersP1;
            valueP2 += multiplicator * checkersP2;
        }

        //detect which player is the bot
        if (players[0].isMachine()) {
            return valueP1 - valueP2;
        } else {
            return valueP2 - valueP1;
        }
    }

    private void calculateBoardValue(boolean addMaximumToValue) {
        boardValue = getCheckerValue() + groups.calculateValue();

        if (groups.isBotWinPossible()) {
            boardValue += 500000;
        }

        if (!isGameTreeNull()) {
            Comparator<ConnectFour> comparator
                    = (v1, v2) -> Integer.compare(v1.boardValue, v2.boardValue);
            ConnectFour maxMin;

            if (addMaximumToValue) {
                // if machine move get node with highest board value of tree
                maxMin = Arrays.stream(gameTree).max(comparator).get();
            } else {
                //else with lowest (human move)
                maxMin = Arrays.stream(gameTree).min(comparator).get();
            }

            // add value to current board
            if (maxMin != null) {
                boardValue += maxMin.boardValue;
            }
        }
    }

    private boolean isGameTreeNull() {
        for (int i = 0; i < gameTree.length; i++) {
            if (gameTree[i] != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Switches between players
     * Requirement for this method
     * @param toMachine
     */
    private void switchPlayer(boolean toMachine) {

        if (players.length > 0) {
            Player machine;
            Player human;

            if (players[0].isMachine()) {
                machine = players[0];
                human = players[1];
            } else {
                machine = players[1];
                human = players[0];
            }

            if (toMachine) {
                currentPlayer = machine;
            } else {
                currentPlayer = human;
            }
        }
    }

}
