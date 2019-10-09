package durak.GameDataClasses;

public class CardBuilder {
    private String rank;
    private String suit;
    private String color; 

    public CardBuilder setRank(String rank) {
        this.rank = rank;
        return this;
    }

    public CardBuilder setSuit(String suit) {
        this.suit = suit;
        return this;
    }

    public CardBuilder setColor(String color) {
        this.color = color;
        return this;
    }
        
    public Card getCard(){
        return new Card(rank, suit, color);
    }
}
