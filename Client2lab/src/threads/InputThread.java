package threads;

import durak.GameConnectionToAPI;
import gamedataclasses.GameData;
import statics.Constants;

public class InputThread implements Runnable {

    private final GameConnectionToAPI connection;
    private GameData gameData;
    private int input;
    private String message;

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
                System.out.println("inputThread: sent CHAT message");
                connection.chat(gameData.getPlayer().getIDs().getKey(), gameData.getPlayer().getIDs().getValue(), message);
            }
            else if (input == Constants.COMMAND_UNDO) {
                System.out.println("inputThread: sent UNDO request");
                connection.undo(gameData.getPlayer().getIDs().getKey());
            } else {
                System.out.println("inputThread: sent input request");
                connection.input(gameData.getPlayer().getIDs().getValue(), gameData.getPlayer().getIDs().getKey(), input);
            }
        } catch (Exception e) {
            // Throwing an exception 
            System.out.println("inputThread Exception is caught: " + e);
            run();
        }
    }

}
