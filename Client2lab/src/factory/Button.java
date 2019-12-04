package factory;

import java.awt.Color;
import javax.swing.JButton;

public abstract class Button {
    private Color background;
    private String name;

    public Button(Color background, String name) {
        this.background = background;
        this.name = name;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public JButton createButton(){
        return new JButton();
    }
}
