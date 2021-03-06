package durak.Threads;

import durak.GameConnectionToAPI;
import durak.GameDataClasses.GameData;

public class inputThread implements Runnable {
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