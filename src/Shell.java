import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
     * @param args main
     * @throws IOException can occur
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader
                = new BufferedReader(new InputStreamReader(System.in));
        runShell(reader);

    }

    /**
     * Runs the shell-loop
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
     * @param input user input
     */
    private static void evalInput(String input) {
        Scanner sc =  new Scanner(input);
        String command = "";

        if (sc.hasNext()) {
            command = sc.next();

            switch (command.charAt(0)) {
                case 'n':
                    if (command.equals(COMMANDS[0]) || command.length() == 1) {
                        game = new ConnectFour(new Player('X'), new Player('O'));
                    }
                    break;

                case 'l':
                    if (command.equals(COMMANDS[1]) || command.length() == 1) {
                        int i = 0;

                        if (sc.hasNextInt()) {
                            i = sc.nextInt();
                        }

                        setLevel(i);
                    }
                    break;

                case 's':
                    if (command.equals(COMMANDS[2]) || command.length() == 1) {
                        System.out.println(game.getCheckerValueQ());
                    }

                    break;
                case 'm':
                    if (command.equals(COMMANDS[3]) || command.length() == 1) {
                        int c = 0;

                        if (sc.hasNextInt()) {
                            c = sc.nextInt();
                        }

                        move(c);
                    }

                    break;
                case 'w':
                    if (command.equals(COMMANDS[4]) || command.length() == 1) {

                    }

                    break;
                case 'p':
                    if (command.equals(COMMANDS[5]) || command.length() == 1) {
                        if (game != null) {
                            System.out.println(game.toString());
                        }
                    }

                    break;
                case 'h':
                    if (command.equals(COMMANDS[6]) || command.length() == 1) {
                        printHelpMessage();
                    }
                    break;

                case 'q':
                    if (command.equals(COMMANDS[7]) || command.length() == 1) {
                        run = false;
                    }
                    break;

                default:
                    System.out.println(DEFAULT_ERR_MESSAGE);
            }
        }

        sc.close();
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
}
