package durak.GameDataClasses;

public class Card {

	private String rank;
	private String suit;
	private String color; 
        
        public Card() {
        }
        
        public Card(String rank_, String suit_, String color_) {
            rank = rank_;
            suit = suit_;
            color = color_;
        }
        
        public String getSuit() {
		return suit;
	}

	public String getColor() {
		return color;
	}

	public String getRank() {
		return rank;
	}
}