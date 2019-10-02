package Server;


import Game.Field;
import Game.Hand;
import Game.Player;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Message {

    public static JsonObject formPlayersHand(Hand playersHand){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("header", "playersHand");
        builder.add("numberOfCards", playersHand.size());
        for (int a = 0; a < playersHand.size(); a++){
            builder.add("card" + a, Json.createObjectBuilder()
                    .add("rank", playersHand.getCardByIndex(a).getRank())
                    .add("suit", playersHand.getCardByIndex(a).getSuit())
                    .add("color", playersHand.getCardByIndex(a).getColor()));
        }
        return builder.build();
    }

    public static JsonObject formField(Field field){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("header", "field");
        builder.add("numberOfPairs", field.getPairs().size());
        for (int a = 0; a < field.getPairs().size(); a++){
            if(field.getPairs().get(a).isCompleted()){
                builder.add("pair" + a, Json.createObjectBuilder()
                        .add("completed", field.getPairs().get(a).isCompleted())
                        .add("atackerCard", Json.createObjectBuilder()
                                .add("rank", field.getPairs().get(a).getAttacker().getRank())
                                .add("suits", field.getPairs().get(a).getAttacker().getSuit())
                                .add("color", field.getPairs().get(a).getAttacker().getColor()))
                        .add("defenderCard", Json.createObjectBuilder()
                                .add("rank", field.getPairs().get(a).getDefender().getRank())
                                .add("suits", field.getPairs().get(a).getDefender().getSuit())
                                .add("color", field.getPairs().get(a).getDefender().getColor())));
            }
            else{
                builder.add("pair" + a, Json.createObjectBuilder()
                        .add("completed", field.getPairs().get(a).isCompleted())
                        .add("atackerCard", Json.createObjectBuilder()
                                .add("rank", field.getPairs().get(a).getAttacker().getRank())
                                .add("suits", field.getPairs().get(a).getAttacker().getSuit())
                                .add("color", field.getPairs().get(a).getAttacker().getColor())));
            }
        }
        return builder.build();
    }

    public static JsonObject formPlayerRole(Player player){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("header", "playerRole");
        builder.add("attacker", player.isAttacker());
        return builder.build();
    }

    public static JsonObject formInput(int input){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("header", "input");
        builder.add("info", input);
        return builder.build();
    }

    public static JsonObject formTrump(String trump){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("header", "trump");
        builder.add("trump", trump);
        return builder.build();
    }

    public static JsonObject formGameCreated(String gameID, Player player){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("gameID", gameID);
        builder.add("playerID", player.getID().toString());
        return builder.build();
    }

    public static JsonObject formPlayerJoined (Player player){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("playerID", player.getID().toString());
        return builder.build();
    }

    public static JsonObject formNoMessages(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("header", "NoMessages");
        return builder.build();
    }

    public static JsonObject formRole(Boolean attacker){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("header", "role");
        if(attacker)
            builder.add("role", "attacker");
        else
            builder.add("role", "defender");
        return builder.build();
    }

    public static JsonObject formRoundEnd(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("header", "roundEnd");
        return builder.build();
    }


}
