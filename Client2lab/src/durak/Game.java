package durak;

import observer.GameObserver;
import gamedataclasses.Card;
import gamedataclasses.CardPair;
import gamedataclasses.GameData;
import gamedataclasses.Iterator;
import statics.Constants;
import statics.Static;
import threads.InputThread;
import threads.PollingThread;
import java.io.IOException;
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
        gameUI.createChatPanel();
        gameUI.drawGameBoard();
        
        join();
    }
    
    public void join(){
        GameJoin join = new GameJoin(connection, gameUI, this);
        join.join();
    }
    
    public void play(GameData gd){
        gameData = gd;
        gameUI.getFrame().setTitle("Durak - "+gameData.getPlayer().getPlayerName());
        gameUI.getChatPanelText().append("DURAK: GameID- "+gameData.getPlayer().getIDs().getKey()+"\n");
        
        System.out.println ("OBSERVER: observable polling thread created.");
        PollingThread thread = new PollingThread(connection, gameData, this);
        System.out.println ("OBSERVER: observer created.");
        GameObserver fo = new GameObserver(this, gameData);
        System.out.println ("OBSERVER: observer added to thread.");
        thread.addObserver(fo);
        Thread t = new Thread(thread);
        System.out.println ("OBSERVER: observable thread started.");
        t.start();
    }
    
    public void refreshUI(GameData gd) throws IOException, JSONException, InterruptedException{
        gameData = gd;

        if(gameData.getWhatsChanged().equals("player")){
            gameUI.refreshPlayer(gameData);
        }
        else if(gameData.getWhatsChanged().equals("gameEnd")){
            gameUI.refreshField(gameData);
            gameUI.refreshPlayer(gameData);
        }
        else if(gameData.getWhatsChanged().equals("roundEnd")){
            gameUI.refreshField(gameData);
            gameUI.refreshPlayer(gameData);
        }
        else if(gameData.getWhatsChanged().equals("field")){
            gameUI.refreshField(gameData);
        }
        else if(gameData.getWhatsChanged().equals("chat")){
            gameUI.getChatPanelText().append(gameData.getChat().getEnemyName()+": "+gameData.getChat().getMessage()+" \n");
            gameData.setWhatsChanged("");
        }
    }
    
    public void sendInput(int command, String message) throws Exception{
        GameConnectionToAPI connection2 = new GameConnectionToAPI();
        InputThread thread2 = new InputThread(connection2, gameData, command, message);
        Thread t2 = new Thread(thread2);
        if(command == Constants.COMMAND_CHAT || command == Constants.COMMAND_ROUND_END || command == Constants.COMMAND_UNDO){
            t2.start();
            t2.join();
        }
        else if(checkIfTurnValid(command)){
            t2.start();
            t2.join();
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
            //for(CardPair cp : gameData.getField().getPairs()){
            for(Iterator iter = gameData.getField().getIterator(); iter.hasNext();){
            CardPair cp = (CardPair) iter.next();
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
