package Game;

import org.bson.types.ObjectId;

enum Ranks {
    SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}
enum Suits{
    HEARTS, DIAMONDS, CLUBS, SPADERS
}

enum Colors{
    BLACK, RED
}

public class Card {
    private ObjectId id;
    private Ranks rank;
    private Suits suit;
    private Colors color;

    public Card(Ranks rank, Suits suit, Colors color){
        id = new ObjectId();
        this.rank = rank;
        this.suit = suit;
        this.color = color;
    }
}
