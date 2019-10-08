package durak.Threads;

import durak.Game;
import durak.GameConnectionToAPI;
import durak.GameDataClasses.GameData;
import java.util.Observable;

public class inputThread extends Observable implements Runnable {
    private final GameConnectionToAPI connection;
    private GameData gameData;
    private int input;
    
    public inputThread(GameConnectionToAPI connection_, GameData gameData_, int input_) {
      connection = connection_;
      gameData = gameData_;
      input = input_;
   }
    
    @Override
    public void run() 
    { 
        try
        { 
            connection.input(gameData.getPlayer().getIDs().getValue(), gameData.getPlayer().getIDs().getKey(), input);
        } 
        catch (Exception e) 
        { 
            // Throwing an exception 
            System.out.println ("inputThread Exception is caught: "+e);
        }
    } 
    
}