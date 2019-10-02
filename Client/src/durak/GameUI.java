package durak;

import durak.GameClasses.*;
import durak.Static.Static;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import javax.swing.border.LineBorder;
import org.json.JSONException;


public class GameUI {
    GameConnectionToAPI connection = new GameConnectionToAPI();
    JFrame frame;

    GameUI(JFrame frame0) {
        frame = frame0;
    }

    public void drawGameBoard(String gameID, String playerID, String userName) throws IOException, ProtocolException, JSONException, InterruptedException{
        
        frame.setTitle("Durak - "+userName);
        
        JPanel backPanel = new JPanel();
        backPanel.setBounds(0,0,1600,1000);
        backPanel.setBackground(Color.black);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(0,0,1600,100);
        infoPanel.setBackground(Color.green.darker().darker());
        infoPanel.setLayout(null);
        
        JLabel usernameLabel=new JLabel("UserName: "+userName);  
        usernameLabel.setBounds(0,0,400,30);
        usernameLabel.setForeground(Color.white);
        infoPanel.add(usernameLabel);
        
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(0,100,1600,500);
        tablePanel.setBackground(Color.green.darker().darker());
        
        JPanel handCardPanel = new JPanel();
        handCardPanel.setBounds(0,600,1600,400);
        handCardPanel.setBackground(Color.DARK_GRAY);
        
        frame.add(infoPanel);
        frame.add(tablePanel);
        frame.add(handCardPanel);
        frame.add(backPanel);
        
        frame.setVisible(true);
        
        polling(gameID, playerID, infoPanel, tablePanel, handCardPanel);
    }
    
    private void polling(String gameID, String playerID, JPanel infoPanel, JPanel tablePanel, JPanel handCardPanel) throws IOException, ProtocolException, JSONException, InterruptedException{     
        String role = "";
        while(role.equals("")){
            role = connection.getRole(playerID, gameID);
            Thread.sleep(500);
        }
        JLabel roleLabel=new JLabel("Role: "+role);  
        roleLabel.setBounds(0,30,400,30);
        roleLabel.setForeground(Color.white);
        infoPanel.add(roleLabel);
        infoPanel.revalidate();
        infoPanel.repaint();
        
        String trump = "";
        while(trump.equals("")){
            trump = connection.getTrump(playerID, gameID);
            Thread.sleep(100);
        }
        String suitSymbol = "";
        switch(trump){
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
        JLabel trumpLabel=new JLabel(suitSymbol);  
        trumpLabel.setBounds(750,0,100,100);
        if(Static.colors.get(trump).equals("Red")){
            trumpLabel.setForeground(Color.red);
        } if(Static.colors.get(trump).equals("Black"))
        {
            trumpLabel.setForeground(Color.black);
        }
        trumpLabel.setFont(new Font("Arial", Font.PLAIN, 100));
        infoPanel.add(trumpLabel);
        infoPanel.revalidate();
        infoPanel.repaint();
        
        //connection.getPlayersHand(playerID, gameID);
        
        
        //temp code to immitate getting cards from api
        Card c0 = new Card(Static.ranks[0],Static.suits[0],Static.colors.get(Static.suits[0]));
        Card c1 = new Card(Static.ranks[1],Static.suits[1],Static.colors.get(Static.suits[1]));
        Card c2 = new Card(Static.ranks[2],Static.suits[2],Static.colors.get(Static.suits[2]));
        Card c3 = new Card(Static.ranks[3],Static.suits[3],Static.colors.get(Static.suits[3]));
        Card c4 = new Card(Static.ranks[4],Static.suits[0],Static.colors.get(Static.suits[0]));
        Card c5 = new Card(Static.ranks[5],Static.suits[1],Static.colors.get(Static.suits[1]));
        Card c6 = new Card(Static.ranks[6],Static.suits[2],Static.colors.get(Static.suits[2]));
        Card c7 = new Card(Static.ranks[7],Static.suits[3],Static.colors.get(Static.suits[3]));
        Card c8 = new Card(Static.ranks[8],Static.suits[0],Static.colors.get(Static.suits[0]));
        
        Hand hand = new Hand();
        hand.add(c0);
        hand.add(c1);
        hand.add(c2);
        hand.add(c3);
        hand.add(c4);
        hand.add(c5);
        hand.add(c6);
        hand.add(c7);
        hand.add(c8);
        
        refreshHand(handCardPanel, hand, tablePanel);
    }
    
    private void refreshHand(JPanel handCardPanel, Hand hand, JPanel tablePanel){
        handCardPanel.removeAll();
        handCardPanel.revalidate();
        handCardPanel.repaint();
        
        for(Card c : hand.getCards()){
            createButton(c, handCardPanel, tablePanel);
        }
    }
    
    private void refreshTable(){
        
    }
    
    private void createButton(Card card, JPanel handCardPanel, JPanel tablePanel)
    {
        String suitSymbol = "";
        switch(card.getSuit()){
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
        String color = card.getColor();
        JButton button = new JButton(suitSymbol+card.getRank());
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
                 JOptionPane.showMessageDialog(handCardPanel,card.getSuit()+" "+card.getRank());
                 handCardPanel.remove(button);
                 handCardPanel.revalidate();
                 handCardPanel.repaint();
                 tablePanel.add(button);
                 tablePanel.revalidate();
                 tablePanel.repaint();
            }
        });  
        handCardPanel.add(button);
        handCardPanel.revalidate();
        handCardPanel.repaint();
    }
}
