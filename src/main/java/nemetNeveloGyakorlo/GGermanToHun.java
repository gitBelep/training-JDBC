package nemetNeveloGyakorlo;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.List;

public class GGermanToHun extends Game implements ActionListener {
    private JLabel lNameOfGame;
    private JLabel lChallenge;
    private JTextField germanTextField;
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
    FileOperations fo;

    public GGermanToHun(FileOperations fo) {             //konstruktor
        this.fo = fo;
        initGGermanToHun();
        playOneWord();
    }

    private void initGGermanToHun(){
        lNameOfGame = new JLabel("Német szó névelővel            Elfogadható megoldások: der Vater / derVater / Der Vater / DerVater / rVater");
        lNameOfGame.setBounds(10, 10, 700, 20);
        lNameOfGame.setFont(new Font("Arial", Font.BOLD, 12));
        add(lNameOfGame);

        lChallenge = new JLabel();                      // out
        lChallenge.setBounds(20, 60, 200, 30);
        lChallenge.setFont(new Font("Arial", Font.PLAIN, 18));
        add(lChallenge);

        germanTextField = new JTextField();              // in
        germanTextField.setBounds(230, 60, 290, 30);
        germanTextField.setFont(new Font("Arial", Font.PLAIN, 18));
        germanTextField.addActionListener(this);      //hozzáadom a figyelőt
        add(germanTextField);

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
        b.addActionListener(this);  //hozzáadom a figyelőt a gombhoz
        add(b);

        separatorLabel = new JLabel("");          // elválasztóvonalként
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
        area.setForeground(new Color(20, 120, 80));
        area.setBackground(new Color(150, 250, 250));
        add(area);

        Color c = new Color(0xB44B79);
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(c);
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
        checkAnswer(germanTextField.getText());
        germanTextField.setText("");
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
        b.setBounds(230, 114, 350, 102);
        b.setIcon(iccon);
        lOk.setText("Eredmény:");
        separatorLabel.setBounds(1, 220, 599, 3);
System.out.println(Menu.SELECTED_DictFile.getName() +"Gameben");
        fo.notingResults(rightAnswerCounter, Menu.SELECTED_DictFile.getName(), Menu.SELECTED_GameArt, Menu.USER_NAME);
    }

    private void checkAnswer(String wholeAnswer){
        if(wholeAnswer == null || wholeAnswer.trim().length() <= 2){
            ProblemPopUp pp = new ProblemPopUp("Answer is not valid, too short. Try again!");
            newRound = true;
            return;
        }
        String answer = wholeAnswer.trim();
        String answerArticle = "";
        String answerGerman = "";
        String answerStart = answer.substring(0,1);

        if(answerStart.equalsIgnoreCase("d")){
            answerArticle = answer.substring(2,3); // Der/die/das -> r/e/s
            answerGerman = answer.substring(3).trim(); //should start with Uppercase
        }

        String articles = "res";
        if(articles.contains(answerStart)){
            answerArticle = answer.substring(0,1); //r/e/s
            answerGerman = answer.substring(1).trim();
        }

        if(answerArticle.equals(actualWord.getArticle())
                && answerGerman.equals(actualWord.getGermanWord())){
            rightAnswerCounter++;
            lastAnswerWasRight = true;
        } else {
            lastAnswerWasRight = false;
        }
    }

    private void playOneWord(){
        actualWord = words.get(words.size()-1);
        lChallenge.setText(actualWord.getHunWord() +":");
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
