package nemetNeveloGyakorlo;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Menu extends JFrame implements ActionListener {
    private JDesktopPane desktop;
    private JLabel userNameInstruction;
    private JTextField userNameTextField;
    private JButton userButton;
    private List<JButton> dictButtons;
    private List<JButton> gameButtons;
    private List<JLabel> labels = new ArrayList<>();
    private List<File> dictionaryList;
    private List<String> dictionaryNameList = new ArrayList<>();
    private int posElementsOnTheBottom = 275; //separator and below
    private int posElementsOnTheRight = 555;
    static JLabel infoSelectedDict;
    static File SELECTED_DictFile;
    static GameType SELECTED_GameArt;
    static String USER_NAME = "";
    FileOperations fo = new FileOperations();

    public Menu() {       //konstruktor
        super("Német névelőgyakorló  by dd");
        Image icon = Toolkit.getDefaultToolkit().getImage("c:\\NemetNeveloGyakorlo\\img\\kekLang.png");
        setIconImage(icon);

        int inset = 10;   //Make the big window be indented X pixels from the edge of the screen.
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, 800, 800); //screenSize.width-inset*2,screenSize.height-inset*2);

        desktop = new JDesktopPane();  //Set up the GUI
        setContentPane(desktop);
        setupList();

        userNameInstruction = new JLabel("Add meg a neved:");
        userNameInstruction.setBounds(20, 10, 400, 40);
        labels.add(userNameInstruction);

        userNameTextField = new JTextField();              // in
        userNameTextField.setBounds(210, 10, 210, 38);
        userNameTextField.setFont(new Font("Arial", Font.PLAIN, 18));
        userNameTextField.addActionListener(this);      //hozzáadom a figyelőt
        add(userNameTextField);

        userButton = new JButton ("OK" );
        userButton.setBounds(430, 10, 110, 35);
        setButtonFontAndAdd(List.of(userButton));

        JLabel separatorLabel2 = new JLabel("");
        separatorLabel2.setBounds(4, 55, 535, 3);
        Border border = BorderFactory.createLineBorder(Color.ORANGE);
        separatorLabel2.setBorder(border);
        add(separatorLabel2);

        JLabel instruction1 = new JLabel("Hogyan gyakoroljunk?");
        instruction1.setBounds(20, posElementsOnTheBottom -210, 300, 30);
        labels.add(instruction1);

        List<String> gameList = List.of("MAGYAR SZÓHOZ NÉMET MEGFELELŐT ÍROK", "NÉMET SZÓHOZ NÉVELŐT VÁLASZTOK", "NÉMET SZÓHOZ MAGYAR MEGFELELŐT ÍROK");
        gameButtons = new ArrayList<>();
        for (int i = 0; i < gameList.size(); i++){
            gameButtons.add(new JButton ( gameList.get(i)));
            gameButtons.get(i).setBounds(30, posElementsOnTheBottom -170 + i * 50, 500, 40);
        }
        setButtonFontAndAdd(gameButtons);

        JLabel separatorLabel1 = new JLabel("");
        separatorLabel1.setBounds(4, posElementsOnTheBottom -10, 535, 3);
        separatorLabel1.setBorder(border);
        add(separatorLabel1);

        JLabel instruction4 = new JLabel("Melyik gyűlyteményt kérdezzem ki?");
        instruction4.setBounds(20, posElementsOnTheBottom, 350, 30);
        labels.add(instruction4);

        JLabel info1 = new JLabel("Kiválasztva:");
        info1.setBounds(250, posElementsOnTheBottom +40, 150, 30);
        labels.add(info1);

        infoSelectedDict = new JLabel(" \"még semmi\" ");
        infoSelectedDict.setBounds(365, posElementsOnTheBottom +40, 200, 30);
        infoSelectedDict.setFont(new Font("Arial", Font.ITALIC, 16));
        add(infoSelectedDict);

        JSeparator verticalSeparator1 = new JSeparator();
        verticalSeparator1.setBounds(posElementsOnTheRight,10,2,600);
        verticalSeparator1.setBorder(border);
        add(verticalSeparator1);
        JSeparator verticalSeparator2 = new JSeparator();
        verticalSeparator2.setBounds(posElementsOnTheRight+2,11,2,601);
        verticalSeparator2.setBorder(border);
        add(verticalSeparator2);

        JButton scoresButton = new JButton("Eredménytábla");
        scoresButton.setBounds(posElementsOnTheRight+15, 10,180,35);
        scoresButton.setFont(new Font("Arial", Font.BOLD, 18));
        scoresButton.addActionListener(this);
        scoresButton.setActionCommand("showScores");
        add(scoresButton);

        dictButtons = new ArrayList<>();
        for (int i = 0; i < dictionaryNameList.size(); i++){
            dictButtons.add( new JButton (i +".  "+ dictionaryNameList.get(i)) );
            dictButtons.get(i).setBounds(30, posElementsOnTheBottom + 80 + i * 50, 290, 40);
        }
        setButtonFontAndAdd(dictButtons);
        setLabelsFontAndAdd();
    }

//Events
     @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("showScores")){
            showScores();
        }
        if(gameButtons.contains(e.getSource())){
            setGameArt(e.getSource());
        }
         if(dictButtons.contains(e.getSource())){
             setDictionary(e.getSource());
         }
        if (e.getSource().equals(userButton) || e.getSource().equals(userNameTextField)){
            setAndResetUser();
        }
        if(SELECTED_GameArt != null && SELECTED_DictFile != null && USER_NAME.trim().length() > 0){
            startGame();
        }
    }

    private void showScores(){
        ScoreDisplay scoreDisplay = new ScoreDisplay(fo);
        createInternalFrame(scoreDisplay);
    }

    private void setAndResetUser(){
        if(userButton.getText().equals("OK")) {
            String temp = userNameTextField.getText();
            if(temp.contains(";")){
                userNameTextField.setText("");
                ProblemPopUp pp = new ProblemPopUp("A neved ne tartalmazzon \";\"-t!");
            }
            if(temp.trim().length() == 0){
                ProblemPopUp pp = new ProblemPopUp("A neved megjeleníthető karakterekből álljon!");
            } else {
                USER_NAME = temp;
                userNameTextField.setText("");
                userNameTextField.setVisible(false);
                userNameInstruction.setText("Helló " + USER_NAME);
                userButton.setText("Új név");
            }
        } else {
            USER_NAME ="";
            userNameTextField.setVisible(true);
            userNameInstruction.setText("Add meg a neved:");
            userButton.setText("OK");
        }
    }

    private void setDictionary(Object source){
        int place = dictButtons.indexOf(source);
        SELECTED_DictFile = dictionaryList.get(place);
        infoSelectedDict.setText(dictionaryNameList.get(place));
        //System.out.println(dictionaryList.get(place).getName() +" "+ dictionaryNameList.get(place));
    }

    private void setGameArt(Object source){
        if (source.equals(gameButtons.get(0))) {
            SELECTED_GameArt = GameType.GERMAN_TO_HUN;
            //System.out.println("Game GGermanToHun");
        }
        if (source.equals(gameButtons.get(1))) {
            SELECTED_GameArt = GameType.ARTICLE_TO_GERMAN;
            //System.out.println("Game GArticleToGerman");
        }
        if (source.equals(gameButtons.get(2))) {
            SELECTED_GameArt = GameType.HUN_TO_GERMAN;
            //System.out.println("Game GHunToGerman");
        }
    }

//Let's play
    private void startGame(){
        Game game = new Game();
        if(SELECTED_GameArt ==GameType.GERMAN_TO_HUN){
            game = new GGermanToHun(fo);
        }
        if(SELECTED_GameArt == GameType.ARTICLE_TO_GERMAN){
            game = new GArticleToGerman(fo);
        }
        if(SELECTED_GameArt == GameType.HUN_TO_GERMAN){
            game = new GHunToGerman(fo);
        }
        createInternalFrame(game);
    }

    private void createInternalFrame(JInternalFrame internalFrame) {
        internalFrame.setVisible(true);
        desktop.add(internalFrame);
        try {
            internalFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
           ProblemPopUp pp = new ProblemPopUp("Cannot create Game. Please restart!");
        }
    }

//setup the Menu page and Lists
    private void setupList(){
        try {
            dictionaryList = new FileOperations().getDictionaryFiles();
            fillDictionaryNameList();
        } catch (IllegalStateException e){
            ProblemPopUp pp = new ProblemPopUp(e.getMessage());
        }
    }

    private void fillDictionaryNameList(){
        if (dictionaryList.size() == 0){
            throw new IllegalStateException("Dictionary is empty");
        }
        for (File f : dictionaryList) {
            String fileName = f.getName();
            int point = fileName.lastIndexOf(".");
            dictionaryNameList.add(fileName.substring(2, point));
        }
    }

    private void setLabelsFontAndAdd() {
        for(JLabel j : labels) {
            j.setFont(new Font("Arial", Font.BOLD, 18));
            add(j);
        }
    }
    private void setButtonFontAndAdd(List<JButton> buttons) {
        for(JButton b : buttons) {
            b.setFont(new Font("Arial", Font.BOLD, 18));
            b.addActionListener(this);
            add(b);
        }
    }

    private static void createAndShowGUI(){ //For thread safety invoked here.
        Menu menuFrame = new Menu();        //Create the window.
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLayout(null);
        menuFrame.setVisible(true);
    }
//    private void quit() {  System.exit(0);  }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Menu::createAndShowGUI);
    }

}
