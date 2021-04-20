package nemetNeveloGyakorlo;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.List;

public class GArticleToGerman extends Game implements ActionListener {
    private JLabel lChallenge;
    private JButton b;
    private JLabel lOk;
    private JLabel lOkCounter;
    private JLabel lSolution;
    private JRadioButton rButtonR;
    private JRadioButton rButtonE;
    private JRadioButton rButtonS;
    private JTextArea area;
    private JLabel separatorLabel;
    private Word actualWord;
    private List<Word> words;
    private int roundSum = 0;
    private int rightAnswerCounter = 0;
    private boolean lastAnswerWasRight = false;
    private boolean newRound = false;
    private final FileOperations fo;

    public GArticleToGerman(FileOperations fo) {
        this.fo = fo;
        initGArticleToGerman();
        playOneWord();
    }

    private void initGArticleToGerman(){
        setFrameIcon(new ImageIcon("c:\\NemetNeveloGyakorlo\\img\\fogaskerek-k.png"));
        setTitle("    Névelő Német Szóhoz                    ");

        lChallenge = new JLabel();
        lChallenge.setBounds(170, 60, 200, 30);
        lChallenge.setFont(new Font("Arial", Font.BOLD, 22));
        add(lChallenge);

        rButtonR =new JRadioButton(" DER ");
        rButtonR.setBounds(40,40,102,35);
        rButtonR.setFont(new Font("Arial", Font.PLAIN, 18));
        rButtonE =new JRadioButton(" DIE ");
        rButtonE.setBounds(40,75,102,35);
        rButtonE.setFont(new Font("Arial", Font.PLAIN, 18));
        rButtonS =new JRadioButton(" DAS ");
        rButtonS.setBounds(40,110,102,35);
        rButtonS.setFont(new Font("Arial", Font.PLAIN, 18));
        ButtonGroup bg=new ButtonGroup();
        bg.add(rButtonR);
        bg.add(rButtonE);
        bg.add(rButtonS);
        add(rButtonR);
        add(rButtonE);
        add(rButtonS);

        lOk = new JLabel("Helyes: ");
        lOk.setBounds(170, 155, 250, 40);
        lOk.setFont(new Font("Arial", Font.BOLD, 14));
        add(lOk);

        lOkCounter = new JLabel("0");
        lOkCounter.setBounds(250, 155, 140, 40);
        lOkCounter.setFont(new Font("Arial", Font.BOLD, 14));
        add(lOkCounter);

        b = new JButton("Kész");
        b.setBounds(40, 160, 102, 40);
        b.setFont(new Font("Arial", Font.BOLD, 18));
        b.addActionListener(this);
        add(b);

        separatorLabel = new JLabel("");
        separatorLabel.setBounds(1, 220, 520, 3);
        Border border = BorderFactory.createLineBorder(new Color(10, 245, 80));
        separatorLabel.setBorder(border);
        add(separatorLabel);

        lSolution = new JLabel("Megoldások:");
        lSolution.setBounds(21, 230, 250, 30);
        lSolution.setFont(new Font("Arial", Font.ITALIC, 16));
        lSolution.setForeground(new Color(50, 145, 60));
        add(lSolution);

        area = new JTextArea();
        area.setBounds(20,265, 500,450);
        area.setFont(new Font("Courier New", Font.BOLD, 18));
        area.setForeground(new Color(20, 10, 80));
        area.setBackground(new Color(50, 145, 60));
        add(area);

        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(225,220,40));
        setLayout(null);
        setVisible(true);

        try {
            words = fo.provideWordList();
            roundSum = words.size();
        } catch(IllegalStateException e){
            ProblemPopUp pp = new ProblemPopUp(e.getMessage());
            closeInternalFrame();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(b.getText().contains("VISSZA")){
            closeInternalFrame();
            return;
        }
        checkAnswer();
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
        Icon iccon = new ImageIcon(FileOperations.PATH_STR + "\\img\\sonic-kor_102x102.png");
        b.setText(" Rohanás innen VISSZA");
        b.setBounds(240, 114, 350, 102);
        b.setIcon(iccon);
        lOk.setText("Eredmény: "+ rightAnswerCounter +"  a "+ roundSum +"-ból.");
        lOk.setBounds(40,160,250, 40);
        lOkCounter.setText("");
        separatorLabel.setBounds(1, 220, 599, 3);
        fo.notingResults(rightAnswerCounter, Menu.SELECTED_DictFile.getName(), Menu.SELECTED_GameArt, Menu.USER_NAME);
    }

    private void checkAnswer(){
        String answerArticle;
        if(rButtonR.isSelected()) {
            answerArticle = "r";
        }else if(rButtonE.isSelected()) {
            answerArticle = "e";
        }else if(rButtonS.isSelected()) {
            answerArticle = "s";
        } else {
            new ProblemPopUp("Answer is not chosen. Try again!");  //?
            newRound = true;
            return;
        }
        if(answerArticle.equals(actualWord.getArticle())){
            rightAnswerCounter++;
            lastAnswerWasRight = true;
        } else {
            lastAnswerWasRight = false;
        }
    }

    private void playOneWord(){
        actualWord = words.get(words.size()-1);
        lChallenge.setText(actualWord.getGermanWord());
        words.remove(words.size()-1);
    }

    private void closeInternalFrame(){
        Menu.SELECTED_DictFile = null;
        Menu.SELECTED_GameArt = null;
        Menu.infoSelectedDict.setText( " \"még semmi\" ");
        try {
            this.setClosed(true);
        } catch (PropertyVetoException e) {
            ProblemPopUp pp = new ProblemPopUp("Please restart!");
        }
    }

}
