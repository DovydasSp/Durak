package gamedataclasses;

public class CardBuilder {
    private String rank = "";
    private String suit = "";
    private String color = ""; 

    public CardBuilder setRank(String rank) {
        System.out.println ("CardBuilder rank set.");
        this.rank = rank;
        return this;
    }

    public CardBuilder setSuit(String suit) {
        System.out.println ("CardBuilder suit set.");
        this.suit = suit;
        return this;
    }

    public CardBuilder setColor(String color) {
        System.out.println ("CardBuilder color set.");
        this.color = color;
        return this;
    }
        
    public Card getCard(){
        System.out.println ("CardBuilder returning new built Card.");
        return new Card(rank, suit, color);
    }
}
