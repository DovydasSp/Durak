package durak.Decorator;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class RedButton extends Decorator {
    public RedButton(String name, Buttonn inner) {
        super(name, inner);
    }

    @Override
    public JButton createButton(){
        JButton button = new JButton(getName());
        button.setBackground(Color.red);
        button.setBorder(new LineBorder(Color.BLACK));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBounds(1510,30,60,40);
        return button;
    }
}
