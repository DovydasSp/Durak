package durak;

import durak.GameDataClasses.Card;
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
    
    
    public GameData poll(GameData gameData) throws IOException, JSONException{
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
            
        try(BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
            }
                
            JSONObject myResponse = new JSONObject(response.toString());
            String header = "";
            header = myResponse.getString("header");
            if(header.equals("NoMessages"))
            {
                conn.disconnect();
                return null;
            }
            if(header.equals("yourTurn"))
            {
                conn.disconnect();
                boolean yourTurn = myResponse.getBoolean("yourTurn");
                gameData.getPlayer().setYourTurn(yourTurn);
                gameData.setWhatsChanged("player");
                gameData.setWhatsChangedInPlayer("yourTurn");
                System.out.println("API sent your turn update");
                return gameData;
            }
            if(header.equals("role"))
            {
                conn.disconnect();
                String role = myResponse.getString("role");
                if(role.equals("attacker")){
                    gameData.getPlayer().setIsAttacker(true);
                }
                else{
                    gameData.getPlayer().setIsAttacker(false);
                }
                gameData.setWhatsChanged("player");
                gameData.setWhatsChangedInPlayer("isAttacker");
                System.out.println("API sent role update");
                return gameData;
            }
            if(header.equals("trump"))
            {
                conn.disconnect();
                String trump = myResponse.getString("trump");
                gameData.getPlayer().setTrump(trump);
                gameData.setWhatsChanged("player");
                gameData.setWhatsChangedInPlayer("trump");
                System.out.println("API sent trump update");
                return gameData;
            }
            if(header.equals("enemyPlayerCardCount"))
            {
                conn.disconnect();
                int enemyPlayerCardCount = myResponse.getInt("count");
                gameData.getPlayer().setOponentCardCount(enemyPlayerCardCount);
                gameData.setWhatsChanged("player");
                gameData.setWhatsChangedInPlayer("oponentCardCount");
                System.out.println("API sent enemy card count update");
                return gameData;
            }
            if(header.equals("input"))
            {
                conn.disconnect();
                int input = myResponse.getInt("info");
                return null;
            }
            if(header.equals("playersHand"))
            {
                conn.disconnect();
                int numberOfCards = myResponse.getInt("numberOfCards");
                Hand hand = new Hand();
                for(int i=0; i < numberOfCards; i++){
                    JSONObject card_data = myResponse.getJSONObject("card"+i);
                    String color = card_data.getString("color");
                    String rank = card_data.getString("rank");
                    String suit = card_data.getString("suit");
                    Card card = new Card(rank, suit, color);
                    hand.add(card);
                }
                gameData.getPlayer().setHand(hand);
                gameData.setWhatsChanged("player");
                gameData.setWhatsChangedInPlayer("hand");
                System.out.println("API sent hand update");
                return gameData;
            }
            if(header.equals("field"))
            {
                conn.disconnect();
                Field field = new Field();
                int numberOfPairs = myResponse.getInt("numberOfPairs");
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
                field.setPairCount(numberOfPairs);
                gameData.setField(field);
                gameData.setWhatsChanged("field");
                gameData.setWhatsChangedInPlayer("");
                System.out.println("API sent field update");
                return gameData;
            }
            if(header.equals("roundEnd"))
            {
                conn.disconnect();
                System.out.println("GOT ROUND END CALL");
                gameData.setWhatsChanged("roundEnd");
                gameData.setField(new Field());
                System.out.println("API sent roundEnd call");
                return gameData;
            }
            if(header.equals("deckCount"))
            {
                conn.disconnect();
                int deckCount = myResponse.getInt("count");
                gameData.getPlayer().setDeckCardCount(deckCount);
                gameData.setWhatsChanged("player");
                gameData.setWhatsChangedInPlayer("deckCount");
                System.out.println("API sent deckCount update");
                return gameData;
            }
            if(header.equals("gameEnd")){
                conn.disconnect();
                boolean win = myResponse.getBoolean("win");
                gameData.getPlayer().setWon(win);
                gameData.getPlayer().setHand(new Hand());
                if(win == false){
                    gameData.getPlayer().setOponentCardCount(0);
                }
                gameData.setField(new Field());
                gameData.setWhatsChanged("gameEnd");
                System.out.println("API sent gameEnd call");
                return gameData;
            }
        }
        return null;
    }
}
