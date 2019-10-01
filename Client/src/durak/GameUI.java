package durak;

import durak.Static.Static;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;


public class GameUI {

    public void drawGameBoard(String gameID, String playerID, String userName) {
        
        //Creating the Frame
        JFrame frame = new JFrame("Durak");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1616, 1000);
        
        JPanel backPanel = new JPanel();
        backPanel.setBounds(0,0,1600,1000);
        backPanel.setBackground(Color.black);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(0,0,1600,100);
        infoPanel.setBackground(Color.green.darker().darker());
        infoPanel.setLayout(null);
        
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(0,100,1600,500);
        tablePanel.setBackground(Color.green.darker().darker());
        //tablePanel.setLayout(null);
        
        JLabel usernameLabel=new JLabel("UserName: "+userName);  
        usernameLabel.setBounds(0,0,100,30);
        usernameLabel.setForeground(Color.white);
        infoPanel.add(usernameLabel);
        
        JPanel handCardPanel = new JPanel();
        handCardPanel.setBounds(0,600,1600,400);
        handCardPanel.setBackground(Color.DARK_GRAY);
        
        createButton("6", Static.suits[0], handCardPanel, tablePanel);
        createButton("7", Static.suits[1], handCardPanel, tablePanel);
        createButton("8", Static.suits[2], handCardPanel, tablePanel);
        createButton("9", Static.suits[3], handCardPanel, tablePanel);
        createButton("10", Static.suits[0], handCardPanel, tablePanel);
        createButton("Jack", Static.suits[1], handCardPanel, tablePanel);
        createButton("Queen", Static.suits[2], handCardPanel, tablePanel);
        createButton("King", Static.suits[3], handCardPanel, tablePanel);
        createButton("Ace", Static.suits[0], handCardPanel, tablePanel);
        createButton("Queen", Static.suits[1], handCardPanel, tablePanel);
        createButton("Queen", Static.suits[2], handCardPanel, tablePanel);
        createButton("Queen", Static.suits[3], handCardPanel, tablePanel);
        createButton("Queen", Static.suits[0], handCardPanel, tablePanel);
        createButton("Queen", Static.suits[1], handCardPanel, tablePanel);
        createButton("Queen", Static.suits[2], handCardPanel, tablePanel);
        createButton("Queen", Static.suits[3], handCardPanel, tablePanel);
        
        frame.add(infoPanel);
        frame.add(tablePanel);
        frame.add(handCardPanel);
        frame.add(backPanel);
        
        frame.setVisible(true);
    }
    
    private void createButton(String rank, String suit, JPanel handCardPanel, JPanel tablePanel)
    {
        String suitSymbol = "";
        switch(suit){
            case "Hearts":
                suitSymbol = "♥";
                break;
            case "Diamonds":
                suitSymbol = "♦";
                break;        
            case "Clubs":
                suitSymbol = "♣";
                break;        
            case "Spades":
                suitSymbol = "♠";
                break;
        }
        String color = Static.colors.get(suit);
        JButton button = new JButton(suitSymbol+rank);
        button.setBackground(Color.white);
        button.setBorder(new LineBorder(Color.BLACK));
        button.setPreferredSize(new Dimension(100, 170));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        if(color.equals("Red")){
            button.setForeground(Color.red);
        } if(color.equals("Black"))
        {
            button.setForeground(Color.black);
        }
        button.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                 JOptionPane.showMessageDialog(handCardPanel,suit+" "+rank);
                 handCardPanel.remove(button);
                 handCardPanel.revalidate();
                 handCardPanel.repaint();
                 tablePanel.add(button);
                 tablePanel.revalidate();
                 tablePanel.repaint();
            }
        });  
        handCardPanel.add(button);
    }
}
