package durak;

import durak.GameDataClasses.GameData;
import durak.GameDataClasses.Player;
import java.awt.Color;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;


public class GameJoin{
    private final GameConnectionToAPI connection;
    private final GameUI gameUI;
    private final Game game;
    
    JTextField tf1, tf2;
    JButton b1,b2;
    JLabel l1,l2;

    GameJoin(GameConnectionToAPI connection_, GameUI gameUI_, Game game_) {
        connection = connection_;
        gameUI = gameUI_;
        game = game_;
    }
    
    public void join(){
        gameUI.getTablePanel().removeAll();
        gameUI.getTablePanel().revalidate();
        gameUI.getTablePanel().repaint();
        gameUI.getInfoPanel().removeAll();
        gameUI.getInfoPanel().revalidate();
        gameUI.getInfoPanel().repaint();
        gameUI.getHandCardPanel().removeAll();
        gameUI.getHandCardPanel().revalidate();
        gameUI.getHandCardPanel().repaint();
        
        gameUI.getFrame().setTitle("Durak - Create or join a game");
        
        l1=new JLabel("Enter username"); 
        l1.setForeground(Color.white);
        l1.setBounds(750,100, 100,30);
        l2=new JLabel("Enter game id");
        l2.setForeground(Color.white);
        l2.setBounds(750,250, 100,30);
        
        tf1=new JTextField();
        tf1.setBounds(700,150, 200,30);  
        tf2=new JTextField();
        tf2.setBounds(700,300, 200,30); 
        
        b1=new JButton("Create game");  
        b1.setBounds(700,200,200,30);  
        b2=new JButton("Join game");  
        b2.setBounds(700,350,200,30);  
        b1.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                 String s1=tf1.getText();
                 
                 if(!s1.isEmpty())
                 {
                     Pair<String, String> pair;
                     try {
                         pair = connection.createGame(s1);
                         System.out.println("GameID: "+pair.getKey()+" PlayerID: "+pair.getValue());
                         gameUI.getTablePanel().removeAll();
                         gameUI.getTablePanel().revalidate();
                         gameUI.getTablePanel().repaint();
                         Player p = new Player();
                         p.setPlayerIds(s1, pair.getValue(), pair.getKey());
                         GameData gd = new GameData();
                         gd.setPlayer(p);
                         game.play(gd);
                     } catch (Exception ex) {
                         Logger.getLogger(GameJoin.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
            }
        }); 
        b2.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                String s1=tf1.getText();
                 String s2=tf2.getText();
                 if(!s1.isEmpty() && !s2.isEmpty())
                 {
                     Pair<String, String> pair;
                     try {
                         pair = connection.joinGame(s1, s2);
                         System.out.println("GameID: "+pair.getKey()+" PlayerID: "+pair.getValue());
                         gameUI.getTablePanel().removeAll();
                         gameUI.getTablePanel().revalidate();
                         gameUI.getTablePanel().repaint();
                         Player p = new Player();
                         p.setPlayerIds(s1, pair.getValue(), pair.getKey());
                         GameData gd = new GameData();
                         gd.setPlayer(p);
                         game.play(gd);
                     } catch (Exception ex) {
                         Logger.getLogger(GameJoin.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
            }
        });
        
        gameUI.getTablePanel().add(l1); gameUI.getTablePanel().add(tf1); gameUI.getTablePanel().add(b1);
        gameUI.getTablePanel().add(l2); gameUI.getTablePanel().add(tf2); gameUI.getTablePanel().add(b2); 
        
        gameUI.getFrame().revalidate();
        gameUI.getFrame().repaint();
    }    
}