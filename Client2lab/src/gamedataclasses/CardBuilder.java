package gamedataclasses;

import chain.AbstractLogger;
import chain.ChainLogger;

public class CardBuilder {
    private String rank = "";
    private String suit = "";
    private String color = ""; 
    public ChainLogger loggerChain = new ChainLogger();
    
    public CardBuilder setRank(String rank) {
        //System.out.println ("CardBuilder rank set.");
        loggerChain.logMessage(AbstractLogger.PATTERN, "CardBuilder rank set.");
        this.rank = rank;
        return this;
    }

    public CardBuilder setSuit(String suit) {
        loggerChain.logMessage(AbstractLogger.PATTERN,"CardBuilder suit set.");
        this.suit = suit;
        return this;
    }

    public CardBuilder setColor(String color) {
       loggerChain.logMessage(AbstractLogger.PATTERN,"CardBuilder color set.");
        this.color = color;
        return this;
    }
        
    public Card getCard(){
        loggerChain.logMessage(AbstractLogger.PATTERN,"CardBuilder returning new built Card.");
        return new Card(rank, suit, color);
    }
}
