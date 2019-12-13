package Server;

import Game.Durak;
import Game.DurakFactory;
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

    public boolean joinGameRequest(JSONObject obj){
        if(obj.getString("gameID") != null && obj.getString("playerName") != null){
            Durak game =  DurakFactory.getDurak(obj.getString("gameID"));
            game.getPlayerTwo().setName(obj.getString("playerName"));
            Thread thread = DurakFactory.getGameThread(obj.getString("gameID"));
            thread.start();
            DurakFactory.getCareTaker(obj.getString("gameID")).add(game.saveState());
            System.out.println("RequestAdapter joinGameRequest success");
            return true;
        }
        System.out.println("RequestAdapter joinGameRequest failed");
        return false;
    }

    public String pollRequest(String gameID, String playerID){
        Durak durak = DurakFactory.getDurak(gameID);
        if(durak.getPlayerByID(playerID) != null){
            //System.out.println("RequestAdapter pollRequest success");
            return durak.getPlayerByID(playerID).popMessage();
        }
        //System.out.println("RequestAdapter pollRequest failed");
        return "";
    }
}
