package durak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import javafx.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import durak.GameClasses.*;
import java.util.ArrayList;


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
        conn.disconnect();
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
        conn.disconnect();
        return pair;
    }
    
    public String getRole(String playerID, String gameID) throws ProtocolException, IOException, JSONException{
        try {
            URL url = new URL("https://durakserver.azurewebsites.net/poll/"+gameID+"/"+playerID);
            conn = (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String isrole = "";
        String role = "";
        
        conn.setRequestMethod("GET");
	conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
	conn.setDoOutput(true);
            
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
            }
                
            JSONObject myResponse = new JSONObject(response.toString());
            JSONObject isrole_data = myResponse.getJSONObject("header");
            isrole = isrole_data.getString("string");
            if(isrole.equals("role"))
            {
                JSONObject role_data = myResponse.getJSONObject("role");
                role = role_data.getString("string");
            }
        }
        System.out.println("role:"+role);
        conn.disconnect();
        return role;
    }
    
    public String getTrump(String playerID, String gameID) throws ProtocolException, IOException, JSONException{
        try {
            URL url = new URL("https://durakserver.azurewebsites.net/poll/"+gameID+"/"+playerID);
            conn = (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String istrump = "";
        String trump = "";
        
        conn.setRequestMethod("GET");
	conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
	conn.setDoOutput(true);
            
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
            }
                
            JSONObject myResponse = new JSONObject(response.toString());
            JSONObject isrole_data = myResponse.getJSONObject("header");
            istrump = isrole_data.getString("string");
            if(istrump.equals("trump"))
            {
                JSONObject role_data = myResponse.getJSONObject("trump");
                trump = role_data.getString("string");
            }
        }
        System.out.println(trump);
        conn.disconnect();
        return trump;
    }
    /*
    public Hand getPlayersHand(String playerID, String gameID) throws ProtocolException, IOException, JSONException{
        try {
            URL url = new URL("https://durakserver.azurewebsites.net/poll/"+gameID+"/"+playerID);
            conn = (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String isPlayersHand = "";
        String numberOfCards = "";
        Hand hand = new Hand();
        
        conn.setRequestMethod("GET");
	conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
	conn.setDoOutput(true);
            
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
            }
                
            JSONObject myResponse = new JSONObject(response.toString());
            JSONObject isrole_data = myResponse.getJSONObject("header");
            isPlayersHand = isrole_data.getString("string");
            if(isPlayersHand.equals("playersHand"))
            {
                JSONObject role_data = myResponse.getJSONObject("numberOfCards");
                //trump = role_data.getString("integral");
                System.out.println("integral:"+role_data.getBoolean("integral"));
                System.out.println("valueType:"+role_data.getString("valueType"));
            }
        }
        //System.out.println(trump);
        conn.disconnect();
        return hand;
    }*/
}
