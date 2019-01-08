import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Dimension2D;

public class View extends JFrame implements ActionListener {

    private  JFrame mainFrame;
    private  JPanel gamePanel;
    private  JPanel menuPanel;
    private  JButton newGameButton;
    private  JButton switchButton;
    private  JButton quitButton;
    private  JComboBox<Integer> levelSelection;

    private static Board gameModel;

    private static final int[] LEVELS = {1, 2, 3, 4, 5};
    private static final int DEFAULT_HEIGHT = 650;
    private static final int DEFAULT_WIDTH = 700;

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
            gamePanel.add(new Slot(slotDim));
            numberOfSlots--;
        }
    }

    private Dimension getSlotSize() {
        int relHeight = DEFAULT_HEIGHT / Board.ROWS;
        int relWidth = DEFAULT_WIDTH / Board.COLS;

        return new Dimension(relWidth, relHeight);
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
            gameModel = gameModel.machineMove();
        }
    }

    /**
     * Checks if game is initiated.
     *
     * @return true if game is running.
     */
    private static boolean initiated() {
        return gameModel != null;
    }

    private void addActionListeners() {
        levelSelection.addActionListener(new SelectionListener());
        newGameButton.addActionListener(new NewGameListener());
        switchButton.addActionListener(new SwitchListener());
        quitButton.addActionListener(new QuitListener());
        gamePanel.addMouseListener(new Mylistener());
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
    }

    class QuitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class Mylistener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            e.getSource();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            e.getSource();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int column;
            e.getSource();
            for (int col = 0; col < Board.COLS; col++) {

            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

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
