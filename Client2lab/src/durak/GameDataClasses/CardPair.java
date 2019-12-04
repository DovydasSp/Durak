package durak.GameDataClasses;

public class CardPair implements Cloneable {

    private Card attacker;
    private Card defender;

    private boolean completed;

    public CardPair(Card a, Card d, boolean completed_) {
        attacker = a;
        defender = d;
        completed = completed_; // Completed means: did it receive a response (does it have a defender?)
    }

    public boolean isCompleted() {
        return completed;
    }

    public Card getAttacker() {
        return attacker;
    }

    public Card getDefender() {
        return defender;
    }

    public void setAttacker(Card a) {
        attacker = a;
    }

    public void setDefender(Card d) {
        defender = d;
    }

    public void setCompleted(boolean c) {
        completed = c;
    }

    public CardPair ShallowCopy(CardPair cardPair) throws CloneNotSupportedException {
        CardPair copy = (CardPair) cardPair.clone();
        int copyAddress = System.identityHashCode(copy);
        int cardPairAddress = System.identityHashCode(cardPair);
        System.out.println("CardPair shallow copy made. CardPair: " + cardPairAddress + ", Copy: " + copyAddress);
        return copy;
    }

    public CardPair DeepCopy(CardPair cardPair) throws CloneNotSupportedException {
        CardPair copy = (CardPair) cardPair.clone();
        copy.attacker = (Card) attacker.clone();
        copy.defender = (Card) defender.clone();
        int copyAddress = System.identityHashCode(copy);
        int cardPairAddress = System.identityHashCode(cardPair);
        System.out.println("CardPair deep copy made. CardPair: " + cardPairAddress + ", Copy: " + copyAddress);
        return copy;
    }
}
