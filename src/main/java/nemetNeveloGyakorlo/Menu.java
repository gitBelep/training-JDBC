package nemetNeveloGyakorlo;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class Menu extends JFrame implements ActionListener, MouseListener {
    private final JDesktopPane desktopPane;
    private JLabel userNameInstruction;
    private JTextField userNameTextField;
    private JButton userButton;
    private List<JButton> dictButtons;
    private List<JButton> gameButtons;
    private List<JLabel> labels = new ArrayList<>();
    private List<File> dictionaryList;
    private List<String> dictionaryNameList = new ArrayList<>();
    private final int posElementsOnTheBottom = 275; //separator and below
    private final int posElementsOnTheRight = 555;
    private final FileOperations fo = new FileOperations();
    private JSeparator verticalSeparator1;
    private JSeparator verticalSeparator2;
    private JLabel separatorLabel1;
    private JLabel separatorLabel2;
    private int r = 255;
    private int g = 165;
    private int b = 1;
    private Color color = new Color(r,g,b);   //Color.ORANGE
    private Border border = BorderFactory.createLineBorder(color);
    static JLabel infoSelectedDict;
    static File SELECTED_DictFile;
    static GameType SELECTED_GameArt;
    static String USER_NAME = "";
    static Image image;

    public Menu() {       //konstruktor
        super("  Német névelőgyakorló  by dd        ");
        image = Toolkit.getDefaultToolkit().getImage("c:\\NemetNeveloGyakorlo\\img\\i_kekLang.png");
        setIconImage(image);

        int inset = 10;   //window indented X pixels from the edge of the screen.
        setBounds(inset, inset, 806, 800);

        desktopPane = new JDesktopPane();  //Set up the GUI
        setContentPane(desktopPane);
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

        separatorLabel2 = new JLabel("");
        separatorLabel2.setBounds(4, 55, 535, 3);
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

        separatorLabel1 = new JLabel("");
        separatorLabel1.setBounds(4, posElementsOnTheBottom -10, 535, 3);
        separatorLabel1.setBorder(border);
        add(separatorLabel1);

        JLabel instruction4 = new JLabel("Melyik gyűlyteményt kérdezzem ki?");
        instruction4.setBounds(20, posElementsOnTheBottom, 350, 30);
        instruction4.addMouseListener(this);                             //MouseListener
        labels.add(instruction4);

        JLabel info1 = new JLabel("Kiválasztva:");
        info1.setBounds(250, posElementsOnTheBottom +40, 150, 30);
        info1.addMouseListener(this);
        labels.add(info1);

        infoSelectedDict = new JLabel(" \"még semmi\" ");
        infoSelectedDict.setBounds(365, posElementsOnTheBottom +40, 200, 30);
        infoSelectedDict.setFont(new Font("Arial", Font.ITALIC, 16));
        add(infoSelectedDict);

        verticalSeparator1 = new JSeparator();
        verticalSeparator1.setBounds(posElementsOnTheRight,10,2,600);
        verticalSeparator1.setBorder(border);
        add(verticalSeparator1);
        verticalSeparator2 = new JSeparator();
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
    }

    private void setGameArt(Object source){
        if (source.equals(gameButtons.get(0))) {
            SELECTED_GameArt = GameType.GERMAN_TO_HUN;
        }
        if (source.equals(gameButtons.get(1))) {
            SELECTED_GameArt = GameType.ARTICLE_TO_GERMAN;
        }
        if (source.equals(gameButtons.get(2))) {
            SELECTED_GameArt = GameType.HUN_TO_GERMAN;
        }
    }

//MouseEvents
    @Override
    public void mouseEntered(MouseEvent e) {
        if(r > 230) {
            r -= 1;
        }
        if(g > 11) {
            g -= 5;
        }
        if(b < 180) {
            b += 4;
        }
        color = new Color(r,g,b);
        border = BorderFactory.createLineBorder(color);
        separatorLabel1.setBorder(border);
        separatorLabel2.setBorder(border);
        verticalSeparator1.setBorder(border);
        verticalSeparator2.setBorder(border);
    }
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

    //Let's play
    private void startGame(){
        Game game = new Game();
        if(SELECTED_GameArt ==GameType.GERMAN_TO_HUN){
            game = new GGermanToHun(fo);
            image = Toolkit.getDefaultToolkit().getImage("c:\\NemetNeveloGyakorlo\\img\\i_GermanToHun.png");
            setIconImage(image);
        }
        if(SELECTED_GameArt == GameType.ARTICLE_TO_GERMAN){
            image = Toolkit.getDefaultToolkit().getImage("c:\\NemetNeveloGyakorlo\\img\\i_ArticleToGerman.png");
            setIconImage(image);
            game = new GArticleToGerman(fo);
        }
        if(SELECTED_GameArt == GameType.HUN_TO_GERMAN){
            image = Toolkit.getDefaultToolkit().getImage("c:\\NemetNeveloGyakorlo\\img\\i_HunToGerman.png");
            setIconImage(image);
            game = new GHunToGerman(fo);
        }
        createInternalFrame(game);
        System.out.println("Menu 146: megáll-e a create-nél? Nem mi?");
    }

    private void createInternalFrame(JInternalFrame internalFrame) {
        internalFrame.setVisible(true);
        desktopPane.add(internalFrame);
        internalFrame.addInternalFrameListener(new InternalFrameAdapter(){
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                    setIconImage(Toolkit.getDefaultToolkit().getImage(
                            "c:\\NemetNeveloGyakorlo\\img\\i_kekLang.png"));
                    r = 255;
                    g = 165;
                    b = 1;
                    color = new Color(r,g,b);
                    border = BorderFactory.createLineBorder(color);
                    separatorLabel1.setBorder(border);
                    separatorLabel2.setBorder(border);
                    verticalSeparator1.setBorder(border);
                    verticalSeparator2.setBorder(border);
                }
        });

        try {
            internalFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
           new ProblemPopUp("Cannot create Game. Please restart!");
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
        if (dictionaryList.isEmpty()){
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

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Menu::createAndShowGUI);
    }

}
