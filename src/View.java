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

        menuPanel.add(levelSelection);
        menuPanel.add(newGameButton);
        menuPanel.add(switchButton);
        menuPanel.add(quitButton);

        gamePanel.setLayout(new GridLayout());
        gamePanel.setBackground(Color.BLUE);

        // Adding components to the main frame
        mainFrame.getContentPane().add(BorderLayout.CENTER, gamePanel);
        mainFrame.getContentPane().add(BorderLayout.SOUTH, menuPanel);

        mainFrame.setSize(500, 400);
        mainFrame.setVisible(true);

    }

    private  void initLevelComboBox() {
        for (int level : LEVELS) {
            levelSelection.addItem(level);
        }
    }

    public static void main(String[] args) {
        View mainView = new View();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
