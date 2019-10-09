package durak.Factory;

import java.awt.Color;

public class UnitFactory extends Factory {
    
    @Override
	public Button getButtons( String param, String name ) {
		// TODO Auto-generated method stub
		if(param.equals("red")) {
			return new RedButton(Color.red, name);
		}
		if(param.equals("green")) {
			return new GreenButton(Color.green, name);
		}
		if(param.equals("white")) {
			return new WhiteButton(Color.white, name);
		}
		return null;
	}
}
