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
        
        public Iterator getIterator(){
            return new FieldPairIterator();
        }
        
        public class FieldPairIterator implements Iterator{
            int index;
    
            @Override
            public boolean hasNext() {
                if(index < pairs.size()){
                    return true;
                }
                return false;
            }

            @Override
            public Object next() {
                if(this.hasNext()){
                    return pairs.get(index++);
                }
                return null;
            }

            @Override
            public boolean hasPrevious() {
                if(index > 0){
                    return true;
                }
                return false;
            }

            @Override
            public Object previous() {
                if(this.hasPrevious()){
                    return pairs.get(--index);
                }
                return null;
            }

            @Override
            public Object first() {
                return pairs.get(0);
            }
        }
}