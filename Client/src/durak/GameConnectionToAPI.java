package durak;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.util.Pair;
import org.json.JSONObject;


public class GameConnectionToAPI {
    HttpURLConnection conn;
    
    public Pair<String, String> createGame(String playerName) throws Exception{
        try {
            URL url = new URL("https://durakserver.azurewebsites.net/createGame");
            conn = (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String gameID = "", playerID = "";
        
        conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
	conn.setDoOutput(true);
        String jsonInputString = "{playerName:"+playerName+"}"; //galima pridėt daugiau body elementų
        
        try(OutputStream os = conn.getOutputStream()) { //sudeda body parametrus
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);           
        }
            
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
            }
                
            JSONObject myResponse = new JSONObject(response.toString());
            JSONObject gameid_data = myResponse.getJSONObject("gameID");
            gameID = gameid_data.getString("string");
            JSONObject playerid_data = myResponse.getJSONObject("playerID");
            playerID = playerid_data.getString("string");
        }
        
        Pair<String, String> pair = new Pair<String, String>(gameID, playerID);
        return pair;
    }
    
    public Pair<String, String> joinGame(String playerName, String gameID) throws Exception{
        try {
            URL url = new URL("https://durakserver.azurewebsites.net/joinGame");
            conn = (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String playerID = "";
        
        conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
	conn.setDoOutput(true);
        String jsonInputString = "{gameID: "+gameID+", playerName: "+playerName+"}"; //galima pridėt daugiau body elementų
        
        try(OutputStream os = conn.getOutputStream()) { //sudeda body parametrus
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);           
        }
            
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
            }
                
            JSONObject myResponse = new JSONObject(response.toString());
            JSONObject playerid_data = myResponse.getJSONObject("playerID");
            playerID = playerid_data.getString("string");
        }
        
        Pair<String, String> pair = new Pair<String, String>(gameID, playerID);
        return pair;
    }
}
