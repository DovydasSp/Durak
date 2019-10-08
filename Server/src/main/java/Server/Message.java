package Server;


import Game.Field;
import Game.Hand;
import Game.Player;
import com.mongodb.util.JSON;
import org.json.JSONObject;

public class Message {

    public static JSONObject formPlayersHand(Hand playersHand){
        JSONObject builder = new JSONObject();
        builder.put("header", "playersHand");
        builder.put("numberOfCards", playersHand.size());
        for (int a = 0; a < playersHand.size(); a++){
            builder.put("card" + a, new JSONObject()
                    .put("rank", playersHand.getCardByIndex(a).getRank())
                    .put("suit", playersHand.getCardByIndex(a).getSuit())
                    .put("color", playersHand.getCardByIndex(a).getColor()));
        }
        return builder;
    }

    public static JSONObject formField(Field field){
        JSONObject builder = new JSONObject();
        builder.put("header", "field");
        builder.put("numberOfPairs", field.getPairs().size());
        for (int a = 0; a < field.getPairs().size(); a++){
            if(field.getPairs().get(a).isCompleted()){
                builder.put("pair" + a, new JSONObject()
                        .put("completed", field.getPairs().get(a).isCompleted())
                        .put("atackerCard", new JSONObject()
                                .put("rank", field.getPairs().get(a).getAttacker().getRank())
                                .put("suits", field.getPairs().get(a).getAttacker().getSuit())
                                .put("color", field.getPairs().get(a).getAttacker().getColor()))
                        .put("defenderCard", new JSONObject()
                                .put("rank", field.getPairs().get(a).getDefender().getRank())
                                .put("suits", field.getPairs().get(a).getDefender().getSuit())
                                .put("color", field.getPairs().get(a).getDefender().getColor())));
            }
            else{
                builder.put("pair" + a, new JSONObject()
                        .put("completed", field.getPairs().get(a).isCompleted())
                        .put("atackerCard", new JSONObject()
                                .put("rank", field.getPairs().get(a).getAttacker().getRank())
                                .put("suits", field.getPairs().get(a).getAttacker().getSuit())
                                .put("color", field.getPairs().get(a).getAttacker().getColor())));
            }
        }
        return builder;
    }

    public static JSONObject formPlayerRole(Player player){
        JSONObject builder = new JSONObject();
        builder.put("header", "playerRole");
        builder.put("attacker", player.isAttacker());
        return builder;
    }

    public static JSONObject formInput(int input){
        JSONObject builder = new JSONObject();
        builder.put("header", "input");
        builder.put("info", input);
        return builder;
    }

    public static JSONObject formTrump(String trump){
        JSONObject builder = new JSONObject();
        builder.put("header", "trump");
        builder.put("trump", trump);
        return builder;
    }

    public static JSONObject formGameCreated(String gameID, Player player){
        JSONObject builder = new JSONObject();
        builder.put("gameID", gameID);
        builder.put("playerID", player.getID().toString());
        return builder;
    }

    public static JSONObject formPlayerJoined (Player player){
        JSONObject builder = new JSONObject();
        builder.put("playerID", player.getID().toString());
        return builder;
    }

    public static JSONObject formNoMessages(){
        JSONObject builder = new JSONObject();
        builder.put("header", "NoMessages");
        return builder;
    }

    public static JSONObject formRole(Boolean attacker){
        JSONObject builder = new JSONObject();
        builder.put("header", "role");
        if(attacker)
            builder.put("role", "attacker");
        else
            builder.put("role", "defender");
        return builder;
    }

    public static JSONObject formRoundEnd(){
        JSONObject builder = new JSONObject();
        builder.put("header", "roundEnd");
        return builder;
    }

    public static JSONObject formEnemyCardCount(Hand hand){
        JSONObject builder = new JSONObject();
        builder.put("header", "enemyPlayerCardCount");
        builder.put("count", hand.size());
        return builder;
    }

    public static JSONObject formPlayerTurnEvent(boolean turn){
        JSONObject builder = new JSONObject();
        builder.put("header", "yourTurn");
        builder.put("yourTurn", turn);
        return builder;
    }

    public static JSONObject formGameEnd(boolean win){
        JSONObject builder = new JSONObject();
        builder.put("header", "gameEnd");
        builder.put("win", win);
        return builder;
    }


}
