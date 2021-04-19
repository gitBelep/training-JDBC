package nemetNeveloGyakorlo;

import javax.swing.*;

public class ProblemPopUp {
    private JFrame frame;

    ProblemPopUp(String message) {    //konstruktor
        frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                message,
                "Alert", JOptionPane.WARNING_MESSAGE);
    }

}
