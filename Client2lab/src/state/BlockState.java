package state;

import chain.AbstractLogger;
import chain.ChainLogger;
import durak.Game;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockState implements State {
    private ChainLogger loggerChain = new ChainLogger();
    
    @Override
    public void doAction(Context context, Game game, String message) {
        loggerChain.logMessage(AbstractLogger.PATTERN,"STATE: block state");
        try {
            game.addChatMsg("BLOCKED! Wait until opponent responds!");
        } catch (Exception ex) {
            Logger.getLogger(FirstState.class.getName()).log(Level.SEVERE, null, ex);
        }
        context.setState(this);
    }
    
    @Override
    public int stateNr(){
        return 4;
    }
}