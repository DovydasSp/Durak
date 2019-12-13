package threads;

import gamedataclasses.GameData;
import org.json.JSONException;
import org.json.JSONObject;

interface ProccessJsonsAdapter {
    public GameData yourTurn(JSONObject myResponse, GameData gameData) throws JSONException;
    public GameData role(JSONObject myResponse, GameData gameData) throws JSONException;
    public GameData trump(JSONObject myResponse, GameData gameData) throws JSONException;
    public GameData enemyPlayerCardCount(JSONObject myResponse, GameData gameData) throws JSONException;
    public GameData playersHand(JSONObject myResponse, GameData gameData) throws JSONException;
    public GameData field(JSONObject myResponse, GameData gameData) throws JSONException, CloneNotSupportedException;
    public GameData roundEnd(JSONObject myResponse, GameData gameData) throws JSONException;
    public GameData deckCount(JSONObject myResponse, GameData gameData) throws JSONException;
    public GameData gameEnd(JSONObject myResponse, GameData gameData) throws JSONException;
    public GameData chat(JSONObject myResponse, GameData gameData) throws JSONException;
}
