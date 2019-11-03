/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package durak.Decorator;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 *
 * @author Admin
 */
public class GreenButton extends Decorator {

    public GreenButton(String name, Buttonn inner) {
        super(name, inner);
    }
    
    
    @Override
    public JButton createButton(){
        JButton button = new JButton(getName());
        button.setBackground(Color.green);
        button.setBorder(new LineBorder(Color.BLACK));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBounds(1460,30,120,40);
        return button;
    }
}
