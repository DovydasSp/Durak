package durak.GameDataClasses;

import java.io.*;
import java.util.*;
import durak.Static.*;
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
        
        public void setPlayerIds(String playerName_, String playerID_, String gameID_){
            playerName = playerName_;
            playerID = playerID_;
            gameID = gameID_;
        }
        
        public String getPlayerName(){
            return playerName;
        }
        
        public Pair<String, String> getIDs(){
            return new Pair<>(gameID, playerID);
        }
        
        public void setHand(Hand hand_){
            hand = hand_;
        }
        
        public Hand getHand(){
            return hand;
        }
        
        public void setTrump(String trump_){
            trump = trump_;
        }
        
        public String getTrump(){
            return trump;
        }
        
        public void setIsAttacker(boolean isAttacker_){
            isAttacker = isAttacker_;
        }
        
        public boolean getIsAttacker(){
            return isAttacker;
        }
        
        public void setYourTurn(boolean yourTurn_){
            yourTurn = yourTurn_;
        }
        
        public boolean getYourTurn(){
            return yourTurn;
        }
        
        public void setOponentCardCount(int oponentCardCount_){
            oponentCardCount = oponentCardCount_;
        }
        
        public int getOponentCardCount(){
            return oponentCardCount;
        }
        
        public void setWon(boolean won_){
            won = won_;
        }
        
        public boolean getWon(){
            return won;
        }
        
        public void setDeckCardCount(int deckCardCount_){
            deckCardCount = deckCardCount_;
        }
        
        public int getDeckCardCount(){
            return deckCardCount;
        }
        
/*
	// Create a new Player and draw five cards from given deck.
	public Player(Deck d) {
		count++; 
		id = count; 
		name = "Player " + id;
		hand = new Hand();
		deck = d;
		drawCards(6);
		attacker = false;
		
	}

	// Create a new Player and draw five cards from given deck, with given name.
	public Player(Deck d, String n) {
		count++; 
		id = count; 
		name = n;
		hand = new Hand();
		deck = d;
		drawCards(6);
		attacker = false;
	}

	// Draws a card from given deck.
	// If empty, nothing happens.
	public void draw(Deck d) {
			if (!d.isEmpty()) {
				Card thisCard = d.draw();
				hand.add(thisCard);
			}
	}

	// Draws card from associated deck (instance variable).
	public void draw() {
		draw(deck);
	}

	// Draw n cards from given deck.
	public void drawCards(Deck d, int n) {
		for (int i = 0; i < n; i++) {
			draw(deck);
		}
	}

	// Draw n cards from associated deck (instance variable).
	public void drawCards(int n) {
		for (int i = 0; i < n; i++) {
			draw();
		}
	}

	public void takeCard(Card c) {
		hand.add(c);
	}

	// Discards a given card from Player's hand.
	public void discard(Card c) {
		hand.remove(c);
	}

	public int cardsInHand() {
		return hand.size();
	}

	// Draw cards until reaching 6
	public void replenish() {
		int toDraw = hand.numberToDraw();
		drawCards(toDraw);
	}

	// Victory check
	public boolean victoryAchieved() {
		return ((hand.size() <= 0) && (deck.isEmpty()));
	}

	public boolean isAttacker() {
		return attacker;
	}

	public void makeAttacker() {
		attacker = true;
	}

	public void makeDefender() {
		attacker = false;
	}

	public void switchRole() {
		attacker = !attacker;
	}

	@Override
	public String toString() {
		return name;
	}

	// *** For turn-making purposes ***

	// Hand ArrayList index-based retrieval for making turns in the command line
	// Here, i represents a natural number as referred to during the game.
	// It is reduced by one to zero-based and passed in that form to Hand,
	// which takes zero-based inputs.

	public Card getCard(int i) {
		return hand.getCardByIndex(i - 1);
	}

	public Card useCard(int i) {
		return hand.useCardByIndex(i - 1);
	}

	// Counter corresponds to (ArrayList index + 1)
	public String cardList() {
		String ret = "\n=== YOUR CARDS ===\n";
		ArrayList<Card> cards = hand.getCards();
		int i = 1;
		for (Card card : cards) {
			ret += i + " ~ " + card + "\n";
			i += 1;
		}
		ret += "\n\n";
		return ret;
	}*/

}