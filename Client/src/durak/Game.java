package durak;

import durak.GameDataClasses.Card;
import durak.GameDataClasses.CardPair;
import durak.GameDataClasses.Field;
import durak.GameDataClasses.GameData;
import durak.GameDataClasses.Player;
import durak.Static.Static;
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
        if(checkIfTurnValid(cardNr)){
            connection.input(gameData.getPlayer().getIDs().getValue(), gameData.getPlayer().getIDs().getKey(), cardNr);
        }
    }
    
    public boolean checkIfTurnValid(int cardNr){
        //check if turn is valid when player is defending
        if(!gameData.getPlayer().getIsAttacker())
        {
            Card defending = gameData.getPlayer().getHand().getCards().get(cardNr-1);
            Card attacking = gameData.getField().getPairs().get(gameData.getField().getPairCount()-1).getAttacker();
            String trump = gameData.getPlayer().getTrump();
        
            if(attacking.getSuit().equals(defending.getSuit()) && Static.values.get(attacking.getRank())<Static.values.get(defending.getRank()))
            {
                return true;
            }
            else if(defending.getSuit().equals(trump) && !attacking.getSuit().equals(trump))
            {
                return true;
            }
            else if(defending.getSuit().equals(trump) && attacking.getSuit().equals(trump) && Static.values.get(attacking.getRank())<Static.values.get(defending.getRank()))
            {
                return true;
            }
            return false;
        }
        // check if turn is valid when player is attacking and there is cards on table
        if(gameData.getPlayer().getIsAttacker() && gameData.getField().getPairCount() > 0)
        {
            Card attacking = gameData.getPlayer().getHand().getCards().get(cardNr-1);
            for(CardPair cp : gameData.getField().getPairs())
            {
                if(cp.getAttacker().getRank().equals(attacking.getRank()) || cp.getDefender().getRank().equals(attacking.getRank()))
                {
                    return true;
                }
                
            }
            return false;
        }
        return true;
    }
}
