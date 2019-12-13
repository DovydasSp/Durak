package factory;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class GreenButton extends Button {
    public GreenButton(Color background, String name) {
        super(background, name);
    }
    
    @Override
    public JButton createButton(){
        JButton button = new JButton(getName());
        button.setBackground(getBackground());
        button.setBorder(new LineBorder(Color.BLACK));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBounds(1460,30,120,40);
        //System.out.println ("FACTORY: Green button was created and returned");
        return button;
    }
}
