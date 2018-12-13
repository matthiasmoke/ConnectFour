import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Scanner;

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
     * Main method that starts the shell
     *
     * @param args main
     * @throws IOException can occur
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader
                = new BufferedReader(new InputStreamReader(System.in));
        init();
        runShell(reader);

    }

    /**
     * Runs the shell-loop
     *
     * @param reader for user-input
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
     * Evals the input
     *
     * @param input user input
     */
    private static void evalInput(String input) {
        Scanner sc = new Scanner(input);
        String command = "";

        if (sc.hasNext()) {
            command = sc.next();

            switch (command.charAt(0)) {

                case 'n':
                    game = new ConnectFour(new Player('X'), new Player('O'));
                    break;

                case 'l':
                    int i = 0;
                    if (sc.hasNextInt()) {
                        i = sc.nextInt();
                    }
                    setLevel(i);
                    break;

                case 's':
                    break;

                case 'm':
                    int c = 0;
                    if (sc.hasNextInt()) {
                        c = sc.nextInt();
                    }
                    move(c);
                    break;

                case 'w':
                    game.groupSearch();
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

    private static void witness(Collection<Coordinates2D> coordinates) {
        for (Coordinates2D c : coordinates) {
            System.out.println(c.toString());
        }
    }

    private static void printHelpMessage() {
        StringBuilder b = new StringBuilder();

        System.out.println(b.toString());
    }

    private static void move(int c) {
        if (c > 0) {
            game.move(c);
        } else {
            System.out.println(DEFAULT_ERR_MESSAGE);
        }
    }

    private static void setLevel(int i) {
        if (i > 0 && i < 6) {

        } else {
            System.out.println(DEFAULT_ERR_MESSAGE);
        }
    }

    private static void init() {
        evalInput("n");
        evalInput("m 1");
        evalInput("m 1");
        evalInput("m 1");
        evalInput("m 2");
        evalInput("m 2");
        evalInput("m 2");
        evalInput("m 3");
        evalInput("m 3");
        evalInput("m 3");
        evalInput("p");

    }
}
