package forms;

import javax.swing.*;
import java.awt.*;


public class MainPanel extends JFrame {


    private JPanel rootPanel;


    public MainPanel() {
        setTitle("Java Application");
        initializeComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 400);
        setContentPane(rootPanel);
        setLocationRelativeTo(null);

    }

    private void initializeComponents() {
        ActivityTable activityTable = new ActivityTable();
        LaborerTable laborerTable = new LaborerTable(activityTable);
        activityTable.setChildTable(laborerTable);
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayout(2, 1, 0, 3));
        rootPanel.add(new JScrollPane(activityTable));
        rootPanel.add(new JScrollPane(laborerTable));
    }


}
