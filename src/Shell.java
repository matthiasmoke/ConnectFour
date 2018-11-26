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

}
