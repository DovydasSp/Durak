package durak;

import chain.*;
import observer.GameObserver;
import gamedataclasses.Card;
import gamedataclasses.CardPair;
import gamedataclasses.GameData;
import gamedataclasses.Iterator;
import interpreter.Expression;
import interpreter.OrExpression;
import interpreter.TerminalExpression;
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
    public ChainLogger loggerChain = new ChainLogger();
     
  
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
        
        //loggerChain.logMessage(AbstractLogger.PATTERN, "OBSERVER: observable polling thread created.");
        PollingThread thread = new PollingThread(connection, gameData, this);
        //loggerChain.logMessage(AbstractLogger.PATTERN, "OBSERVER: observer created.");
        GameObserver fo = new GameObserver(this, gameData);
        //loggerChain.logMessage(AbstractLogger.PATTERN, "OBSERVER: observer added to thread.");
        thread.addObserver(fo);
        Thread t = new Thread(thread);
        //loggerChain.logMessage(AbstractLogger.PATTERN, "OBSERVER: observable thread started.");
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
            gameUI.addChatMessage(gameData.getChat().getEnemyName(), gameData.getChat().getMessage());
            gameData.setWhatsChanged("");
        }
    }
    
    public void sendInput(int command, String message) throws Exception{
        GameConnectionToAPI connection2 = new GameConnectionToAPI();
        InputThread thread2 = new InputThread(connection2, gameData, command, message);
        Thread t2 = new Thread(thread2);
        if(command == Constants.COMMAND_CHAT || command == Constants.COMMAND_ROUND_END || command == Constants.COMMAND_UNDO || command == Constants.COMMAND_RESTART){
            t2.start();
            t2.join();
        }
        else if(checkIfTurnValid(command)){
            t2.start();
            t2.join();
        }
    }
    
    private static Expression getExpression(String e1, String e2){
        Expression expr1 = new TerminalExpression(e1);
        Expression expr2 = new TerminalExpression(e2);
        return new OrExpression(expr1, expr2);
    }
    
    public void chatCommand(String message) throws Exception{
        Expression isUndo = getExpression("/undo", "/back");
        Expression isRestart = getExpression("/restart", "/rr");
        if(isUndo.interpret(message)){
            sendInput(Constants.COMMAND_UNDO, "");
        }
        if(isRestart.interpret(message)){
            sendInput(Constants.COMMAND_RESTART, "");
        }
    }
    
    public void addChatMsg(String message){
        gameUI.addChatMessage("DURAK", message);
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
