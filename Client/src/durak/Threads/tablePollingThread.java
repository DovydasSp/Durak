package durak.Threads;

import durak.GameDataClasses.Field;
import durak.GameConnectionToAPI;
import java.util.Observable;

public class tablePollingThread extends Observable implements Runnable {
    GameConnectionToAPI connection;
    String playerID;
    String gameID;
    
    public tablePollingThread(GameConnectionToAPI connection_, String playerID_, String gameID_) {
      connection = connection_;
      playerID = playerID_;
      gameID = gameID_;
   }
    
    @Override
    public void run() 
    { 
        
        try
        { 
            while(true){
                Field field = new Field();
                while((field = connection.getField(playerID, gameID)) == null){
                    Thread.sleep(500);
                    //System.out.println("trying");
                }
                //System.out.println("done");
                int input = connection.getInput(playerID, gameID);
                setChanged();
                notifyObservers(field);
            }
        } 
        catch (Exception e) 
        { 
            // Throwing an exception 
            System.out.println ("Exception is caught: "+e); 
        }
    } 
    
}
