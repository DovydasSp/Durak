package durak.Threads;

import durak.Game;
import durak.GameConnectionToAPI;
import durak.GameDataClasses.GameData;
import durak.Observer.Observable;
import org.json.JSONException;
import org.json.JSONObject;

public class pollingThread extends Observable implements Runnable {
    private final GameConnectionToAPI connection;
    private GameData gameData;
    private Game game;
    private ProccessJsons processJsons = new ProccessJsons();
    
    public pollingThread(GameConnectionToAPI connection_, GameData gameData_, Game game_) {
      connection = connection_;
      gameData = gameData_;
      game = game_;
   }
    
    @Override
    public void run() 
    { 
        try
        { 
            boolean run = true;
            while(run){
                Thread.sleep(100);
                JSONObject json = connection.poll(gameData);
                if(json != null){
                    gameData = decidePath(json, gameData);
                    if(gameData.getWhatsChanged().equals("gameEnd"))
                    {
                        run = false;
                    }
                    if(!gameData.getWhatsChanged().equals("")){
                        notifyObservers(gameData);
                    }
                }
                
            }
        } 
        catch (Exception e) 
        { 
            // Throwing an exception 
            System.out.println ("pollingThread Exception is caught: "+e);
            game.play(gameData);
            
        }
    } 
    
    private GameData decidePath(JSONObject json, GameData gameData) throws JSONException{
        String header = "";
        header = json.getString("header");
        switch(header)
        {
            case "input":
                gameData.setWhatsChanged("");
                gameData.setWhatsChangedInPlayer("");
                break;
            case "NoMessages":
                gameData.setWhatsChanged("");
                gameData.setWhatsChangedInPlayer("");
                break;
            case "yourTurn":
                gameData = processJsons.yourTurn(json, gameData);
                break;
            case "role":
                gameData = processJsons.role(json, gameData);
                break;
            case "trump":
                gameData = processJsons.trump(json, gameData);
                break;
            case "enemyPlayerCardCount":
                gameData = processJsons.enemyPlayerCardCount(json, gameData);
                break;
            case "playersHand":
                gameData = processJsons.playersHand(json, gameData);
                break;
            case "field":
                gameData = processJsons.field(json, gameData);
                break;
            case "roundEnd":
                gameData = processJsons.roundEnd(json, gameData);
                break;
            case "deckCount":
                gameData = processJsons.deckCount(json, gameData);
                break;
            case "gameEnd":
                gameData = processJsons.gameEnd(json, gameData);
                break;
        }
        return gameData;
    }
    
}
