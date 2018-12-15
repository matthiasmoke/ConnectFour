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

    public void groupSearch() {
        Collection<Checker> lol = getSurrounding(currBoard[2][1]);
        for (Checker che : lol) {
            System.out.println(che.getPosition().toString());
        }

        //TODO: implement groupsearch
    }

    private Collection<Checker> getSurrounding(Checker checker) {
        List<Checker> surrounding = new ArrayList<>(8);
        List<Checker> out = new ArrayList<>();
        char symbol = checker.getOwner().getSymbol();

        findDiagonalMembers(checker, surrounding);
        findHorizontalNeighbours(checker, surrounding);
        findVerticalNeighbours(checker, surrounding);

        for (Checker c : surrounding) {
            if (c != null) {
                if((c.getOwner().getSymbol() == symbol)) {
                    out.add(c);
                }
            }
        }

        return out;
    }

    /**
     * Searches for the vertical neighbours of the given checker
     *
     * @param c Checker for search.
     * @param surrounding List to add the results to.
     */
    private void findVerticalNeighbours
            (Checker c, List<Checker> surrounding) {

        int actRow = c.getPosition().getRow();
        int actCol = c.getPosition().getColumn();

        //add checker underneath if possible
        if (actRow > 0) {
            surrounding.add(currBoard[actRow - 1][actCol]);
        }

        //add checker above if possible
        if (actRow < ROWS - 1) {
            surrounding.add(currBoard[actRow + 1][actCol]);
        }
    }

    /**
     * Searches for the right and left neighbours of the given checker.
     *
     * @param c Checker for search.
     * @param surrounding List to add results to.
     */
    private void findHorizontalNeighbours
            (Checker c, List<Checker> surrounding) {

        int actRow = c.getPosition().getRow();
        int actCol = c.getPosition().getColumn();

        //add right checker if possible
        if (actCol > 0) {
            surrounding.add(currBoard[actRow][actCol - 1]);
        }

        //add right checker if possible
        if (actCol < COLS - 1) {
            surrounding.add(currBoard[actRow][actCol + 1]);
        }
    }

    /**
     * Searches for the neighbours diagonal around the given checker.
     *
     * @param c Checker for search.
     * @param surrounding List to add results to.
     */
    private void findDiagonalMembers
            (Checker c, List<Checker> surrounding) {

        int actRow = c.getPosition().getRow();
        int actCol = c.getPosition().getColumn();

        //if possible add checker from:
        // -> right bottom
        if (actCol > 0 && actRow > 0) {
            surrounding.add(currBoard[actRow - 1][actCol + 1]);
        }

        // -> right top
        if (actCol < COLS - 1 && actRow < COLS - 1) {
            surrounding.add(currBoard[actRow + 1][actCol + 1]);
        }

        // -> left top
        if (actCol > 0 && actRow < ROWS - 1) {
            surrounding.add(currBoard[actRow + 1][actCol - 1]);
        }

        // -> left bottom
        if (actCol < COLS - 1 && actRow > 0) {
            surrounding.add(currBoard[actRow - 1][actCol - 1]);
        }
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
