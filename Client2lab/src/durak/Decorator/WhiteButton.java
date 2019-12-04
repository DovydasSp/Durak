package durak.Decorator;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class WhiteButton extends Buttonn {
    public WhiteButton(String name) {
        super(name);
    }
    
    @Override
    public JButton createButton(){
        JButton button = new JButton(getName());
        button.setBackground(Color.white);
        button.setBorder(new LineBorder(Color.BLACK));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBounds(1300,30,80,40);
        return button;
    }
}
