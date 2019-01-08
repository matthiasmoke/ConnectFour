import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class View extends JFrame implements ActionListener {

    private  JFrame mainFrame;
    private  JPanel gamePanel;
    private  JPanel menuPanel;
    private  JButton newGameButton;
    private  JButton switchButton;
    private  JButton quitButton;
    private  JComboBox<Integer> levelSelection;

    private static Board gameModel;
    private static Thread machineThread;

    private boolean machinePlaying = false;

    private static final int[] LEVELS = {1, 2, 3, 4, 5};
    private static final int DEFAULT_HEIGHT = 650;
    private static final int DEFAULT_WIDTH = 700;
    private static final String MSG_ILLEGAL_MOVE = "Illegal Move!";
    private static final String MSG_NOT_INITIATED = "Game has not started yet!";
    private static final String MSG_VICTORY = "Congratulations! You won.";
    private static final String MSG_DEFEAT = "Sorry! Machine wins.";
    private static final String MSG_GAMEOVER = "Game is already over!";

    public View() {

    }

    public void showGame() {
        initMainView();
    }

    public  void initMainView() {
        mainFrame = new JFrame("Connect Four");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initializing controls here.
        menuPanel = new JPanel();
        gamePanel = new JPanel();

        newGameButton = new JButton("New");
        switchButton = new JButton("Switch");
        quitButton = new JButton("Quit");
        levelSelection = new JComboBox<>();
        initLevelComboBox();
        addActionListeners();

        menuPanel.add(levelSelection);
        menuPanel.add(newGameButton);
        menuPanel.add(switchButton);
        menuPanel.add(quitButton);

        gamePanel.setLayout(new GridLayout(Board.ROWS, Board.COLS));
        initGamePanel();

        // Adding components to the main container.
        Container mainContainer = mainFrame.getContentPane();
        mainContainer.add(BorderLayout.CENTER, gamePanel);
        mainContainer.add(BorderLayout.SOUTH, menuPanel);

        mainFrame.pack();
        mainFrame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        mainFrame.setVisible(true);

    }

    private void drawGamePanel() {

    }

    private void initLevelComboBox() {
        for (int level : LEVELS) {
            levelSelection.addItem(level);
            levelSelection.setSelectedItem(LEVELS[3]);
        }
    }

    private void initGamePanel() {
        int numberOfSlots = Board.COLS * Board.ROWS;
        Dimension slotDim = getSlotSize();

        while (numberOfSlots > 0) {
            gamePanel.add(new Slot(slotDim, this));
            numberOfSlots--;
        }
    }

    private Dimension getSlotSize() {
        int relHeight = DEFAULT_HEIGHT / Board.ROWS;
        int relWidth = DEFAULT_WIDTH / Board.COLS;

        return new Dimension(relWidth, relHeight);
    }

    /**
     * Checks if game is initiated.
     *
     * @return true if game is running.
     */
    private static boolean initiated() {
        return gameModel != null;
    }

    private void performMachineMove() {
        machineThread = new Thread(() -> gameModel.machineMove());
        machineThread.start();
    }

    public void columnClickedEvent(int column) {
        if (initiated()) {
            if (!machinePlaying) {
                if (gameModel.isGameOver()) {
                    showMessage(MSG_GAMEOVER);
                } else {
                    Board playerMove = gameModel.move(column);

                    if (playerMove != null) {
                        gameModel = playerMove;

                        if (!checkWinner()) {
                            gameModel = gameModel.machineMove();
                            checkWinner();
                        }
                    } else {
                        showMessage(MSG_ILLEGAL_MOVE);
                    }
                }
            } else {
                showMessage(MSG_ILLEGAL_MOVE);
            }
        } else {
            showMessage(MSG_NOT_INITIATED);
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this,
                 message, "Attention",
                JOptionPane.WARNING_MESSAGE);
    }

    private boolean checkWinner() {
        if (gameModel.isGameOver()) {
            Player winner = gameModel.getWinner();

            if (winner != null) {
                if (winner.isMachine()) {
                    showMessage(MSG_DEFEAT);
                } else {
                    showMessage(MSG_VICTORY);
                }
                return true;
            }
        }
        return false;
    }

    private void addActionListeners() {
        levelSelection.addActionListener(new SelectionListener());
        newGameButton.addActionListener(new NewGameListener());
        switchButton.addActionListener(new SwitchListener());
        quitButton.addActionListener(new QuitListener());
        gamePanel.addComponentListener(new ResizeListener());
    }

    class SelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (initiated()) {
                gameModel.setLevel((int)levelSelection.getSelectedItem());
            }
        }
    }

    class NewGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameModel = new ConnectFour();
        }
    }

    class SwitchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Player p1 = new Player(Color.RED, true);
            Player p2 = new Player(Color.YELLOW, false);
            createNewGame(p1, p2);
        }

        /**
         * Creates a new game. Takes over level from old game.
         *
         * @param player1 Player one (beginning player)
         * @param player2 Player two
         */
        private void createNewGame(Player player1, Player player2) {
            gameModel = new ConnectFour(player1, player2);
            gameModel.setLevel((int) levelSelection.getSelectedItem());

            if (gameModel.getFirstPlayer().isMachine()) {
                performMachineMove();
            }
        }
    }

    class QuitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class ResizeListener implements ComponentListener {

        @Override
        public void componentResized(ComponentEvent e) {
            Dimension newSize = getSlotSize();

            //TODO
            for (Component slot : gamePanel.getComponents()) {
                slot.setPreferredSize(newSize);
            }
        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }

    public static void main(String[] args) {
        View mainView = new View();
        mainView.showGame();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
