package durak;

import durak.GameDataClasses.Card;
import durak.GameDataClasses.CardBuilder;
import durak.GameDataClasses.CardPair;
import durak.GameDataClasses.Field;
import durak.GameDataClasses.GameData;
import durak.GameDataClasses.Hand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;


public class GameConnectionToAPI {
    HttpURLConnection conn;
    private String urlas = "https://durakserver.azurewebsites.net/";
    //private String urlas = "http://192.168.0.106:8080/";
    
    public Pair<String, String> createGame(String playerName) throws Exception{
        try {
            URL url = new URL(urlas+"createGame");
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
            gameID = myResponse.getString("gameID");
            playerID = myResponse.getString("playerID");
        }
        
        Pair<String, String> pair = new Pair<String, String>(gameID, playerID);
        conn.disconnect();
        return pair;
    }
    
    public Pair<String, String> joinGame(String playerName, String gameID) throws Exception{
        try {
            URL url = new URL(urlas+"joinGame");
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
            playerID = myResponse.getString("playerID");
        }
        
        Pair<String, String> pair = new Pair<String, String>(gameID, playerID);
        conn.disconnect();
        return pair;
    }
    
    public void input(String playerID, String gameID, int cardNr) throws Exception{
        try {
            URL url = new URL(urlas+"input");
            conn = (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            System.out.println("Input send to API failed");
            e.printStackTrace();
        }
        
        conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
	conn.setDoOutput(true);
        String jsonInputString = "{gameID: "+gameID+", playerID: "+playerID+", command: "+cardNr+"}"; //galima pridėt daugiau body elementų
        
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
        }
        conn.disconnect();
    }
    
    
    public JSONObject poll(GameData gameData) throws IOException, JSONException{
        try {
            URL url = new URL(urlas+"poll/"+gameData.getPlayer().getIDs().getKey()+"/"+gameData.getPlayer().getIDs().getValue());
            conn = (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        conn.setRequestMethod("GET");
	conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
	conn.setDoOutput(true);
          
        JSONObject myResponse = null;
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
            }
            
            myResponse = new JSONObject(response.toString());
        }
        conn.disconnect();
        return myResponse;
    }
}
