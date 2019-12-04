package durak.Decorator;

import javax.swing.JButton;

public abstract class Buttonn {
    private String name;

    public Buttonn(String name) {
        this.name = name;
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

