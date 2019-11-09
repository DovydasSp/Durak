package durak.Threads;

import durak.GameDataClasses.GameData;
import durak.Static.Static;
import org.json.JSONException;
import org.json.JSONObject;

public class ProccessJsonsAdapterImplementation implements ProccessJsonsAdapter{

    private final ProccessJsons proccessJsons = new ProccessJsons();
    
    @Override
    public GameData yourTurn(JSONObject myResponse, GameData gameData) throws JSONException {
        if(myResponse.has("yourTurn") && !myResponse.isNull("yourTurn")){
            System.out.println("ADAPTER: Checked yourTurn validity");
            return proccessJsons.yourTurn(myResponse, gameData);
        }
        System.out.println("ADAPTER: Checked yourTurn validity. WRONG");
        return gameData;
    }

    @Override
    public GameData role(JSONObject myResponse, GameData gameData) throws JSONException {
        if(myResponse.has("role") && !myResponse.isNull("role")){
            System.out.println("ADAPTER: Checked role validity");
            return proccessJsons.role(myResponse, gameData);
        }
        System.out.println("ADAPTER: Checked role validity. WRONG");
        return gameData;
    }

    @Override
    public GameData trump(JSONObject myResponse, GameData gameData) throws JSONException {
        if(myResponse.has("trump") && !myResponse.isNull("trump")){
            if(myResponse.getString("trump").equalsIgnoreCase(Static.suits[0]) || myResponse.getString("trump").equalsIgnoreCase(Static.suits[1]) ||
                    myResponse.getString("trump").equalsIgnoreCase(Static.suits[2]) || myResponse.getString("trump").equalsIgnoreCase(Static.suits[3])){
                System.out.println("ADAPTER: Checked trump validity.");
                return proccessJsons.trump(myResponse, gameData);
            }
            
        }
        System.out.println("ADAPTER: Checked trump validity. WRONG");
        return gameData;
    }

    @Override
    public GameData enemyPlayerCardCount(JSONObject myResponse, GameData gameData) throws JSONException {
        if(myResponse.has("count") && !myResponse.isNull("count")){
            if(myResponse.getInt("count") > -1){
                System.out.println("ADAPTER: Checked enemyPlayerCardCount validity.");
                return proccessJsons.enemyPlayerCardCount(myResponse, gameData);
            }
        }
        System.out.println("ADAPTER: Checked enemyPlayerCardCount validity. WRONG");
        return gameData;
    }

    @Override
    public GameData playersHand(JSONObject myResponse, GameData gameData) throws JSONException {
        if(myResponse.has("numberOfCards") && !myResponse.isNull("numberOfCards")){
            if(myResponse.getInt("numberOfCards") > -1){
                for (int i = 0; i < myResponse.getInt("numberOfCards"); i++) {
                    JSONObject card_data = myResponse.getJSONObject("card" + i);
                    if(card_data.has("color") && !card_data.isNull("color") && card_data.has("suit") && !card_data.isNull("suit"))
                    {
                        if(!(card_data.getString("suit").equalsIgnoreCase(Static.suits[0]) && card_data.getString("color").equalsIgnoreCase(Static.colors.get(Static.suits[0])) || 
                            card_data.getString("suit").equalsIgnoreCase(Static.suits[1]) && card_data.getString("color").equalsIgnoreCase(Static.colors.get(Static.suits[1])) ||
                            card_data.getString("suit").equalsIgnoreCase(Static.suits[2]) && card_data.getString("color").equalsIgnoreCase(Static.colors.get(Static.suits[2])) || 
                            card_data.getString("suit").equalsIgnoreCase(Static.suits[3]) && card_data.getString("color").equalsIgnoreCase(Static.colors.get(Static.suits[3]))))
                        {
                            System.out.println("ADAPTER: Checked numberOfCards validity. WRONG");
                            return gameData;
                        }
                    }
                    else{
                        System.out.println("ADAPTER: Checked numberOfCards validity. WRONG");
                        return gameData;
                    }
                }
                System.out.println("ADAPTER: Checked numberOfCards validity");
                return proccessJsons.playersHand(myResponse, gameData);
            }
            else{
                System.out.println("ADAPTER: Checked numberOfCards validity. WRONG");
                return gameData;
            }
        }
        System.out.println("ADAPTER: Checked numberOfCards validity. WRONG");
        return gameData;
    }

    @Override
    public GameData field(JSONObject myResponse, GameData gameData) throws JSONException, CloneNotSupportedException {
        /*CardPair pair = new CardPair(new Card(), new Card(), false);
        CardPair pairCopy;
        CardBuilder cb = new CardBuilder();
        Field field = new Field();
        int numberOfPairs = myResponse.getInt("numberOfPairs");
        for (int i = 0; i < numberOfPairs; i++) {
            pairCopy = pair.DeepCopy(pair);
            JSONObject pair_data = myResponse.getJSONObject("pair" + i);
            boolean completed = pair_data.getBoolean("completed");
            JSONObject att_data = pair_data.getJSONObject("atackerCard");
            System.out.println ("BUILDER: building a new card.");
            Card attCard = cb.setColor(att_data.getString("color")).setRank(att_data.getString("rank")).setSuit(att_data.getString("suits")).getCard();
            pairCopy.setAttacker(attCard);
            if (completed) {
                JSONObject def_data = pair_data.getJSONObject("defenderCard");
                System.out.println ("BUILDER: building a new card.");
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
        System.out.println("API sent field update");
        return gameData;*/
        System.out.println("ADAPTER: Checked field validity");
        return proccessJsons.field(myResponse, gameData);
    }

    @Override
    public GameData roundEnd(JSONObject myResponse, GameData gameData) throws JSONException {
        System.out.println("ADAPTER: Checked roundEnd validity");
        return proccessJsons.roundEnd(myResponse, gameData);
    }

    @Override
    public GameData deckCount(JSONObject myResponse, GameData gameData) throws JSONException {
        if(myResponse.has("count") && !myResponse.isNull("count")){
            if(myResponse.getInt("count") > -1){
                System.out.println("ADAPTER: Checked deckCount validity.");
                return  proccessJsons.deckCount(myResponse, gameData);
            }
        }
        System.out.println("ADAPTER: Checked deckCount validity. WRONG");
        return gameData;
    }

    @Override
    public GameData gameEnd(JSONObject myResponse, GameData gameData) throws JSONException {
        if(myResponse.has("win") && !myResponse.isNull("win")){
            System.out.println("ADAPTER: Checked win validity.");
            return proccessJsons.gameEnd(myResponse, gameData);
        }
        System.out.println("ADAPTER: Checked win validity. WRONG");
        return gameData;
    }
}
