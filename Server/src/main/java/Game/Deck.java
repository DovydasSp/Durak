package Game;

import org.bson.types.ObjectId;
import java.util.ArrayList;

public class Deck {
    private ObjectId id;
    private Durak durak;
    private ArrayList<Player> players;
    private ArrayList<Card> cards;

    public Deck(Durak durak, ArrayList<Player> players, ArrayList<Card> cards){
        id = new ObjectId();
        this.durak = durak;
        this.players = players;
        this.cards = cards;
    }
}
