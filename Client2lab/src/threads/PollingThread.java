package threads;

import durak.Game;
import durak.GameConnectionToAPI;
import gamedataclasses.GameData;
import observer.Observable;
import org.json.JSONException;
import org.json.JSONObject;

public class PollingThread extends Observable implements Runnable {

    private final GameConnectionToAPI connection;
    private GameData gameData;
    private Game game;
    private ProccessJsonsAdapter processJsonsAdapter = new ProccessJsonsAdapterImplementation();

    public PollingThread(GameConnectionToAPI connection_, GameData gameData_, Game game_) {
        connection = connection_;
        gameData = gameData_;
        game = game_;
    }

    @Override
    public void run() {
        try {
            boolean run = true;
            while (run) {
                Thread.sleep(100);
                JSONObject json = connection.poll(gameData);
                if (json != null) {
                    gameData = decidePath(json, gameData);
                    if (gameData.getWhatsChanged().equals("gameEnd")) {
                        run = false;
                    }
                    if (!gameData.getWhatsChanged().equals("")) {
                        notifyObservers(gameData);
                    }
                }
            }
        } catch (Exception e) {
            // Throwing an exception 
            System.out.println("pollingThread Exception is caught: " + e);
            game.play(gameData);

        }
    }

    private GameData decidePath(JSONObject json, GameData gameData) throws JSONException, CloneNotSupportedException {
        String header = "";
        header = json.getString("header");
        switch (header) {
            case "input":
                gameData.setWhatsChanged("");
                gameData.setWhatsChangedInPlayer("");
                break;
            case "NoMessages":
                gameData.setWhatsChanged("");
                gameData.setWhatsChangedInPlayer("");
                break;
            case "yourTurn":
                gameData = processJsonsAdapter.yourTurn(json, gameData);
                break;
            case "role":
                gameData = processJsonsAdapter.role(json, gameData);
                break;
            case "trump":
                gameData = processJsonsAdapter.trump(json, gameData);
                break;
            case "enemyPlayerCardCount":
                gameData = processJsonsAdapter.enemyPlayerCardCount(json, gameData);
                break;
            case "playersHand":
                gameData = processJsonsAdapter.playersHand(json, gameData);
                break;
            case "field":
                gameData = processJsonsAdapter.field(json, gameData);
                break;
            case "roundEnd":
                gameData = processJsonsAdapter.roundEnd(json, gameData);
                break;
            case "deckCount":
                gameData = processJsonsAdapter.deckCount(json, gameData);
                break;
            case "gameEnd":
                gameData = processJsonsAdapter.gameEnd(json, gameData);
                break;
        }
        return gameData;
    }

}
