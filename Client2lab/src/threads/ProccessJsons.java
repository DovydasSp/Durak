
package threads;

import chain.AbstractLogger;
import chain.ChainLogger;
import gamedataclasses.Card;
import gamedataclasses.CardBuilder;
import gamedataclasses.CardPair;
import gamedataclasses.Chat;
import gamedataclasses.Field;
import gamedataclasses.GameData;
import gamedataclasses.Hand;
import org.json.JSONException;
import org.json.JSONObject;

public class ProccessJsons {

        private ChainLogger loggerChain = new ChainLogger();
    
    public GameData yourTurn(JSONObject myResponse, GameData gameData) throws JSONException {
        boolean yourTurn = myResponse.getBoolean("yourTurn");
        gameData.getPlayer().setYourTurn(yourTurn);
        gameData.setWhatsChanged("player");
        gameData.setWhatsChangedInPlayer("yourTurn");
        loggerChain.logMessage(AbstractLogger.INFO,"API sent your turn update");
        return gameData;
    }

    public GameData role(JSONObject myResponse, GameData gameData) throws JSONException {
        String role = myResponse.getString("role");
        if (role.equals("attacker")) {
            gameData.getPlayer().setIsAttacker(true);
        } else {
            gameData.getPlayer().setIsAttacker(false);
        }
        gameData.setWhatsChanged("player");
        gameData.setWhatsChangedInPlayer("isAttacker");
        loggerChain.logMessage(AbstractLogger.INFO,"API sent role update");
        return gameData;
    }

    public GameData trump(JSONObject myResponse, GameData gameData) throws JSONException {
        String trump = myResponse.getString("trump");
        gameData.getPlayer().setTrump(trump);
        gameData.setWhatsChanged("player");
        gameData.setWhatsChangedInPlayer("trump");
        loggerChain.logMessage(AbstractLogger.INFO, "API sent trump update");
       return gameData;
    }

    public GameData enemyPlayerCardCount(JSONObject myResponse, GameData gameData) throws JSONException {
        int enemyPlayerCardCount = myResponse.getInt("count");
        gameData.getPlayer().setOponentCardCount(enemyPlayerCardCount);
        gameData.setWhatsChanged("player");
        gameData.setWhatsChangedInPlayer("oponentCardCount");
        loggerChain.logMessage(AbstractLogger.INFO, "API sent enemy card count update");
        return gameData;
    }

    public GameData playersHand(JSONObject myResponse, GameData gameData) throws JSONException {
        CardBuilder cb = new CardBuilder();
        int numberOfCards = myResponse.getInt("numberOfCards");
        Hand hand = new Hand();

        for (int i = 0; i < numberOfCards; i++) {
            JSONObject card_data = myResponse.getJSONObject("card" + i);
            //loggerChain.logMessage(AbstractLogger.PATTERN,"BUILDER: building a new card.");
            hand.add(cb.setColor(card_data.getString("color")).setRank(card_data.getString("rank")).setSuit(card_data.getString("suit")).getCard());
        }
        gameData.getPlayer().setHand(hand);
        gameData.setWhatsChanged("player");
        gameData.setWhatsChangedInPlayer("hand");
        loggerChain.logMessage(AbstractLogger.INFO,"API sent hand update");
        return gameData;
    }

    public GameData field(JSONObject myResponse, GameData gameData) throws JSONException, CloneNotSupportedException {
        CardPair pair = new CardPair(new Card(), new Card(), false);
        CardPair pairCopy;
        CardBuilder cb = new CardBuilder();
        Field field = new Field();
        int numberOfPairs = myResponse.getInt("numberOfPairs");
        for (int i = 0; i < numberOfPairs; i++) {
            pairCopy = pair.DeepCopy(pair);
            JSONObject pair_data = myResponse.getJSONObject("pair" + i);
            boolean completed = pair_data.getBoolean("completed");
            JSONObject att_data = pair_data.getJSONObject("atackerCard");
            //loggerChain.logMessage(AbstractLogger.PATTERN,"BUILDER: building a new card.");
            Card attCard = cb.setColor(att_data.getString("color")).setRank(att_data.getString("rank")).setSuit(att_data.getString("suits")).getCard();
            pairCopy.setAttacker(attCard);
            if (completed) {
                JSONObject def_data = pair_data.getJSONObject("defenderCard");
                //loggerChain.logMessage(AbstractLogger.PATTERN,"BUILDER: building a new card.");
                Card defCard = cb.setColor(def_data.getString("color")).setRank(def_data.getString("rank")).setSuit(def_data.getString("suits")).getCard();
                pairCopy.setDefender(defCard);
                pairCopy.setCompleted(completed);
            }
            field.addPair(pairCopy);
            if (i == 5 && completed == true) {
                field.setCompleted();
            }
        }
        field.setPairCount(numberOfPairs);
        gameData.setField(field);
        gameData.setWhatsChanged("field");
        gameData.setWhatsChangedInPlayer("");
        loggerChain.logMessage(AbstractLogger.INFO,"API sent field update");
        return gameData;
    }

    public GameData roundEnd(JSONObject myResponse, GameData gameData) throws JSONException {
        gameData.setWhatsChanged("roundEnd");
        gameData.setField(new Field());
        loggerChain.logMessage(AbstractLogger.INFO,"API sent roundEnd call");
        return gameData;
    }

    public GameData deckCount(JSONObject myResponse, GameData gameData) throws JSONException {
        int deckCount = myResponse.getInt("count");
        gameData.getPlayer().setDeckCardCount(deckCount);
        gameData.setWhatsChanged("player");
        gameData.setWhatsChangedInPlayer("deckCount");
        loggerChain.logMessage(AbstractLogger.INFO,"API sent deckCount update");
        return gameData;
    }

    public GameData gameEnd(JSONObject myResponse, GameData gameData) throws JSONException {
        boolean win = myResponse.getBoolean("win");
        gameData.getPlayer().setWon(win);
        gameData.getPlayer().setHand(new Hand());
        if (win == false) {
            gameData.getPlayer().setOponentCardCount(0);
        }
        gameData.setField(new Field());
        gameData.setWhatsChanged("gameEnd");
        loggerChain.logMessage(AbstractLogger.INFO,"API sent gameEnd call");
        return gameData;
    }
    
    public GameData chat(JSONObject myResponse, GameData gameData) throws JSONException {
        String playerName = myResponse.getString("playerName");
        String message = myResponse.getString("message");
        Chat c = new Chat();
        c.setEnemyName(playerName);
        c.setMessage(message);
        gameData.setWhatsChanged("chat");
        gameData.setChat(c);
        loggerChain.logMessage(AbstractLogger.INFO,"API sent chat call");
        return gameData;
    }
}
