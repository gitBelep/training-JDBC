package nemetNeveloGyakorlo;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.List;

public class GHunToGerman extends Game implements ActionListener {
    private JLabel lChallenge;
    private JTextField hunTextField;
    private JButton b;
    private JLabel lOk;
    private JLabel lOkCounter;
    private JLabel lSolution;
    private JTextArea area;
    private JLabel separatorLabel;
    private Word actualWord;
    private List<Word> words;
    private int roundSum = 0;
    private int rightAnswerCounter = 0;
    private boolean lastAnswerWasRight = false;
    private boolean newRound = false;
    private final FileOperations fo;

    public GHunToGerman(FileOperations fo) {
        this.fo = fo;
        initGHunToGerman();
        playOneWord();
    }

    private void initGHunToGerman(){
        setFrameIcon(new ImageIcon("c:\\NemetNeveloGyakorlo\\img\\i_hal.png"));
        setTitle("          Magyar Szó Névelő Nélkül                    ");

        lChallenge = new JLabel();
        lChallenge.setBounds(20, 60, 200, 30);
        lChallenge.setFont(new Font("Arial", Font.PLAIN, 18));
        add(lChallenge);

        hunTextField = new JTextField();              // in
        hunTextField.setBounds(230, 60, 290, 30);
        hunTextField.setFont(new Font("Arial", Font.PLAIN, 18));
        hunTextField.addActionListener(this);
        add(hunTextField);

        lOk = new JLabel("Helyes: ");
        lOk.setBounds(20, 125, 250, 40);
        lOk.setFont(new Font("Arial", Font.BOLD, 14));
        add(lOk);

        lOkCounter = new JLabel("0");
        lOkCounter.setBounds(100, 125, 140, 40);
        lOkCounter.setFont(new Font("Arial", Font.BOLD, 14));
        add(lOkCounter);

        b = new JButton("Kész");
        b.setBounds(230, 125, 102, 40);
        b.setFont(new Font("Arial", Font.BOLD, 18));
        b.addActionListener(this);
        add(b);

        separatorLabel = new JLabel("");
        separatorLabel.setBounds(1, 220, 520, 3);
        Border border = BorderFactory.createLineBorder(Color.CYAN);
        separatorLabel.setBorder(border);
        add(separatorLabel);

        lSolution = new JLabel("Megoldások:");
        lSolution.setBounds(20, 230, 250, 30);
        lSolution.setFont(new Font("Arial", Font.ITALIC, 16));
        lSolution.setForeground(new Color(30, 70, 120));
        add(lSolution);

        area = new JTextArea();
        area.setBounds(20,265, 500,450);
        area.setFont(new Font("Courier New", Font.BOLD, 18));
        area.setForeground(new Color(20, 100, 80));
        area.setBackground(new Color(150, 250, 250));
        add(area);

        Color c = new Color(55,200,60);
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(c);
        setLayout(null);
        setVisible(true);

        try {
            words = fo.provideWordList();
            roundSum = words.size();
        } catch(IllegalStateException e){
            new ProblemPopUp(e.getMessage());
            closeInternalFrame();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(b.getText().contains("VISSZA")){
            closeInternalFrame();
            return;
        }
        checkAnswer(hunTextField.getText());
        hunTextField.setText("");
        if (newRound){
            newRound = false;
            return;
        }
        String rightStr = lastAnswerWasRight ? "( + )" : "( - )";
        String answer = String.format("%s %s %-17s °  %s", rightStr, actualWord.getArticle(), actualWord.getGermanWord(),actualWord.getHunWord());
        area.setText(area.getText() + answer +"\n");
        lOkCounter.setText(""+ rightAnswerCounter +"  a "+ roundSum +"-ból.");
        if(words.isEmpty()){
            saveAndSonic();
        } else {
            playOneWord();
        }
    }

    private void saveAndSonic(){
        Icon iccon = new ImageIcon(FileOperations.PATH_STR + "\\img\\sonic-end.png");
        b.setText(" Rohanás innen VISSZA");
        b.setBounds(230, 114, 350, 102);
        b.setIcon(iccon);
        lOk.setText("Eredmény:");
        separatorLabel.setBounds(1, 220, 599, 3);
        fo.notingResults(rightAnswerCounter, Menu.SELECTED_DictFile.getName(), Menu.SELECTED_GameArt, Menu.USER_NAME);
    }

    private void checkAnswer(String answerHun){
        if(answerHun == null || answerHun.trim().length() <= 1){
            new ProblemPopUp("Answer is not valid, too short. Try again!");
            newRound = true;
            return;
        }
        if(answerHun.trim().equalsIgnoreCase(actualWord.getHunWord())){
            rightAnswerCounter++;
            lastAnswerWasRight = true;
        } else {
            lastAnswerWasRight = false;
        }
    }

    private void playOneWord(){
        actualWord = words.get(words.size()-1);
        lChallenge.setText(actualWord.getArticle() +" "+ actualWord.getGermanWord());
        words.remove(words.size()-1);
    }

    private void closeInternalFrame(){
        Menu.SELECTED_DictFile = null;
        Menu.SELECTED_GameArt = null;
        Menu.infoSelectedDict.setText( " \"még semmi\" ");
        try {
            this.setClosed(true);
        } catch (PropertyVetoException e) {
            new ProblemPopUp("Please restart!");
        }
    }

}
