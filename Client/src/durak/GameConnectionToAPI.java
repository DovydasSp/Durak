package durak;

import durak.GameDataClasses.Card;
import durak.GameDataClasses.CardPair;
import durak.GameDataClasses.Field;
import durak.GameDataClasses.Hand;
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
            gameID = myResponse.getString("gameID");
            playerID = myResponse.getString("playerID");
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
            playerID = myResponse.getString("playerID");
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
            isrole = myResponse.getString("header");
            if(isrole.equals("role"))
            {
                role = myResponse.getString("role");
            }
        }
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
            istrump = myResponse.getString("header");
            if(istrump.equals("trump"))
            {
                trump = myResponse.getString("trump");
            }
        }
        conn.disconnect();
        return trump;
    }
    
    public Hand getPlayersHand(String playerID, String gameID) throws ProtocolException, IOException, JSONException{
        try {
            URL url = new URL("https://durakserver.azurewebsites.net/poll/"+gameID+"/"+playerID);
            conn = (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String isPlayersHand = "";
        int numberOfCards = -1;
        Hand hand = null;
        
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
            isPlayersHand = myResponse.getString("header");
            if(isPlayersHand.equals("playersHand"))
            {
                numberOfCards = myResponse.getInt("numberOfCards");
                if(numberOfCards > 0){
                    hand = new Hand();
                }
                for(int i=0; i < numberOfCards; i++){
                    JSONObject card_data = myResponse.getJSONObject("card"+i);
                    String color = card_data.getString("color");
                    String rank = card_data.getString("rank");
                    String suit = card_data.getString("suit");
                    Card card = new Card(rank, suit, color);
                    hand.add(card);
                }
            }
        }
        conn.disconnect();
        return hand;
    }
    
    public String input(String playerID, String gameID, int cardNr) throws Exception{
        try {
            URL url = new URL("https://durakserver.azurewebsites.net/input");
            conn = (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String success = "";
        
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

            success = response.toString();
        }
        conn.disconnect();
        return success;
    }
    
    public Field getField(String playerID, String gameID) throws ProtocolException, IOException, JSONException{
        try {
            URL url = new URL("https://durakserver.azurewebsites.net/poll/"+gameID+"/"+playerID);
            conn = (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String isPlayersHand = "";
        int numberOfPairs = -1;
        Field field = new Field();;

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
            isPlayersHand = myResponse.getString("header");
            if(isPlayersHand.equals("field"))
            {
                numberOfPairs = myResponse.getInt("numberOfPairs");
                for(int i=0; i < numberOfPairs; i++){
                    JSONObject pair_data = myResponse.getJSONObject("pair"+i);
                    boolean completed = pair_data.getBoolean("completed");
                    JSONObject att_data = pair_data.getJSONObject("atackerCard");
                    String color = att_data.getString("color");
                    String rank = att_data.getString("rank");
                    String suit = att_data.getString("suits");
                    Card attCard = new Card(rank, suit, color);
                    CardPair pair = new CardPair(attCard, new Card(), completed);
                    if(completed){
                        JSONObject def_data = pair_data.getJSONObject("defenderCard");
                        color = def_data.getString("color");
                        rank = def_data.getString("rank");
                        suit = def_data.getString("suits");
                        Card defCard = new Card(rank, suit, color);
                        pair = new CardPair(attCard, defCard, completed);
                    }
                    field.addPair(pair);
                    if(i==5 && completed == true){
                        field.setCompleted();
                    }
                }
                return field;
            }
        }
        //conn.disconnect();
        return null;
    }
    
    public int getInput(String playerID, String gameID) throws ProtocolException, IOException, JSONException{
        try {
            URL url = new URL("https://durakserver.azurewebsites.net/poll/"+gameID+"/"+playerID);
            conn = (HttpURLConnection)url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String isinput = "";
        int input = -1;
        
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
            isinput = myResponse.getString("header");
            if(isinput.equals("input"))
            {
                input = myResponse.getInt("info");
            }
        }
        conn.disconnect();
        return input;
    }
}
