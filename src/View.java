import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public View() {
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

        gamePanel.setLayout(new GridLayout());
        gamePanel.setBackground(Color.BLUE);

        // Adding components to the main frame.
        mainFrame.getContentPane().add(BorderLayout.CENTER, gamePanel);
        mainFrame.getContentPane().add(BorderLayout.SOUTH, menuPanel);

        mainFrame.setVisible(true);
        mainFrame.setSize(500, 400);

    }

    private void addActionListeners() {
        levelSelection.addActionListener(actionEvent -> selectionChanged());
        newGameButton.addActionListener(actionEvent -> initNewGame());
        switchButton.addActionListener(actionEvent -> switchBeginner());
        quitButton.addActionListener(actionEvent -> quitGame());
    }

    private void quitGame() {
        System.exit(0);
    }

    private void switchBeginner() {
        Player p1 = new Player(Color.RED, true);
        Player p2 = new Player(Color.YELLOW, false);
        createNewGame(p1, p2);
    }

    private void initNewGame() {
        gameModel = new ConnectFour();
    }

    private void selectionChanged() {
        if (initiated()) {
            gameModel.setLevel((int)levelSelection.getSelectedItem());
        }
    }

    private  void initLevelComboBox() {
        for (int level : LEVELS) {
            levelSelection.addItem(level);
            levelSelection.setSelectedItem(LEVELS[3]);
        }
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

    public static void main(String[] args) {
        View mainView = new View();

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
