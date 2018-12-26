import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Scanner;

/**
 * Shell class as user interface
 */
public final class Shell {

    private static final String DEFAULT_ERR_MESSAGE
            = "Error! No valid input...";
    private static final String MSG_VICTORY = "Congratulations! You won.";
    private static final String MSG_DEFEAT = "Sorry! Machine wins";
    private static final String[] COMMANDS =
            {
                    "new", "level", "switch", "move", "witness", "print",
                    "help", "quit"
            };

    private static boolean run;
    private static ConnectFour game;

    private Shell() {

    }

    /**
     * Main method that starts the shell.
     *
     * @param args main
     * @throws IOException Occurs on
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
                    setLevel(getNextInt(sc));
                    break;

                case 's':
                    Player p1 = new Player('O', true);
                    Player p2 = new Player('X');
                    createNewGame(p1, p2);
                    break;

                case 'm':
                    move(getNextInt(sc));
                    break;

                case 'w':
                    witness();
                    break;

                case 'p':
                    if (game != null) {
                        System.out.println(game.toString());
                    }
                    break;

                case 'h':
                    printHelpMessage();
                    break;

                case 'q':
                    run = false;
                    break;

                default:
                    System.out.println(DEFAULT_ERR_MESSAGE);
            }
        }

        sc.close();
    }

    private static void createNewGame(Player player1, Player player2) {
        game = new ConnectFour(player1, player2);
        if (game.getFirstPlayer().isMachine()) {
            game.machineMove();
        }
    }

    /**
     * Prints coordinates of winning group
     */
    private static void witness() {
        for (Coordinates2D c : game.getWitness()) {
            System.out.println(c.toString());
        }
    }

    /**
     * Prints a help message for the user
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
     * Performs player move if possible
     *
     * @param column Column to put Checker in
     */
    private static void move(int column) {
        if (!game.getFirstPlayer().isMachine()) {
            if (column > 0 && column < 8) {
                game = (ConnectFour) game.move(column);
            } else {
                System.out.println(DEFAULT_ERR_MESSAGE);
            }
        }
    }

    /**
     * Sets game level
     *
     * @param level Level for game (values from 1 to 5 possible)
     */
    private static void setLevel(int level) {
        if (level > 0 && level < 6) {
            game.setLevel(level);
        } else {
            System.out.println(DEFAULT_ERR_MESSAGE);
        }
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
}
