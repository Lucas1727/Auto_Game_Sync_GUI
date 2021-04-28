import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

public class MainGUI {

    private static MainGUI mGUI;

    public static JFrame mainFrame;
    private JLabel headerLabel1;
    private JPanel controlPanel;
    private JPanel outputPanel;
    private JPanel bottomPanel;

    private JTextArea outputArea = new JTextArea();

    private int windowHeight = 640;
    private int windowWidth = 960;

    private String data[][];

    private TreeMap sortedHashMap = null;

    //private ArrayList<String> data = new ArrayList<String>();

    GameSync gameSync;
    //Add_Game_GUI addGameGui;

    private void Start() {
        gameSync = new GameSync();
        try {
            gameSync.Start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        prepareGUI();
    }


    public void prepareGUI() {

        mainFrame = new JFrame("Auto Game Sync");
        mainFrame.setSize(windowWidth, windowHeight);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width / 2 - mainFrame.getSize().width / 2, dim.height / 2 - mainFrame.getSize().height / 2);

        controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        outputPanel = new JPanel();
        outputPanel.setLayout(new FlowLayout());

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        headerLabel1 = new JLabel("", JLabel.CENTER);
        headerLabel1.setText("Add new Game or Sync Games?");

        ShowButton();
        SyncButton();
        AddButton();
        SavesTable();
        CreateOutputArea();

        Border blackLine = BorderFactory.createLineBorder(Color.black);
        controlPanel.setBorder(blackLine);

        Border redLine = BorderFactory.createLineBorder(Color.red);
        bottomPanel.setBorder(redLine);

        mainFrame.add(controlPanel, BorderLayout.CENTER);
        controlPanel.add(outputPanel, BorderLayout.SOUTH);
        bottomPanel.add(headerLabel1);
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
    }

    private void ShowButton() {
        JButton ShowButton = new JButton("Show");
        ShowButton.setActionCommand("Show");
        ShowButton.addActionListener(new ButtonClickListener());
        bottomPanel.add(ShowButton);
    }

    private void SyncButton() {
        JButton SyncButton = new JButton("Sync");
        SyncButton.setActionCommand("Sync");
        SyncButton.addActionListener(new ButtonClickListener());
        bottomPanel.add(SyncButton);
    }

    private void AddButton() {
        JButton AddButton = new JButton("Add");
        AddButton.setActionCommand("Add");
        AddButton.addActionListener(new ButtonClickListener());
        bottomPanel.add(AddButton);
    }

    public static void PrintOutput(String output) {
        mGUI.outputArea.append(output + "\n");
    }


    private void CreateOutputArea() {

        for (int i = 0; i < 20; i++) {
            if (i == 0) {
                //outputArea.append(i + ". " + "HI");
            } else {
                //outputArea.append("\n" + i + ". " + "HI");
            }

        }

        outputArea.setLineWrap(true);
        outputArea.setEditable(false);
        //outputArea.setSize(new Dimension(controlPanel.getWidth(), controlPanel.getHeight() / 2));
        outputArea.setPreferredSize(new Dimension(mainFrame.getWidth() - 40, 1000));
        outputArea.setMinimumSize(new Dimension(100, 200));
        outputArea.setRows(8);

        //outputArea.setPreferredSize(new Dimension(1, 10));

        JScrollPane scroll = new JScrollPane(outputArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setViewportView(outputArea);

        outputPanel.add(scroll);
    }

    public void SavesTable() {
        try {
            gameSync.runAppHash();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JTable jTable = PopulateTable();
        JScrollPane sPane = new JScrollPane(jTable);
        controlPanel.add(sPane);

        TableColumnModel columnModel = jTable.getColumnModel();
        jTable.setRowHeight(20);

        columnModel.getColumn(0).setPreferredWidth((int) (windowWidth * 0.1));
        columnModel.getColumn(1).setPreferredWidth((int) (windowWidth * 0.3));
        columnModel.getColumn(2).setPreferredWidth((int) (windowWidth * 0.28));
        columnModel.getColumn(3).setPreferredWidth((int) (windowWidth * 0.08));
        columnModel.getColumn(4).setPreferredWidth((int) (windowWidth * 0.08));
        columnModel.getColumn(5).setPreferredWidth((int) (windowWidth * 0.06));
        columnModel.getColumn(6).setPreferredWidth((int) (windowWidth * 0.1));
    }

    public JTable PopulateTable() {
        int tableLength;
        int i = 0;
        String[] columnNames = {"Title", "Directory", "Save File Location", "Game Exists", "Save Exists", "Synced", "Cloud Updated"};

        try {
            sortedHashMap = gameSync.returnTreeMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableLength = sortedHashMap.size();

        data = new String[tableLength][7];


        Set set = sortedHashMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            String[] info = new String[4];
            Map.Entry mentry = (Map.Entry) iterator.next();

            String x = (String) mentry.getKey();
            String y[] = (String[]) mentry.getValue();

            try {
                info = gameSync.runAppGUI(x, y[0], y[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            data[i][0] = x;
            data[i][1] = y[1];
            data[i][2] = y[0];
            data[i][3] = info[0];
            data[i][4] = info[1];
            data[i][5] = info[2];
            data[i][6] = info[3];
            i += 1;
        }

        JTable jTable = new JTable(data, columnNames);

        return jTable;
    }

    class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("Sync")) {
                System.out.println("Sync");
                try {
                    gameSync.runSyncGUI(data);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (command.equals("Add")) {
                //System.out.println("Add");
                //AddGameGUI.prepareAddGameGUI();
            } else if (command.equals("Show")) {
                //gameSync.Start();
                //gameSync.runAppHash();
            } else if (command.equals("Back")) {
                //System.out.println("Add");
                AddGameGUI.GoBackToMainGUI();
            } else {
                System.out.println("Else");
            }
        }
    }

    ItemListener sizeListener = new ItemListener() {
        public void itemStateChanged(ItemEvent itemEvent) {
            int state = itemEvent.getStateChange();
            ItemSelectable is = itemEvent.getItemSelectable();
            Integer.parseInt(selectedString(is));
        }
    };

    private String selectedString(ItemSelectable is) {
        Object selected[] = is.getSelectedObjects();
        return ((selected.length == 0) ? "null" : (String) selected[0]);
    }

    public static void main(String[] args) {
        mGUI = new MainGUI();
        mGUI.Start();
    }
}
