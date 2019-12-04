package observer;

import durak.Game;
import gamedataclasses.GameData;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;

public class GameObserver implements Observer {

    private Game game;
    private GameData gameData = new GameData();

    public GameObserver(Game game_, GameData gameData_) {
        game = game_;
        gameData = gameData_;
        System.out.println("Observer was created.");
    }

    @Override
    public void update(Object arg) {
        gameData = (GameData) arg;
        try {
            System.out.println("Observer refreshed UI");
            game.refreshUI(gameData);
        } catch (IOException ex) {
            Logger.getLogger(GameObserver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GameObserver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameObserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
