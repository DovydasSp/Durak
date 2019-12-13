package Server;

import Game.Durak;
import Game.DurakFactory;
import org.json.JSONObject;

import java.util.HashMap;

public interface Request {
    public boolean inputRequest(Durak durak, String playerID, int command);
    public boolean joinGameRequest(JSONObject obj);
    public String pollRequest(String gameID, String playerID);
}
