import dao.ActivityDAO;
import dao.LaborerDAO;
import entities.Activity;
import entities.Laborer;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException,
            UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new forms.MainPanel().setVisible(true);
    }
}
