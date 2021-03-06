package durak;

import chain.AbstractLogger;
import chain.ChainLogger;
import decorator.Buttonn;
import decorator.GreenButton;
import decorator.WhiteButton;
import decorator.RedButton;
import durak.Game;
import gamedataclasses.Card;
import gamedataclasses.CardPair;
import gamedataclasses.GameData;
import gamedataclasses.Iterator;
import statics.Static;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;
import org.json.JSONException; 
import state.Context;
import state.FirstState;
import statics.Constants;

public class GameUI {

    private Game game;
    private JFrame frame;
    private JPanel backPanel;
    private JPanel infoPanel;
    private JPanel tablePanel;
    private JPanel handCardPanel;
    private Context context;
    
    public ChainLogger loggerChain = new ChainLogger();
    private JPanel chatPanel;
    private JTextArea chatPanelText;

    GameUI(Game game_) {
        game = game_;
        context = new Context();
        FirstState state = new FirstState();
        context.setState(state);
    }

    public void createFrame() {
        frame = new JFrame("Durak");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1916, 1039);
        frame.setBackground(Color.gray);
    }

    public void createBackPanel() {
        backPanel = new JPanel();
        backPanel.setBounds(0, 0, 1600, 1000);
        backPanel.setBackground(Color.black);
    }

    public void createInfoPanel() {
        infoPanel = new JPanel();
        infoPanel.setBounds(0, 0, 1600, 100);
        infoPanel.setBackground(Color.green.darker().darker());
        infoPanel.setLayout(null);
    }

    public void createTablePanel() {
        tablePanel = new JPanel();
        tablePanel.setBounds(0, 100, 1600, 500);
        tablePanel.setBackground(Color.green.darker().darker());
        tablePanel.setLayout(null);
    }

    public void createHandCardPanel() {
        handCardPanel = new JPanel();
        handCardPanel.setBounds(0, 600, 1600, 400);
        handCardPanel.setBackground(Color.DARK_GRAY);
    }

    public void createChatPanel() {
        chatPanel = new JPanel();
        chatPanel.setBounds(1600, 0, 300, 1000);
        chatPanel.setLayout(null);
        
        chatPanelText = new JTextArea();
        chatPanelText.setEditable(false);
        chatPanelText.setBounds(1600, 0, 300, 950);
        
        JScrollPane scrollPanel = new JScrollPane(chatPanelText);
        scrollPanel.setBounds(1600, 0, 300, 950);
        
        JTextField tf1 = new JTextField();
        tf1.setBounds(1600,950,250,50);
        
        JButton sendButton = new JButton("SEND");
        sendButton.setBackground(Color.white);
        sendButton.setBorder(new LineBorder(Color.BLACK));
        sendButton.setBounds(1850, 950, 50, 50);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String s1=tf1.getText();
                    tf1.setText("");
                    String isCommand = s1.substring(0, 1);
                    if(isCommand.equals("/")){
                        game.chatCommand(s1);
                    }
                    else{
                        if(context.getState().stateNr() < 4)
                        {
                            chatPanelText.append("YOU: "+s1+"\n");
                        }
                        //game.sendInput(Constants.COMMAND_CHAT, s1);
                        context.getState().doAction(context, game, s1);
                    }
                } catch (Exception ex){}
            }
        });
        
        chatPanel.add(scrollPanel);
        chatPanel.add(tf1);
        chatPanel.add(sendButton);
    }
    
    public void addChatMessage(String sender, String message){
        chatPanelText.append(sender+": "+message+"\n");
        if(!sender.equals("DURAK")){
            FirstState state = new FirstState();
            context.setState(state);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getBackPanel() {
        return backPanel;
    }

    public JPanel getInfoPanel() {
        return infoPanel;
    }

    public JPanel getTablePanel() {
        return tablePanel;
    }

    public JPanel getHandCardPanel() {
        return handCardPanel;
    }

    public JPanel getChatPanel() {
        return chatPanel;
    }
    
    public JTextArea getChatPanelText(){
        return chatPanelText;
    }

    public void drawGameBoard() {
        frame.add(infoPanel);
        frame.add(tablePanel);
        frame.add(handCardPanel);
        frame.add(backPanel);
        frame.add(chatPanel);
        frame.setVisible(true);
    }
    
    public void refreshPlayer(GameData gd) throws IOException, JSONException, InterruptedException{
        //System.out.println("Refreshing player");
        loggerChain.logMessage(AbstractLogger.INFO, "Refreshing player");
        if(gd.getWhatsChangedInPlayer().equals("hand")){
            refreshHandCardPanel(gd);
        } else {
            if (gd.getWhatsChangedInPlayer().equals("yourTurn") || gd.getWhatsChanged().equals("gameEnd")) {
                refreshHandCardPanel(gd);
                refreshInfoPanel(gd);
            } else {
                refreshInfoPanel(gd);
            }
        }
    }

    private void refreshInfoPanel(GameData gd) {
        infoPanel.removeAll();

        String userName = gd.getPlayer().getPlayerName();
        String trump = gd.getPlayer().getTrump();

        if (!userName.equals("")) {
            JLabel usernameLabel = new JLabel("UserName: " + userName);
            usernameLabel.setBounds(0, 0, 400, 25);
            usernameLabel.setForeground(Color.white);
            infoPanel.add(usernameLabel);
        }

        JLabel roleLabel = new JLabel("You are attaker: " + gd.getPlayer().getIsAttacker());
        roleLabel.setBounds(0, 25, 400, 25);
        roleLabel.setForeground(Color.white);
        infoPanel.add(roleLabel);

        if (!trump.equals("")) {
            String suitSymbol = Static.symbols.get(trump);
            JLabel trumpLabel = new JLabel(suitSymbol);
            trumpLabel.setBounds(750, 0, 100, 100);
            if (Static.colors.get(trump).equals("Red")) {
                trumpLabel.setForeground(Color.red);
            }
            if (Static.colors.get(trump).equals("Black")) {
                trumpLabel.setForeground(Color.black);
            }
            trumpLabel.setFont(new Font("Arial", Font.PLAIN, 100));
            infoPanel.add(trumpLabel);
        }

        JLabel turnLabel = new JLabel("Your turn: " + gd.getPlayer().getYourTurn());
        turnLabel.setBounds(0, 50, 400, 25);
        turnLabel.setForeground(Color.white);
        infoPanel.add(turnLabel);

        JLabel oponentCardCount = new JLabel("Oponent card count: " + gd.getPlayer().getOponentCardCount());
        oponentCardCount.setBounds(0, 75, 200, 25);
        oponentCardCount.setForeground(Color.white);
        infoPanel.add(oponentCardCount);

        JLabel deckCardCount = new JLabel("Deck card count: " + gd.getPlayer().getDeckCardCount());
        deckCardCount.setBounds(200, 75, 200, 25);
        deckCardCount.setForeground(Color.white);
        infoPanel.add(deckCardCount);

        //loggerChain.logMessage(AbstractLogger.PATTERN, "Decorator: White button was requested");
                Buttonn bu = new WhiteButton("UNDO");
                JButton button0 = bu.createButton();               
                button0.addActionListener(new ActionListener(){  
                    @Override
                    public void actionPerformed(ActionEvent e){  
                        try {
                            game.sendInput(Constants.COMMAND_UNDO, "");
                        } catch (Exception ex) {
                            Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }});
                infoPanel.add(button0);
        
        if(gd.getPlayer().getYourTurn() && gd.getField().getPairCount() > 0){
            if(gd.getPlayer().getIsAttacker()){
               //loggerChain.logMessage(AbstractLogger.PATTERN, "Decorator: Green button was requested");
                Buttonn b = new GreenButton("DONE", bu);
                JButton button = b.createButton();
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            game.sendInput(Constants.COMMAND_ROUND_END, "");
                        } catch (Exception ex) {
                            Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            infoPanel.add(button);
            }
            else{
                //loggerChain.logMessage(AbstractLogger.PATTERN, "Decorator: Red button was requested");
                Buttonn b = new RedButton("TAKE", bu);
                JButton button = b.createButton();
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            game.sendInput(Constants.COMMAND_ROUND_END, "");
                        } catch (Exception ex) {
                            Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                infoPanel.add(button);
            }
        }
       // System.out.println("Refreshed infoPanel");
        loggerChain.logMessage(AbstractLogger.INFO, "Refreshed infoPanel");
        infoPanel.revalidate();
        infoPanel.repaint();
    }
    
    public void refreshField(GameData gd){
       // System.out.println("Refreshing field");
       loggerChain.logMessage(AbstractLogger.INFO, "Refreshing field");
        tablePanel.removeAll();
        tablePanel.revalidate();
        tablePanel.repaint();
        if(gd.getWhatsChanged().equals("gameEnd")){
            //System.out.println("Refreshed UI to game ending screen");
            loggerChain.logMessage(AbstractLogger.INFO,"Refreshed UI to game ending screen");
            JLabel wonLabel = new JLabel();
            if (gd.getPlayer().getWon()) {
                wonLabel = new JLabel("You won! ☺");
            } else {
                wonLabel = new JLabel("You lost...");
            }
            wonLabel.setBounds(750, 250, 150, 50);
            wonLabel.setForeground(Color.white);
            wonLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            tablePanel.add(wonLabel);

            JButton button = new JButton("PLAY AGAIN");
            button.setBackground(Color.white);
            button.setBorder(new LineBorder(Color.BLACK));
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.setBounds(700, 300, 200, 100);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        game.join();
                    } catch (Exception ex) {
                        Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            tablePanel.add(button);
            tablePanel.revalidate();
            tablePanel.repaint();
        } else {
            int nr = 1;
            //for(CardPair c : gd.getField().getPairs()){
            for (Iterator iter = gd.getField().getIterator(); iter.hasNext();) {
                CardPair c = (CardPair) iter.next();
                //System.out.println(c.getAttacker().getColor()+" "+c.getAttacker().getRank()+" "+c.getAttacker().getSuit());
                createTableCardButton(c.getAttacker(), nr, false);
                if (c.isCompleted()) {
                    //System.out.println(c.getDefender().getColor()+" "+c.getDefender().getRank()+" "+c.getDefender().getSuit());
                    createTableCardButton(c.getDefender(), nr, true);
                }
                nr++;
            }
        }
    }

    private void createTableCardButton(Card card, int cardNr, boolean isDefending) {
        String suitSymbol = Static.symbols.get(card.getSuit());

        String color = card.getColor();
        JButton button = new JButton(suitSymbol + card.getRank());
        button.setBackground(Color.white);
        button.setBorder(new LineBorder(Color.BLACK));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        if (color.equals("Red")) {
            button.setForeground(Color.red);
        }
        if (color.equals("Black")) {
            button.setForeground(Color.black);
        }
        int x = 150;
        int y = 50;
        if (cardNr > 1) {
            x = x + (100 + 140) * (cardNr - 1);
        }
        if (isDefending) {
            y = 250;
        }
        button.setBounds(x, y, 100, 170);
        tablePanel.add(button);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    private void refreshHandCardPanel(GameData gd) throws IOException, JSONException, InterruptedException {
        handCardPanel.removeAll();
        handCardPanel.revalidate();
        handCardPanel.repaint();
        int cardNr = 1;
        //for(Card c : gd.getPlayer().getHand().getCards()){
        for (Iterator iter = gd.getPlayer().getHand().getIterator(); iter.hasNext();) {
            Card c = (Card) iter.next();
            createCardButton(gd, c, cardNr);
            cardNr++;
        }
        loggerChain.logMessage(AbstractLogger.INFO,"Refreshed hand card panel");
       // System.out.println("Refreshed hand card panel");
    }

    private void createCardButton(GameData gd, Card card, int cardNr) throws IOException, JSONException, InterruptedException {
        String suitSymbol = Static.symbols.get(card.getSuit());

        String color = card.getColor();
        JButton button = new JButton(suitSymbol + card.getRank());
        button.setBackground(Color.white);
        button.setBorder(new LineBorder(Color.BLACK));
        button.setPreferredSize(new Dimension(100, 170));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        if (color.equals("Red")) {
            button.setForeground(Color.red);
        }
        if (color.equals("Black")) {
            button.setForeground(Color.black);
        }
        if (gd.getPlayer().getYourTurn()) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        game.sendInput(cardNr, "");
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
