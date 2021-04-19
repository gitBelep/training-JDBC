package nemetNeveloGyakorlo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ScoreDisplay extends JInternalFrame implements ActionListener {
    private Container contentPane;
    private JPanel panel1;
    private JButton button;
    private JPanel panel2;
    private JPanel panel3;
    private Color color1 = new Color(20, 110, 200, 90);
    private Color color2 = new Color(25, 170, 120, 90);
    FileOperations fo;

    public ScoreDisplay(FileOperations fo) {
        this.fo = fo;
//        " Dicsőségtábla "
//        Image icon = Toolkit.getDefaultToolkit().getImage("c:\\NemetNeveloGyakorlo\\img\\data-mining2.png");
        contentPane = this.getContentPane();
        contentPane.setVisible(true);
        contentPane.setBackground(new Color(150,200,200,140));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPanels();
        addComponentsToPanel1();
        addComponentsToPanel2();
        addComponentsToPanel3();
        pack();           //Display the window.
        setVisible(true);
        setSize(800,800);
    }

    private void addPanels() {
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints cc = new GridBagConstraints();

        panel1 = new JPanel()
        //       { @Override  public Dimension getPreferredSize() {return new Dimension(750,300); }}
        ;
        panel1.setVisible(true);
        cc.ipady = 47;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.weightx = 1;
        cc.weighty = 0;
        cc.gridx = 0;
        cc.gridy = 0;
        contentPane.add(panel1, cc);
        panel1.setOpaque(false);

        panel2 = new JPanel();
        panel2.setVisible(true);
        cc.ipady = 70;
        cc.fill = GridBagConstraints.HORIZONTAL;
        cc.weightx = 1;
        cc.weighty = 0;
        cc.gridx = 0;
        cc.gridy = 1;
        contentPane.add(panel2, cc);
        panel2.setBorder(BorderFactory.createBevelBorder(1, color2, color1));
        panel2.setOpaque(false);

        panel3 = new JPanel();
        panel3.setVisible(true);
        cc.ipady = 653;
        cc.fill = GridBagConstraints.BOTH;
        cc.weightx = 1;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.gridy = 2;
        contentPane.add(panel3, cc);
        panel3.setOpaque(false);
    }

    private void addComponentsToPanel1(){              //panel 1
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel space00 = new JLabel("          ");
        space00.setFont(new Font("Arial", Font.BOLD, 18));
        c.ipady = 30;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        panel1.add(space00, c);

        Color stripeBgColor = new Color(20,160,100, 20);

        JLabel stripe = new JLabel("");
        stripe.setFont(new Font("Arial", Font.BOLD, 12));
        stripe.setOpaque(true);
        stripe.setBorder(BorderFactory.createSoftBevelBorder(1, color1, color2));
        stripe.setBackground(stripeBgColor);
        stripe.setBounds(0,0, 180, 20);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 0;
        panel1.add(stripe, c);

        JLabel space02 = new JLabel("          ");
        space02.setFont(new Font("Arial", Font.BOLD, 18));
        c.ipady = 30;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 2;
        c.gridy = 0;
        panel1.add(space02, c);

        button = new JButton(" BEZÁR ");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.addActionListener(this);
        c.fill = GridBagConstraints.NONE;
        c.ipady = 30;
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 3;
        c.gridy = 0;
        panel1.add(button, c);

    }

    private void addComponentsToPanel2() {
        fo.gainBestPlayers();
        String[] column2 = {"NÉV", "ÖSSZEGYŰJTÖTT PONTSZÁM", "JÁTÉKOK SZÁMA"};
        String[][] data2 = get5BestPlayers();

        JTable jt2 = new JTable(data2, column2);
        jt2.getColumn("NÉV").setMinWidth(130);
        jt2.getColumn("ÖSSZEGYŰJTÖTT PONTSZÁM").setMinWidth(150);
        JScrollPane sp2 = new JScrollPane(jt2);
//        sp2.setMinimumSize(new Dimension(700, 1));  //nem szélesíti
        panel2.add(sp2);
    }

    private void addComponentsToPanel3(){           //panel 3
        fo.gainOldScores();
        String[] column3 = {"NÉV", "SZÓTÁR", "PONTSZÁM","JÁTÉK", "DÁTUM"};
        String[][] data3 = provideLastScores();

        panel3.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JTable jt3 = new JTable(data3, column3);
        JScrollPane sp3 = new JScrollPane(jt3);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        panel3.add(sp3, c);
    }

    private String[][] get5BestPlayers(){
        FileOperations.BEST_PLAYERS.sort((b1,b2) -> b2.getSumScores() - b1.getSumScores());
        int maxFive = Math.min(FileOperations.BEST_PLAYERS.size(), 5);
        String[][] data = new String[maxFive][];
        for(int i = 0; i < maxFive; i++){
            BestPlayer b = FileOperations.BEST_PLAYERS.get(i);
            String[] player = {b.getName(), ""+ b.getSumScores(), ""+ b.getSumRounds()};
            data[i] = Arrays.copyOf(player, 3);
        }
        return data;
    }

    public String[][] provideLastScores(){
        String[][] last25Scores = new String[FileOperations.OLD_RESULTS.size()][];
        for(int i = 0; i < FileOperations.OLD_RESULTS.size(); i++){
            Result r = FileOperations.OLD_RESULTS.get(i);
            String[] row = new String[5];
            row[0] = r.getName();
            row[1] = r.getDict();
            row[2] = ""+ ( r.getPoints() < 10 ? " "+ r.getPoints() : r.getPoints() );
            row[3] = r.getGameType().getArt();
            DateTimeFormatter dtFormatterThisYear = DateTimeFormatter.ofPattern("MM.dd. HH:mm");
            DateTimeFormatter dtFormatterBefore = DateTimeFormatter.ofPattern("yyyy.MM.");
            LocalDateTime thisYear = LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0,0);
            if( r.getLdt().isBefore(thisYear) ) {
                row[4] = r.getLdt().format(dtFormatterBefore);
            } else{
                row[4] = r.getLdt().format(dtFormatterThisYear);
            }
            last25Scores[i] = row;
        }
        return last25Scores;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(button)){
            closeInternalFrame();
        }
    }

    private void closeInternalFrame(){
        try {
            this.setClosed(true);
        } catch (PropertyVetoException e) {
            ProblemPopUp pp = new ProblemPopUp("Please restart!");
        }
    }

}