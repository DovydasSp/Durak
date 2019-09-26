package Game;

import org.bson.types.ObjectId;

public class Player {
    private ObjectId id;
    private String name;
    private int count;
    private boolean attacker;
    private Deck deck;
    private Hand hand;
    public Player(String name, Deck deck, Hand hand, boolean attacker){
        id = new ObjectId();
        this.name = name;
        this.deck = deck;
        this.hand = hand;
        this.attacker = attacker;
        count = 0;
    }
}
