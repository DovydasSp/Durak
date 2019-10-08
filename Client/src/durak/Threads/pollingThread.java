package durak.Threads;

import durak.Game;
import durak.GameConnectionToAPI;
import durak.GameDataClasses.GameData;
import java.util.Observable;

public class pollingThread extends Observable implements Runnable {
    private final GameConnectionToAPI connection;
    private GameData gameData;
    private Game game;
    
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
                GameData gd = connection.poll(gameData);
                if(gd != null){
                    gameData = gd;
                    if(gameData.getWhatsChanged().equals("gameEnd"))
                    {
                        run = false;
                    }
                    setChanged();
                    notifyObservers(gameData);
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
    
}
