package decorator;

import javax.swing.JButton;

public abstract class Decorator extends Buttonn{
    
    private Buttonn innerElement;
    
    public Decorator( String name, Buttonn inner) {
        super(name);
        this.innerElement = inner;
    }
    
    public abstract JButton createButton();
}
