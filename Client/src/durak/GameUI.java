package durak;

import durak.GameDataClasses.Card;
import durak.GameDataClasses.CardPair;
import durak.GameDataClasses.Field;
import durak.GameDataClasses.Hand;
import durak.Static.Static;
import durak.Threads.tablePollingThread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;
import org.json.JSONException;


public class GameUI {
    GameConnectionToAPI connection = new GameConnectionToAPI();
    JFrame frame;
    String gameID;
    String playerID;

    GameUI(JFrame frame0, String gameID_, String playerID_) {
        frame = frame0;
        gameID = gameID_;
        playerID = playerID_;
    }

    public void drawGameBoard(String userName) throws IOException, ProtocolException, JSONException, InterruptedException{
        
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
        tablePanel.setLayout(null);
        
        JPanel handCardPanel = new JPanel();
        handCardPanel.setBounds(0,600,1600,400);
        handCardPanel.setBackground(Color.DARK_GRAY);
        
        String trump = "";
        while(trump.equals("")){
            trump = connection.getTrump(playerID, gameID);
            Thread.sleep(100);
        }
        String suitSymbol = Static.symbols.get(trump);

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
        
        frame.add(infoPanel);
        frame.add(tablePanel);
        frame.add(handCardPanel);
        frame.add(backPanel);
        
        frame.setVisible(true);
        
        handPolling(infoPanel, tablePanel, handCardPanel);
    }
    
    private void handPolling(JPanel infoPanel, JPanel tablePanel, JPanel handCardPanel) throws IOException, ProtocolException, JSONException, InterruptedException{     
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
        
        Hand hand = null;
        while(hand == null){
            hand = connection.getPlayersHand(playerID, gameID);
            Thread.sleep(500);
        }
        
        refreshHand(handCardPanel, hand, tablePanel, role);
        tablePolling(tablePanel, handCardPanel);
    }
    
    private void tablePolling(JPanel tablePanel, JPanel handCardPanel) throws IOException, ProtocolException, JSONException, InterruptedException{     
        Field field = null;

        tablePollingThread thread = new tablePollingThread(connection, playerID, gameID);
        FieldObserver fo = new FieldObserver(this, handCardPanel, tablePanel);
        thread.addObserver(fo);
        Thread t = new Thread(thread);
        t.start();
        };
    
    private void refreshHand(JPanel handCardPanel, Hand hand, JPanel tablePanel, String role) throws IOException, ProtocolException, JSONException, InterruptedException{
        handCardPanel.removeAll();
        handCardPanel.revalidate();
        handCardPanel.repaint();
        int cardNr=1;
        for(Card c : hand.getCards()){
            createCardButton(c, handCardPanel, role, cardNr);
            cardNr++;
        }
    }
    
    void refreshTable(JPanel handCardPanel, JPanel tablePanel, Field field){
        //tikrint cia ar yra kortu kurias reikia kirst ir pagal tai createCardButton buttonus enablint
        int nr = 1;
        for(CardPair c : field.getPairs()){
            //System.out.println(c.getAttacker().getColor()+" "+c.getAttacker().getRank()+" "+c.getAttacker().getSuit());
            createTableCardButton(c.getAttacker(), tablePanel, nr, false);
            if(c.isCompleted()){
                //System.out.println(c.getDefender().getColor()+" "+c.getDefender().getRank()+" "+c.getDefender().getSuit());
                createTableCardButton(c.getDefender(), tablePanel, nr, true);
            }
            nr++;
        }
    }
    
    private void createCardButton(Card card, JPanel handCardPanel, String role, int cardNr) throws IOException, ProtocolException, JSONException, InterruptedException
    {
        String suitSymbol = Static.symbols.get(card.getSuit());
        
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
        if(/*role.equals("attacker")*/true){
            button.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                try {
                    String success = connection.input(playerID, gameID, cardNr);
                    if(success.equals("success")){
                        handCardPanel.remove(button);
                        handCardPanel.revalidate();
                        handCardPanel.repaint();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                 
                
            }
        });
        }
        handCardPanel.add(button);
        handCardPanel.revalidate();
        handCardPanel.repaint();
    }
    
    private void createTableCardButton(Card card, JPanel tablePanel, int cardNr, boolean isDefending)
    {
        String suitSymbol = Static.symbols.get(card.getSuit());
        
        String color = card.getColor();
        JButton button = new JButton(suitSymbol+card.getRank());
        button.setBackground(Color.white);
        button.setBorder(new LineBorder(Color.BLACK));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        if(color.equals("Red")){
            button.setForeground(Color.red);
        } if(color.equals("Black"))
        {
            button.setForeground(Color.black);
        }
        int x = 150;
        int y = 50;
        if(cardNr > 1){
            x = x + (100+140)*(cardNr-1);
        }
        if(isDefending){
            y = 250;
        }
        button.setBounds(x,y,100,170);
        tablePanel.add(button);
        tablePanel.revalidate();
        tablePanel.repaint();
    }
}
