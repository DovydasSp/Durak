package gamedataclasses;

import chain.AbstractLogger;
import chain.ChainLogger;
import java.util.*;

public class Hand {

    private ArrayList<Card> cards;
    private ChainLogger loggerChain = new ChainLogger();

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

    public Iterator getIterator() {
        return new HandCardIterator();
    }

    public class HandCardIterator implements Iterator {

        int index;

        @Override
        public boolean hasNext() {
            loggerChain.logMessage(AbstractLogger.PATTERN,"Iterator of hand called hasNext.");
            if (index < cards.size()) {
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            loggerChain.logMessage(AbstractLogger.PATTERN,"Iterator of hand called next.");
            if (this.hasNext()) {
                return cards.get(index++);
            }
            return null;
        }

        @Override
        public boolean hasPrevious() {
            loggerChain.logMessage(AbstractLogger.PATTERN,"Iterator of hand called hasPrevious.");
            if (index > 0) {
                return true;
            }
            return false;
        }

        @Override
        public Object previous() {
            loggerChain.logMessage(AbstractLogger.PATTERN,"Iterator of hand called previous.");
            if (this.hasPrevious()) {
                return cards.get(--index);
            }
            return null;
        }

        @Override
        public Object first() {
            loggerChain.logMessage(AbstractLogger.PATTERN,"Iterator of hand called first.");
            return cards.get(0);
        }
    }
}
