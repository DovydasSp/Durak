package factory;

import java.awt.Color;

public class UnitFactory extends Factory {  
    @Override
    public Button getButtons( String param, String name ) {
	if(param.equals("red")) {
            System.out.println ("FACTORY: Red button was requested, created and returned");
            return new RedButton(Color.red, name);
	}
	if(param.equals("green")) {
            System.out.println ("FACTORY: Green button was requested, created and returned");
            return new GreenButton(Color.green, name);
	}
	if(param.equals("white")) {
            System.out.println ("FACTORY: White button was requested, created and returned");
            return new WhiteButton(Color.white, name);
	}
	return null;
    }
}
