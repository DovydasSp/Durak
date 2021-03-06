package Game;
import Server.Message;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {

    private Hand hand;
    private String name;
    private final ObjectId id;
    private Deck deck;
    //private static int count = 0;
    private boolean attacker;
    private Stack<Integer> input = new Stack<>();
    private Stack<String> messages = new Stack<>();

    // Create a new Player with an empty hand and set to draw from a default deck (does not draw).
    public Player() {
        //count++;
        id = new ObjectId();
        name = "Player " + id;
        hand = new Hand();
        deck = new Deck();
        attacker = false;
    }

    // Create a new Player and draw five cards from given deck.
    public Player(Deck d) {
        //count++;
        id = new ObjectId();
        name = "Player " + id;
        hand = new Hand();
        deck = d;
        drawCards(6);
        attacker = false;

    }

    // Create a new Player and draw five cards from given deck, with given name.
    public Player(Deck d, String n) {
        //count++;
        id = new ObjectId();
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
        addMessage(Message.formPlayersHand(hand));
    }

    public void takeCard(Card c) {
        hand.add(c);
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
        addMessage(Message.formRole(attacker));
    }

    public void makeDefender() {
        attacker = false;
        addMessage(Message.formRole(attacker));
    }

    public void switchRole() {
        attacker = !attacker;
        addMessage(Message.formRole(attacker));
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
    }

    public ObjectId getID() { return id; }

    public void setName(String name) { this.name = name; }

    public void addInput(int Input){
        synchronized(input) {
            this.input.add(Input);
        }
    }

    public int getInput() throws InterruptedException {
        while(input.size() == 0){
            Thread.sleep(300);
        }
        synchronized(input){
            int res = input.get(0);
            input.remove(0);
            return res;
        }
    }

    public void addMessage(JSONObject msg){ messages.add(msg.toString()); }
    public String popMessage() {
        synchronized (messages){
            if(messages.size() != 0){
                String res = messages.get(0);
                messages.remove(0);
                return res;
            }
            return Message.formNoMessages().toString();
        }
    }

    public void setDeck(Deck deck){ this.deck = deck; }
    public Hand getHand() {return hand;}
}