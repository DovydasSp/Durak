package durak.GameDataClasses;

import javafx.util.Pair;

public class Player {

    private String playerName;
    private String playerID;
    private String gameID;
    private Hand hand;
    private String trump;
    private boolean isAttacker;
    private boolean yourTurn;
    private int oponentCardCount;
    private int deckCardCount;
    private int roundCount;
    private boolean newRound;
    private boolean won;

    public Player() {
        playerName = "";
        playerID = "";
        gameID = "";
        trump = "";
        hand = new Hand();
        isAttacker = false;
        yourTurn = false;
        oponentCardCount = 6;
        deckCardCount = 36;
        roundCount = 1;
        won = false;
    }

    public void setPlayerIds(String playerName_, String playerID_, String gameID_) {
        playerName = playerName_;
        playerID = playerID_;
        gameID = gameID_;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Pair<String, String> getIDs() {
        return new Pair<>(gameID, playerID);
    }

    public void setHand(Hand hand_) {
        hand = hand_;
    }

    public Hand getHand() {
        return hand;
    }

    public void setTrump(String trump_) {
        trump = trump_;
    }

    public String getTrump() {
        return trump;
    }

    public void setIsAttacker(boolean isAttacker_) {
        isAttacker = isAttacker_;
    }

    public boolean getIsAttacker() {
        return isAttacker;
    }

    public void setYourTurn(boolean yourTurn_) {
        yourTurn = yourTurn_;
    }

    public boolean getYourTurn() {
        return yourTurn;
    }

    public void setOponentCardCount(int oponentCardCount_) {
        oponentCardCount = oponentCardCount_;
    }

    public int getOponentCardCount() {
        return oponentCardCount;
    }

    public void setWon(boolean won_) {
        won = won_;
    }

    public boolean getWon() {
        return won;
    }

    public void setDeckCardCount(int deckCardCount_) {
        deckCardCount = deckCardCount_;
    }

    public int getDeckCardCount() {
        return deckCardCount;
    }
}
