package durak;

import durak.GameDataClasses.GameData;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;

public class GameObserver implements Observer{
    private Game game;
    private GameData gameData = new GameData();

    GameObserver(Game game_, GameData gameData_) {
        game = game_;
        gameData = gameData_;
    }
    
    
    @Override
    public void update(Observable o, Object arg) {
        gameData = (GameData)arg;
        try {
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
