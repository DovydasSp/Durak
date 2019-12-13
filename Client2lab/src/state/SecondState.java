package state;

import chain.AbstractLogger;
import chain.ChainLogger;
import durak.Game;
import java.util.logging.Level;
import java.util.logging.Logger;
import statics.Constants;

public class SecondState implements State {
    private ChainLogger loggerChain = new ChainLogger();
    
    @Override
    public void doAction(Context context, Game game, String message) {
        loggerChain.logMessage(AbstractLogger.PATTERN,"STATE: second state");
        try {
            game.sendInput(Constants.COMMAND_CHAT, message);
            game.addChatMsg("Wait until opponent responds!");
        } catch (Exception ex) {
            Logger.getLogger(FirstState.class.getName()).log(Level.SEVERE, null, ex);
        }
        State newState = new ThirdState();
        context.setState(newState);
    }
    
    @Override
    public int stateNr(){
        return 2;
    }
}