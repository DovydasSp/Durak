package threads;

import chain.AbstractLogger;
import chain.ChainLogger;
import durak.GameConnectionToAPI;
import gamedataclasses.GameData;
import statics.Constants;

public class InputThread implements Runnable {

    private final GameConnectionToAPI connection;
    private GameData gameData;
    private int input;
    private String message;
    private ChainLogger loggerChain = new ChainLogger();

    public InputThread(GameConnectionToAPI connection_, GameData gameData_, int input_, String message_) {
        connection = connection_;
        gameData = gameData_;
        input = input_;
        message = message_;
    }

    @Override
    public void run() {
        try {
            if(input == Constants.COMMAND_CHAT){
                loggerChain.logMessage(AbstractLogger.PATTERN, "COMMAND: inputThread: sent CHAT message");
                connection.chat(gameData.getPlayer().getIDs().getKey(), gameData.getPlayer().getIDs().getValue(), message);
            }
            else if (input == Constants.COMMAND_RESTART) {
                loggerChain.logMessage(AbstractLogger.PATTERN, "COMMAND: inputThread: sent RESTART request");
                connection.restart(gameData.getPlayer().getIDs().getKey());
            }
            else if (input == Constants.COMMAND_UNDO) {
                loggerChain.logMessage(AbstractLogger.PATTERN, "COMMAND: inputThread: sent UNDO request");
                connection.undo(gameData.getPlayer().getIDs().getKey());
            } else {
               // System.out.println("inputThread: sent input request");
                 loggerChain.logMessage(AbstractLogger.PATTERN, "COMMAND: inputThread: sent input request");
                connection.input(gameData.getPlayer().getIDs().getValue(), gameData.getPlayer().getIDs().getKey(), input);
            }
        } catch (Exception e) {
            // Throwing an exception 
           // System.out.println("inputThread Exception is caught: " + e);
            loggerChain.logMessage(AbstractLogger.ERROR, "COMMAND: inputThread Exception is caught: " + e);
            run();
        }
    }

}
