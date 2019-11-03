/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package durak.Decorator;

import java.awt.Color;
import javax.swing.JButton;

public abstract class Decorator extends Buttonn{
    
    private Buttonn innerElement;
    
    public Decorator( String name, Buttonn inner) {
        super(name);
        this.innerElement = inner;
    }
    
    public abstract JButton createButton();
}
