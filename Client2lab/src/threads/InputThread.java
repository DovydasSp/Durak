package threads;

import durak.GameConnectionToAPI;
import gamedataclasses.GameData;
import statics.Constants;

public class InputThread implements Runnable {

    private final GameConnectionToAPI connection;
    private GameData gameData;
    private int input;

    public InputThread(GameConnectionToAPI connection_, GameData gameData_, int input_) {
        connection = connection_;
        gameData = gameData_;
        input = input_;
    }

    @Override
    public void run() {
        try {
            if (input == Constants.COMMAND_UNDO) {
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