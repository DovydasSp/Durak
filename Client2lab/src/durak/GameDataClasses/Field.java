package durak.GameDataClasses;

import java.util.*;

public class Field {
	private ArrayList<CardPair> pairs;
        private int pairCount = 0;
	private boolean completed;

	// Empty field
	public Field() {
		pairs = new ArrayList<CardPair>();
		completed = false;
	}
        
        public void addPair(CardPair cardPair){
            pairs.add(cardPair);
        }
        
        public void setCompleted(){
            completed = true;
        }
        
        public ArrayList<CardPair> getPairs(){
            return pairs;
        }
        
        public int getPairCount(){
            return pairCount;
        }
        
        public void setPairCount(int p){
            pairCount = p;
        }
}