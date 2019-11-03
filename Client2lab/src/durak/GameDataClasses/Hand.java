package durak.GameDataClasses;

import java.util.*;

public class Hand {

	private ArrayList<Card> cards;

	// Empty hand
	public Hand() {
		cards = new ArrayList<Card>();
	}

	public void add(Card c) {
		cards.add(c);
	}

	public void remove(Card c) {
		cards.remove(c);
	}

	public int size() {
		return cards.size();
	}
        
        public ArrayList<Card> getCards() {
		return cards;
	}
}