/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package durak.Decorator;

import java.awt.Color;
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

