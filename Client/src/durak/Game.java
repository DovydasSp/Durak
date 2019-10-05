package durak;

import durak.GameDataClasses.Field;
import durak.GameDataClasses.GameData;
import durak.GameDataClasses.Player;
import durak.Threads.pollingThread;
import java.io.IOException;
import java.util.Observer;
import org.json.JSONException;

public class Game {
    private final GameConnectionToAPI connection = new GameConnectionToAPI();
    private final GameUI gameUI = new GameUI(this);
    private GameData gameData = new GameData();
    
    public void start(){
        gameUI.createFrame();
        gameUI.createBackPanel();
        gameUI.createInfoPanel();
        gameUI.createTablePanel();
        gameUI.createHandCardPanel();
        gameUI.drawGameBoard();
        
        GameJoin join = new GameJoin(connection, gameUI, this);
        join.join();
    }
    
    public void play(GameData gd){
        gameData = gd;
        gameUI.getFrame().setTitle("Durak - "+gameData.getPlayer().getPlayerName());
        //System.out.println("Started playing "+gameData.getPlayer().getPlayerName());
        
        pollingThread thread = new pollingThread(connection, gameData, this);
        GameObserver fo = new GameObserver(this, gameData);
        thread.addObserver(fo);
        Thread t = new Thread(thread);
        t.start();
    }
    
    public void refreshUI(GameData gd) throws IOException, JSONException, InterruptedException{
        gameData = gd;
        
        if(gameData.getWhatsChanged().equals("player")){
            gameUI.refreshPlayer(gameData);
        }
        else{
            gameUI.refreshField(gameData);
        }
    }
    
    public void sendInput(int cardNr) throws Exception{
        connection.input(gameData.getPlayer().getIDs().getValue(), gameData.getPlayer().getIDs().getKey(), cardNr);
    }
}
