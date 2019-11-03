package durak;

import durak.Factory.Factory;
import durak.Factory.UnitFactory;
import durak.Factory.Button;
import durak.GameDataClasses.Card;
import durak.GameDataClasses.CardPair;
import durak.GameDataClasses.GameData;
import durak.Static.Static;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;
import org.json.JSONException;

public class GameUI {
    private Game game;
    private JFrame frame;
    private JPanel backPanel;
    private JPanel infoPanel;
    private JPanel tablePanel;
    private JPanel handCardPanel;
    
    GameUI(Game game_){
        game = game_;
    }
    
    public void createFrame(){
        frame = new JFrame("Durak");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1616, 1000);
        frame.setBackground(Color.gray);
    }
    
    public void createBackPanel(){
        backPanel = new JPanel();
        backPanel.setBounds(0,0,1600,1000);
        backPanel.setBackground(Color.black);
    }
    
    public void createInfoPanel(){
        infoPanel = new JPanel();
        infoPanel.setBounds(0,0,1600,100);
        infoPanel.setBackground(Color.green.darker().darker());
        infoPanel.setLayout(null);
    }
    
    public void createTablePanel(){
        tablePanel = new JPanel();
        tablePanel.setBounds(0,100,1600,500);
        tablePanel.setBackground(Color.green.darker().darker());
        tablePanel.setLayout(null);
    }
    
    public void createHandCardPanel(){
        handCardPanel = new JPanel();
        handCardPanel.setBounds(0,600,1600,400);
        handCardPanel.setBackground(Color.DARK_GRAY);
    }
    
    public JFrame getFrame(){
        return frame;
    }
    
    public JPanel getBackPanel(){
        return backPanel;
    }
    
    public JPanel getInfoPanel(){
        return infoPanel;
    }
    
    public JPanel getTablePanel(){
        return tablePanel;
    }
    
    public JPanel getHandCardPanel(){
        return handCardPanel;
    }
    
    public void drawGameBoard(){
        frame.add(infoPanel);
        frame.add(tablePanel);
        frame.add(handCardPanel);
        frame.add(backPanel);
        frame.setVisible(true);
    }
    
    public void refreshPlayer(GameData gd) throws IOException, JSONException, InterruptedException{
        System.out.println("Refreshing player");
        if(gd.getWhatsChangedInPlayer().equals("hand")){
            refreshHandCardPanel(gd);
        }
        else{
            if(gd.getWhatsChangedInPlayer().equals("yourTurn") || gd.getWhatsChanged().equals("gameEnd")){
                refreshHandCardPanel(gd);
                refreshInfoPanel(gd);
            }
            else{
                refreshInfoPanel(gd);   
            }
        }
    }
    
    private void refreshInfoPanel(GameData gd){
        infoPanel.removeAll();
        
        String userName = gd.getPlayer().getPlayerName();
        String trump = gd.getPlayer().getTrump();
        
        if(!userName.equals("")){
            JLabel usernameLabel=new JLabel("UserName: "+userName);  
            usernameLabel.setBounds(0,0,400,25);
            usernameLabel.setForeground(Color.white);
            infoPanel.add(usernameLabel);
        }

        JLabel roleLabel=new JLabel("You are attaker: "+gd.getPlayer().getIsAttacker());  
        roleLabel.setBounds(0,25,400,25);
        roleLabel.setForeground(Color.white);
        infoPanel.add(roleLabel);
        
        if(!trump.equals("")){
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
        }
        
        JLabel turnLabel=new JLabel("Your turn: "+gd.getPlayer().getYourTurn());  
        turnLabel.setBounds(0,50,400,25);
        turnLabel.setForeground(Color.white);
        infoPanel.add(turnLabel);
        
        JLabel oponentCardCount=new JLabel("Oponent card count: "+gd.getPlayer().getOponentCardCount());  
        oponentCardCount.setBounds(0,75,200,25);
        oponentCardCount.setForeground(Color.white);
        infoPanel.add(oponentCardCount);
        
        JLabel deckCardCount=new JLabel("Deck card count: "+gd.getPlayer().getDeckCardCount());  
        deckCardCount.setBounds(200,75,200,25);
        deckCardCount.setForeground(Color.white);
        infoPanel.add(deckCardCount);
        
        Factory factory = new UnitFactory();
        
        if(gd.getPlayer().getYourTurn() && gd.getField().getPairCount() > 0){
            if(gd.getPlayer().getIsAttacker()){
                System.out.println ("FACTORY: Green button was requested");
                Button b = factory.getButtons("green", "DONE");
                JButton button = b.createButton();               
                button.addActionListener(new ActionListener(){  
                    @Override
                    public void actionPerformed(ActionEvent e){  
                        try {
                            game.sendInput(0);
                        } catch (Exception ex) {
                            Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }});
                infoPanel.add(button);
            }
            else{
                System.out.println ("FACTORY: Red button was requested");
                Button b = factory.getButtons("red", "TAKE");
                JButton button = b.createButton(); 
                button.addActionListener(new ActionListener(){  
                    @Override
                    public void actionPerformed(ActionEvent e){  
                        try {
                            game.sendInput(0);
                        } catch (Exception ex) {
                            Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }});
                infoPanel.add(button);
            }
        }
        System.out.println("Refreshed infoPanel");
        infoPanel.revalidate();
        infoPanel.repaint();
    }
    
    public void refreshField(GameData gd){
        System.out.println("Refreshing field");
        tablePanel.removeAll();
        tablePanel.revalidate();
        tablePanel.repaint();
        if(gd.getWhatsChanged().equals("gameEnd")){
            System.out.println("Refreshed UI to game ending screen");
            JLabel wonLabel = new JLabel();
            if(gd.getPlayer().getWon()){
                wonLabel=new JLabel("You won! ☺");
            }
            else{
                wonLabel=new JLabel("You lost...");
            }
            wonLabel.setBounds(750,250,150,50);
            wonLabel.setForeground(Color.white);
            wonLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            tablePanel.add(wonLabel);
            
            JButton button = new JButton("PLAY AGAIN");
                button.setBackground(Color.white);
                button.setBorder(new LineBorder(Color.BLACK));
                button.setFont(new Font("Arial", Font.PLAIN, 20));
                button.setBounds(700,300,200,100);
                button.addActionListener(new ActionListener(){  
                    @Override
                    public void actionPerformed(ActionEvent e){  
                        try {
                            game.join();
                        } catch (Exception ex) {
                            Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }});
                tablePanel.add(button);
            
            tablePanel.revalidate();
            tablePanel.repaint();
        }
        else{
           int nr = 1;
            for(CardPair c : gd.getField().getPairs()){
                //System.out.println(c.getAttacker().getColor()+" "+c.getAttacker().getRank()+" "+c.getAttacker().getSuit());
                createTableCardButton(c.getAttacker(), nr, false);
                if(c.isCompleted()){
                    //System.out.println(c.getDefender().getColor()+" "+c.getDefender().getRank()+" "+c.getDefender().getSuit());
                    createTableCardButton(c.getDefender(), nr, true);
                }
                nr++;
            } 
        }
    }
    
    private void createTableCardButton(Card card, int cardNr, boolean isDefending)
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
    
    private void refreshHandCardPanel(GameData gd) throws IOException, JSONException, InterruptedException{
        handCardPanel.removeAll();
        handCardPanel.revalidate();
        handCardPanel.repaint();
        int cardNr=1;
        for(Card c : gd.getPlayer().getHand().getCards()){
            createCardButton(gd, c, cardNr);
            cardNr++;
        }
        System.out.println("Refreshed hand card panel");
    }
    
    private void createCardButton(GameData gd, Card card, int cardNr) throws IOException, JSONException, InterruptedException
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
        if(gd.getPlayer().getYourTurn()){
            button.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e){  
                try {
                    game.sendInput(cardNr);
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
    
    
    
}