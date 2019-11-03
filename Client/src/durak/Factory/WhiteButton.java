package durak.Factory;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class WhiteButton extends Button {

    public WhiteButton(Color background, String name) {
        super(background, name);
    }
    
    @Override
    public JButton createButton(){
        JButton button = new JButton(getName());
        button.setBackground(getBackground());
        button.setBorder(new LineBorder(Color.BLACK));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBounds(1500,30,80,40);
        System.out.println ("FACTORY: White button was created and returned");
        return button;
    }
}
