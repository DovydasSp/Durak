package durak;

import java.awt.Color;
import javax.swing.JFrame;

/*
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;*/


public class Durak {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Durak");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1616, 1000);
        frame.setBackground(Color.gray);
        
        GameJoinUI durak = new GameJoinUI(frame);
        durak.start();
    }
}
