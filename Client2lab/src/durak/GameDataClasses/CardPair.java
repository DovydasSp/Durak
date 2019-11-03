package durak.GameDataClasses;

public class CardPair {
	private Card attacker;
	private Card defender;

	private boolean completed;

	public CardPair(Card a, Card d, boolean completed_) {
		attacker = a; 
                defender = d;
		completed = completed_; // Completed means: did it receive a response (does it have a defender?)
	}
        
        public boolean isCompleted(){
            return completed;
        }
        
        public Card getAttacker(){
            return attacker;
        }
        
        public Card getDefender(){
            return defender;
        }
}