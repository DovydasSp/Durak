package Server;

import Game.Durak;
import org.json.JSONObject;

import java.util.HashMap;

public class RequestAdapter implements Request {
    public boolean inputRequest(Durak durak,  String playerID, int command){
        if(command >= 0)
            if(durak.getPlayerByID(playerID) != null){
                durak.getPlayerByID(playerID).addInput(command);
                System.out.println("RequestAdapter inputRequest success");
                return true;
            }
        System.out.println("RequestAdapter inputRequest failed");
        return false;
    }

    public boolean joinGameRequest(HashMap<String, Durak> gameMap, JSONObject obj){
        if(obj.getString("gameID") != null && obj.getString("playerName") != null){
            gameMap.get(obj.getString("gameID")).getPlayerTwo().setName(obj.getString("playerName"));
            System.out.println("RequestAdapter joinGameRequest success");
            return true;
        }
        System.out.println("RequestAdapter joinGameRequest failed");
        return false;
    }

    public String pollRequest(HashMap<String, Durak>  gameMap, String gameID, String playerID){
        if(gameMap.get(gameID) != null){
            Durak durak = gameMap.get(gameID);
            if(durak.getPlayerByID(playerID) != null){
                System.out.println("RequestAdapter pollRequest success");
                return durak.getPlayerByID(playerID).popMessage();
            }
        }
        System.out.println("RequestAdapter pollRequest failed");
        return "";
    }
}
