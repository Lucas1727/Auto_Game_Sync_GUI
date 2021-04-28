import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddGameGUI extends MainGUI {

    public static JFrame mainFrame;
    private static JLabel headerLabel1;
    private static JPanel controlPanel;

    public void prepareAddGameGUI() {

        MainGUI.mainFrame.setVisible(false);

        mainFrame = new JFrame("Simulated Exploration Setup");
        mainFrame.setSize(350, 190);
        //mainFrame.setLayout(new BorderLayout());
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        headerLabel1 = new JLabel("", JLabel.CENTER);
        headerLabel1.setText("Add a game");

        BackButton();

        mainFrame.add(headerLabel1);
        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);
    }

    public void BackButton() {
        JButton BackButton = new JButton("Back");
        BackButton.setActionCommand("Back");
        BackButton.addActionListener(new MainGUI.ButtonClickListener());
        controlPanel.add(BackButton);
    }

    public static void GoBackToMainGUI() {
        mainFrame.setVisible(false);
        MainGUI.mainFrame.setVisible(true);
    }
}
