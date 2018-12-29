import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Shell class as user interface
 */
public final class Shell {

    private static final short ERR_NO_VALID_INPUT = 0;
    private static final short ERR_NOT_INITIALIZED = 1;
    private static final short ERR_ACTION_NOT_POSSIBLE = 2;
    private static final String MSG_VICTORY = "Congratulations! You won.";
    private static final String MSG_DEFEAT = "Sorry! Machine wins";
    private static final String[] COMMANDS =
            {
                    "new", "level", "switch", "move", "witness", "print",
                    "help", "quit"
            };
    private static boolean run;
    private static Board game;
    private static int level = 0;
    private static boolean levelIsSet = false;

    private Shell() {

    }

    /**
     * Main method that starts the shell.
     *
     * @param args
     * @throws IOException Occurs on wrong usage.
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader
                = new BufferedReader(new InputStreamReader(System.in));
        runShell(reader);

    }

    /**
     * Runs the shell-loop.
     *
     * @param reader For processing user input.
     * @throws IOException can occur
     */
    private static void runShell(BufferedReader reader) throws IOException {
        run = true;

        while (run) {
            System.out.print("4inarow> ");
            String input = reader.readLine();

            if (input != null) {
                evalInput(input.toLowerCase());
            }
        }
    }

    /**
     * Evals the input.
     *
     * @param input Input user has given.
     */
    private static void evalInput(String input) {
        Scanner sc = new Scanner(input);
        String command = "";

        if (sc.hasNext()) {
            command = sc.next();
            switch (command.charAt(0)) {
                case 'n':
                    createNewGame(new Player('X'),
                            new Player('O', true));
                    break;

                case 'l':
                    level = getNextInt(sc);
                    setLevel(level);
                    break;

                case 's':
                    switchBeginner();
                    break;

                case 'm':
                    move(getNextInt(sc));
                    break;

                case 'w':
                    witness();
                    break;

                case 'p':
                    print();
                    break;

                case 'h':
                    printHelpMessage();
                    break;

                case 'q':
                    run = false;
                    break;

                default:
                    handleError(ERR_NO_VALID_INPUT);
            }
        }
        sc.close();
    }

    /**
     * Creates a new game. Takes over level from old game.
     *
     * @param player1 Player one (beginning player)
     * @param player2 Player two
     */
    private static void createNewGame(Player player1, Player player2) {
        game = new ConnectFour(player1, player2);
        if (levelIsSet) {
            setLevel(level);
        }
        if (game.getFirstPlayer().isMachine()) {
            game = game.machineMove();
        }
    }

    /**
     * Switches beginner.
     */
    private static void switchBeginner() {
        Player p1 = new Player('O', true);
        Player p2 = new Player('X');
        createNewGame(p1, p2);
    }

    /**
     * Prints coordinates of winning group.
     */
    private static void witness() {
        if (initiated()) {
            if (game.isGameOver()) {
                for (Coordinates2D c : game.getWitness()) {
                    System.out.println(c.toString());
                }
            } else {
                handleError(ERR_ACTION_NOT_POSSIBLE);
            }
        } else {
            handleError(ERR_NOT_INITIALIZED);
        }
    }

    /**
     * Prints a help message for the user.
     */
    private static void printHelpMessage() {
        StringBuilder b = new StringBuilder();

        b.append("--Following commands can be performed--\n");
        b.append(COMMANDS[0]).append(":\t\t Starts a new game.\n");
        b.append(COMMANDS[1]).append(":\t\t Sets the game-difficulty.\n");
        b.append("\t\t\t You can choose from 1 to 5. Default value is 4.\n");
        b.append(COMMANDS[2]).append(":\t\t Switches the beginner.\n");
        b.append(COMMANDS[3]).append(" c:\t\t Moves a checker for the current");
        b.append(" player to column c.\n");
        b.append(COMMANDS[4]).append(":\t Shows the witness of the player.\n");
        b.append(COMMANDS[5]).append(":\t\t Prints the current game board.\n");
        b.append(COMMANDS[6]).append(":\t\t Prints help info.\n");
        b.append(COMMANDS[7]).append(":\t\t Quits the game");

        System.out.println(b.toString());
    }

    /**
     * Prints the current game board.
     */
    private static void print() {
        if (initiated()) {
            System.out.println(game);
        } else {
            handleError(ERR_NOT_INITIALIZED);
        }
    }

    /**
     * Performs player move if possible.
     *
     * @param column Column to put Checker in.
     */
    private static void move(int column) {
        if (initiated()) {
            if (column > 0 && column < 8) {
                Board playerMove = game.move(column);
                if (playerMove != null) {
                    game = playerMove;
                    game = game.machineMove();
                } else {
                    handleError(ERR_ACTION_NOT_POSSIBLE);
                }
            } else {
                handleError(ERR_NO_VALID_INPUT);
            }
        } else {
            handleError(ERR_NOT_INITIALIZED);
        }
    }

    /**
     * Sets game level.
     *
     * @param level Level for game (values from 1 to 5 possible).
     */
    private static void setLevel(int level) {
        if (initiated()) {
            if (level > 0 && level < 6) {
                game.setLevel(level);
                levelIsSet = true;
            } else {
                handleError(ERR_NO_VALID_INPUT);
            }
        } else {
            handleError(ERR_NOT_INITIALIZED);
        }
    }

    /**
     * Checks if game is initiated.
     *
     * @return true if game is running.
     */
    private static boolean initiated() {
        return game != null;
    }

    /**
     * Checks if Scanner has next int and returns it.
     *
     * @param sc Scanner to check.
     * @return Next int of Scanner or 0, if Scanner has no nextInt.
     */
    private static int getNextInt(Scanner sc) {
        if (sc.hasNext()) {
            return sc.nextInt();
        } else {
            return 0;
        }
    }

    /**
     * Prints message by certain error code.
     *
     * @param errorCode Error code to decide which message to print.
     */
    private static void handleError(int errorCode) {

        // (IDE automatically shifts case, and I wont shift it back)
        switch (errorCode) {
            case ERR_NO_VALID_INPUT:
                System.out.println("Error! No valid input...");
                break;
            case  ERR_NOT_INITIALIZED:
                System.out.println("Error! Game has not started yet!");
                break;
            case  ERR_ACTION_NOT_POSSIBLE:
                System.out.println("Error! Action not possible");
        }
    }
}
