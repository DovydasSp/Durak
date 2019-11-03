package Game;
import Server.DB;
import Server.Message;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class Durak implements Runnable, Serializable {
    public static String TRUMP;
    private ObjectId id;
    private Player one;
    private Player two;
    private Deck deck;
    private int round; // Natural number
    private Player attacker;
    private Player defender;
    private Player whosTurn;
    private Field currentField;
    private boolean roundInitiated; // If a round is occurring and has completed its initiation stages
    private boolean startAgain = false;
    private Stack<Integer> playerOneInput = new Stack<>();
    private Stack<Integer> playerTwoInput = new Stack<>();
    private Stack<String> durakBinaries = new Stack<>();

    //public Scanner sc = new Scanner(System.in);
    public Random r = new Random();
    public Stack<Integer> input = new Stack<>();

    // Set default trump to hearts
    public Durak() {
        this.id = new ObjectId();
        one = new Player();
        two = new Player();
    }

    public void run() {
        try {
            if(startAgain == false) {
                startAgain = true;
                start();
            }
            else
                startDurakAgain();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() throws InterruptedException {
        boolean running = true;
        r.setSeed(4564654654L);
        setup();
        game();
        System.out.println("The game has ended.");
    }

    // Setting up for a game instance
    public void setup() {
        System.out.println("Welcome to Durak!");

        System.out.println("\nEnter the name of Player 1...\n");
        //String oneName = sc.nextLine();
        System.out.println("\nEnter the name of Player 2...\n");
        //String twoName = sc.nextLine();

        System.out.println("\nPlease wait!\n");
        System.out.println("Creating game...\n");

        System.out.println("Shuffling the cards...\n");
        deck = new Deck();

        System.out.println("Dealing the cards...\n");
        //one = new Player(deck, one.getName());
        one.setDeck(deck);
        one.drawCards(6);
        //two = new Player(deck, two.getName());
        two.setDeck(deck);
        two.drawCards(6);
        System.out.println("Determining trump card...\n");
        Card trumpCard = deck.draw();
        String trumpSuit = trumpCard.getSuit();
        TRUMP = trumpSuit;

        System.out.println("The trump is: " + TRUMP + "!\n");

        System.out.println("Reinserting trump card...\n");
        deck.reinsert(trumpCard);

        System.out.println("Resetting round count...\n");
        round = 1;

        System.out.println("The game is ready.\n");
    }

    // Running a game instance
    public void game() throws InterruptedException {
        System.out.println("Determining initial attacker...\n");
        if (r.nextInt(2) < 1) {
            setAttacker(one);
            setDefender(two);
        } else {
            setAttacker(two);
            setDefender(one);
        }
        one.addMessage(Message.formTrump(TRUMP));
        two.addMessage(Message.formTrump(TRUMP));

        System.out.println("The initial attacker is: " + attacker + ".");
        System.out.println("The initial defender is: " + defender + ".\n\n");

        // Round creation & handling until victoryAchieved()

        boolean gameOver = false;

        while (!gameOver) {

            boolean thisRound = round(); // Run a round
            one.addMessage(Message.formRoundEnd());
            two.addMessage(Message.formRoundEnd());
            if (victoryAchieved()) {
                // Victory was achieved by the playing of cards at some point
                // Arrived here after breaking out of the round instantly
                gameOver = true; // Break out of the game loop
            } else {
                // No victory was achieved
                // Draw cards, last attacker first
                attacker.replenish();
                defender.replenish();
                round++; // Increment round number
                if (thisRound) {
                    // Attacker won the round
                    // Attacker goes again; no switching occurs
                } else {
                    // Defender won the round
                    // Roles switch
                    switchRoles();
                }
            }
        }

        System.out.println("Game over!\n");
        System.out.println("The winner is " + determineWinner() + "!\n");
        if(determineWinner() == one) {
            one.addMessage(Message.formGameEnd(true));
            two.addMessage(Message.formGameEnd(false));
        }else{
            one.addMessage(Message.formGameEnd(false));
            two.addMessage(Message.formGameEnd(true));
        }
        return;
    }

    // Victory check
    public boolean victoryAchieved() {
        return ((one.victoryAchieved() || two.victoryAchieved()));
    }


    // Determines the winner.
    // Should only be called if victoryAchieved returns true.
    public Player determineWinner() {
        if (victoryAchieved()) {
            if (one.victoryAchieved()) {
                return one;
            } else {
                return two;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    // Runs a single round, given references to attacker and defender
    // Returns true if attacker succeeded
    // Returns false if defender succeeded
    public boolean round() throws InterruptedException {
        // Create references to attacker and defender


        // Generate header
        String roundName = "ROUND " + round;
        String headerLine = "==================== " + roundName + " ====================" + "\n";
        String headerContent = "Attacker: " + attacker + " | " + "Defender: " + defender + "\n";
        String header = "\n\n\n" + headerLine + headerContent + headerLine + "\n\n\n";

        roundInitiated = false; // Round is in initial stages

        System.out.println(header);
        System.out.println(roundName + " has begun!\n");

        // Initiation of round and its associated field
        attacker.addMessage(Message.formDeckCount(deck));
        defender.addMessage(Message.formDeckCount(deck));
        defender.addMessage(Message.formEnemyCardCount(attacker.getHand()));
        attacker.addMessage(Message.formEnemyCardCount(defender.getHand()));

        System.out.println(attacker + ", initiate the attack!");
        whosTurn = attacker;
        int initialAttack = playerInput(attacker);

        attacker.addMessage(Message.formInput(1));
        Card initialAttackCard = attacker.useCard(initialAttack);
        announceCardPlayed(attacker, initialAttackCard);
        attacker.addMessage(Message.formPlayersHand(attacker.getHand()));
        defender.addMessage(Message.formEnemyCardCount(attacker.getHand()));
        //storeDurakToDB();
        //  (CARD PLAYED: Check for victory!)

        if (victoryAchieved()) {
            return true; // Pop out of round (In game() screen, there should be a victoryAchieved() check after each round to immediately proceed from here)
        }

        Field roundField = new Field(initialAttackCard); // Generate a Field
        currentField = roundField;
        roundInitiated = true;
        one.addMessage(Message.formField(currentField));
        two.addMessage(Message.formField(currentField));

        while (!roundField.isCompleted()) {
            whosTurn = defender;
            boolean defenderTurn = defenderResponse(roundField);
            one.addMessage(Message.formField(currentField));
            two.addMessage(Message.formField(currentField));
            //storeDurakToDB();
            if (defenderTurn || victoryAchieved()) {
                // Defender took the cards, ended the round
                // OR as a result of the defender's turn, victory was achieved (CARD PLAYED: Check for victory!)
                roundInitiated = false;
                currentField = null;
                //switchRoles();
                return true; // Pop out of round
            }
            defender.addMessage(Message.formPlayersHand(defender.getHand()));
            attacker.addMessage(Message.formEnemyCardCount(defender.getHand()));
            // Defender responded by playing a card

            whosTurn = attacker;
            boolean attackerTurn = attackerResponse(roundField);
            one.addMessage(Message.formField(currentField));
            two.addMessage(Message.formField(currentField));
            //storeDurakToDB();
            if (attackerTurn || victoryAchieved()) {
                // Attacker declared the round to be over
                // OR as a result of the attacker's turn, victory was achieved (CARD PLAYED: Check for victory!)
                roundInitiated = false;
                currentField = null;
                return false; // Pop out of round
            }
            attacker.addMessage(Message.formPlayersHand(attacker.getHand()));
            defender.addMessage(Message.formEnemyCardCount(attacker.getHand()));
        }
        return true; // Satisfy Java
    }

    public void announceCardPlayed(Player p, Card c) {
        if (p.isAttacker()) {
            System.out.println("\n\n" + p + " has played " + c + ", initiating a new pair!");
        } else {
            System.out.println("\n\n" + p + " has played " + c + " in response!");
        }
    }

    // Action: prints turn selection dialogue to console
    public void turnPrompt(Player player) {
        boolean isAttacker = player.isAttacker();

        String prompt = new String("\n\n"); // StartHeaderline + Precontent + Content + Tail + EndHeaderline
        String promptStartLine = "\n============ PROMPT ============\n\n";
        String promptEndLine = "\n========== END PROMPT ==========\n\n";
        String preContent = new String("CURRENT TRUMP: " + TRUMP + "\n\n");
        preContent += "CARDS REMAINING IN DECK: " + deck.size() + "\n\n";
        preContent += "CARDS REMAINING IN HAND: " + player.cardsInHand() + "\n\n";
        String fieldString;
        String content = new String();
        String tail = new String("=== MESSAGE ===\n\n");

        // Card selection component (same for everyone)
        content += player.cardList();
        content += "=== OTHER OPTIONS ===\n\n";

        if (isAttacker) {
            // Attacker (split)
            if (roundInitiated) {
                // Attacker in midst of round
                preContent += "# ATTACK CONTINUATION #\n\n";
                content += "0 | Beaten\n\n";
            } else {
                // Attacker initiating round
                preContent += "# ATTACK INITIATION #\n\n";
                content += "<none>\n\n";
            }
            tail += player + ", you're attacking!\n";
        } else {
            // Defender at any time
            preContent += "# DEFENSE #\n\n";
            content += "0 | Take\n\n";
            tail += player + ", you're defending!\n";
        }

        tail += "Make your move by hitting the corresponding key.\n";

        // Combining

        if (currentField == null) {
            fieldString = "<Empty Field>\n\n";
        } else {
            fieldString = "" + currentField;
        }

        prompt += promptStartLine + fieldString + preContent + content + tail + promptEndLine;

        System.out.println(prompt);
    }

    // Prints player options and accepts input
    // Handles improper input (not in terms of playable cards, but if it was available as a selection numerically)
    public int playerInput(Player p) throws InterruptedException {
        boolean isAttacker = p.isAttacker();

        turnPrompt(p);
        if(p == attacker){
            attacker.addMessage(Message.formPlayerTurnEvent(true));
            defender.addMessage(Message.formPlayerTurnEvent(false));
        }
        else{
            attacker.addMessage(Message.formPlayerTurnEvent(false));
            defender.addMessage(Message.formPlayerTurnEvent(true));
        }

        int playerSelection = -1;
        boolean properInput = false;
        while (!properInput) {
            playerSelection = p.getInput();
            if (isAttacker) {
                if (roundInitiated) {
                    // Valid input: 0 through and including hand size
                    properInput = ((playerSelection >= 0) && (playerSelection <= p.cardsInHand()));
                } else {
                    // Valid input: 1 through and including hand size
                    properInput = ((playerSelection >= 1) && (playerSelection <= p.cardsInHand()));
                }
            } else {
                // Valid input: 0 through and including hand size
                properInput = ((playerSelection >= 0) && (playerSelection <= p.cardsInHand()));
            }
            if (!properInput) {
                System.out.println("Invalid input. Please enter an acceptable value.");
                p.addMessage(Message.formInput(2));
            }
        }
        return playerSelection;
    }


    // Returns a boolean.
    // Indicates whether or not round was ended by defender.
    // If ended: returns true. Field has been closed. Defender has taken cards.
    // If not ended: returns false. A card has been played in response.
    public boolean defenderResponse(Field f) {
        int defenderResponse = -1;
        boolean properDefenderResponse = false;
        while (!properDefenderResponse) {
            try { // Is this a valid defender? Try to use it.
                defenderResponse = playerInput(defender);
                if (defenderResponse != 0) {
                    // This is a card.
                    Card defenderResponseCard = defender.getCard(defenderResponse); // getCard() is like peeking
                    f.respond(defenderResponseCard);
                    properDefenderResponse = true;
                    defender.useCard(defenderResponse); // useCard() is like committing to the card
                    announceCardPlayed(defender, defenderResponseCard);
                    defender.addMessage(Message.formInput(1));
                    f.getPlayedRanks().add(defenderResponseCard.getRank());
                    return false;
                } else {
                    // This is not a card.
                    // This is a request to take all cards and end the round (field).
                    properDefenderResponse = true;
                    System.out.println("\n\n" + defender + " has chosen to take all cards in the field and end the round!");
                    ArrayList<Card> takenCards = f.fetchAllCards();
                    for (Card card : takenCards) {
                        defender.takeCard(card);
                    }
                    f.endField();
                    defender.addMessage(Message.formInput(1));
                    return true;
                }
            } catch (IllegalArgumentException | InterruptedException e) { // Invalid defender, another one will be solicited
                System.out.println("\n\nInvalid defender!");
                defender.addMessage(Message.formInput(2));
                properDefenderResponse = false;
            }
        }
        return true; // Satisfy Java
    }

    // Returns a boolean.
    // Indicates whether or not round was ended by attacker.
    // If ended: returns true. Field has been closed. Round over. No one took cards.
    // If not ended: returns false. A card has been played in the field by the attacker.
    public boolean attackerResponse(Field f) {
        int attackerResponse = -1;
        boolean properAttackerResponse = false;
        while (!properAttackerResponse) {
            try { // Is this a valid move? Try to use it.
                attackerResponse = playerInput(attacker);
                if (attackerResponse != 0) {
                    // This is a card.
                    Card attackerResponseCard = attacker.getCard(attackerResponse);
                    f.attack(attackerResponseCard);
                    properAttackerResponse = true;
                    attacker.useCard(attackerResponse);
                    announceCardPlayed(attacker, attackerResponseCard);
                    attacker.addMessage(Message.formInput(1));
                    f.getPlayedRanks().add(attackerResponseCard.getRank());
                    return false;
                } else {
                    // This is not a card.
                    // This is a request to end the round. No one takes any cards. The field closes.
                    System.out.println("\n\n" + attacker + " has chosen to end the round!");
                    properAttackerResponse = true;
                    f.endField();
                    attacker.addMessage(Message.formInput(1));
                    return true;
                }
            } catch (IllegalArgumentException | InterruptedException e) { // Invalid attack card, another one will be solicited
                System.out.println("Invalid attack card!");
                attacker.addMessage(Message.formInput(2));
                properAttackerResponse = false;
            }
        }
        return true; // Satisfy Java
    }

    // Determines current attacker once game is initiated
    // If true: one is attacker, two is defender
    // If false: one is defender, two is attacker
    public boolean whichAttacker() {
        return one.isAttacker();
    }

    public void setAttacker(Player p) {
        attacker = p;
        p.makeAttacker();
    }

    public void setDefender(Player p) {
        defender = p;
        p.makeDefender();
    }

    public void switchRoles() {
        Player temp = attacker;
        attacker = defender;
        defender = temp;
        one.switchRole();
        two.switchRole();
    }

    public Player getAttacker() {
        return attacker;
    }

    public Player getDefender() {
        return defender;
    }

    public Player getPlayerOne() {return one;}

    public Player getPlayerTwo() {return two;}

    public boolean roundInitiated() {
        return roundInitiated;
    }

    public Player getPlayerByID(String id){
        if(one.getID().toString().compareTo(id) == 0)
            return one;
        else if(two.getID().toString().compareTo(id) == 0)
            return two;
        return null;
    }

    public ObjectId getID() { return id; }
    private void storeDurakToDB(){
        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( stream );
            oos.writeObject( this );
            oos.close();
            String objectBinary = Base64.getEncoder().encodeToString(stream.toByteArray());
            if(DB.collectionExists(id.toString())){
                MongoCollection collection  = DB.Instance().getCollection(id.toString());
                collection.updateOne(Filters.eq("id", id.toString()), Updates.set("binary", objectBinary));
            }else{
                DB.Instance().createCollection(id.toString());
                MongoCollection collection  = DB.Instance().getCollection(id.toString());
                collection.insertOne(new Document("id", id.toString()).append("binary", objectBinary));
            }
            durakBinaries.add(objectBinary);
        } catch (IOException ex){
            System.out.println(ex.toString());
        }

    }

    public boolean restartRound() throws InterruptedException {
        // Create references to attacker and defender


        // Generate header
        String roundName = "ROUND " + round;
        String headerLine = "==================== " + roundName + " ====================" + "\n";
        String headerContent = "Attacker: " + attacker + " | " + "Defender: " + defender + "\n";
        String header = "\n\n\n" + headerLine + headerContent + headerLine + "\n\n\n";
        Boolean firstCheck = true;
        roundInitiated = false; // Round is in initial stages
        Field roundField;
        roundField = currentField; // Generate a Field

        roundInitiated = true;

        while (!roundField.isCompleted()) {
            if(firstCheck && whosTurn == defender){
                firstCheck = false;
                whosTurn = defender;
                boolean defenderTurn = defenderResponse(roundField);
                one.addMessage(Message.formField(currentField));
                two.addMessage(Message.formField(currentField));
                //storeDurakToDB();
                if (defenderTurn || victoryAchieved()) {
                    // Defender took the cards, ended the round
                    // OR as a result of the defender's turn, victory was achieved (CARD PLAYED: Check for victory!)
                    roundInitiated = false;
                    currentField = null;
                    //switchRoles();
                    return true; // Pop out of round
                }
                defender.addMessage(Message.formPlayersHand(defender.getHand()));
                attacker.addMessage(Message.formEnemyCardCount(defender.getHand()));
                // Defender responded by playing a card
            }else if(firstCheck && whosTurn == attacker){
                firstCheck = false;
                whosTurn = attacker;
                boolean attackerTurn = attackerResponse(roundField);
                one.addMessage(Message.formField(currentField));
                two.addMessage(Message.formField(currentField));
                //storeDurakToDB();
                if (attackerTurn || victoryAchieved()) {
                    // Attacker declared the round to be over
                    // OR as a result of the attacker's turn, victory was achieved (CARD PLAYED: Check for victory!)
                    roundInitiated = false;
                    currentField = null;
                    return false; // Pop out of round
                }
                attacker.addMessage(Message.formPlayersHand(attacker.getHand()));
                defender.addMessage(Message.formEnemyCardCount(attacker.getHand()));
            }
        }
        return true; // Satisfy Java
    }

    public void startDurakAgain() throws InterruptedException {
        boolean gameOver = false;
        boolean restart = true;
        while (!gameOver) {

            boolean thisRound;
            if(restart == true && currentField != null)
                thisRound = restartRound(); // Run a round
            else
                thisRound = round();
            restart = false;
            one.addMessage(Message.formRoundEnd());
            two.addMessage(Message.formRoundEnd());
            if (victoryAchieved()) {
                // Victory was achieved by the playing of cards at some point
                // Arrived here after breaking out of the round instantly
                gameOver = true; // Break out of the game loop
            } else {
                // No victory was achieved
                // Draw cards, last attacker first
                attacker.replenish();
                defender.replenish();
                round++; // Increment round number
                if (thisRound) {
                    // Attacker won the round
                    // Attacker goes again; no switching occurs
                } else {
                    // Defender won the round
                    // Roles switch
                    switchRoles();
                }
            }
        }

        System.out.println("Game over!\n");
        System.out.println("The winner is " + determineWinner() + "!\n");
        if(determineWinner() == one) {
            one.addMessage(Message.formGameEnd(true));
            two.addMessage(Message.formGameEnd(false));
        }else{
            one.addMessage(Message.formGameEnd(false));
            two.addMessage(Message.formGameEnd(true));
        }
    }

    public void sendStatusToClient(){
        one.addMessage(Message.formDeckCount(deck));
        two.addMessage(Message.formDeckCount(deck));

        one.addMessage(Message.formRole(one.isAttacker()));
        two.addMessage(Message.formRole(two.isAttacker()));

        one.addMessage(Message.formPlayersHand(one.getHand()));
        two.addMessage(Message.formPlayersHand(two.getHand()));

        one.addMessage(Message.formEnemyCardCount(two.getHand()));
        two.addMessage(Message.formEnemyCardCount(one.getHand()));

        if(currentField != null){
            one.addMessage(Message.formField(currentField));
            two.addMessage(Message.formField(currentField));
        } else{
            Field empty = new Field();
            one.addMessage(Message.formField(empty));
            two.addMessage(Message.formField(empty));
        }

        one.addMessage(Message.formPlayerTurnEvent(one.isAttacker()));
        two.addMessage(Message.formPlayerTurnEvent(two.isAttacker()));
    }
}