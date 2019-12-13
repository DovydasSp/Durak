package factory;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class RedButton extends Button {
    public RedButton(Color background, String name) {
        super(background, name);
    }
    
    @Override
    public JButton createButton(){
        JButton button = new JButton(getName());
        button.setBackground(getBackground());
        button.setBorder(new LineBorder(Color.BLACK));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBounds(1510,30,60,40);
        //System.out.println ("FACTORY: Red button was created and returned");
        return button;
    }
    
}
