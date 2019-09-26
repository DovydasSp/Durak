package Game;

import org.bson.types.ObjectId;
import java.util.ArrayList;

public class Hand {
    private ObjectId id;
    private Player player;
    private ArrayList<Card> cards;
    public Hand(Player player, ArrayList<Card> cards){
        id = new ObjectId();
        this.player = player;
        this.cards = cards;
    }
}
