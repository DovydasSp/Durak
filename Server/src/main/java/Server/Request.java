package Server;

import Game.Durak;
import org.json.JSONObject;

import java.util.HashMap;

public interface Request {
    public boolean inputRequest(Durak durak, String playerID, int command);
    public boolean joinGameRequest(HashMap<String, Durak> gameMap, JSONObject obj);
    public String pollRequest(HashMap<String, Durak>  gameMap, String gameID, String playerID);
}
