package durak;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;


public class GameJoinUI{
    GameConnectionToAPI connection = new GameConnectionToAPI();
    JFrame frame;
    
    JTextField tf1, tf2;
    JButton b1,b2;
    JLabel l1,l2;

    GameJoinUI(JFrame frame0) {
        frame = frame0;
    }
    
    public void start(){
        frame.setTitle("Durak - Create or join a game");
        
        l1=new JLabel("Enter username");  
        l1.setBounds(750,300, 100,30);
        l2=new JLabel("Enter game id");  
        l2.setBounds(750,450, 100,30);
        
        tf1=new JTextField();
        tf1.setBounds(700,350, 200,30);  
        tf2=new JTextField();
        tf2.setBounds(700,500, 200,30); 
        
        b1=new JButton("Create game");  
        b1.setBounds(700,400,200,30);  
        b2=new JButton("Join game");  
        b2.setBounds(700,550,200,30);  
        b1.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                 String s1=tf1.getText();
                 
                 if(!s1.isEmpty())
                 {
                     Pair<String, String> pair;
                     try {
                         pair = connection.createGame(s1);
                         System.out.println("GameID: "+pair.getKey()+" PlayerID: "+pair.getValue());
                         //JOptionPane.showMessageDialog(frame,"Created. PlayerName: "+s1+" GameID: "+pair.getKey()+" PlayerID: "+pair.getValue());
                         frame.getContentPane().removeAll();
                         frame.repaint();
                         GameUI game = new GameUI(frame);
                         game.drawGameBoard(pair.getKey(), pair.getValue(), s1);
                     } catch (Exception ex) {
                         Logger.getLogger(GameJoinUI.class.getName()).log(Level.SEVERE, null, ex);
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
                         //JOptionPane.showMessageDialog(frame,"Joined. PlayerName: "+s1+" GameID: "+pair.getKey()+" PlayerID: "+pair.getValue());
                         frame.getContentPane().removeAll();
                         frame.repaint();
                         GameUI game = new GameUI(frame);
                         game.drawGameBoard(pair.getKey(), pair.getValue(), s1);
                     } catch (Exception ex) {
                         Logger.getLogger(GameJoinUI.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
            }
        });
        
        frame.add(l1); frame.add(tf1); frame.add(b1);
        frame.add(l2); frame.add(tf2); frame.add(b2); 
        
        frame.setLayout(null);
        frame.setVisible(true);
    }    
}