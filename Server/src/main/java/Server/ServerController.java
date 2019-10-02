package Server;

//import Game.Lobby;
import Game.Durak;
import org.json.HTTP;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class ServerController {

    //private Lobby lobby;
    private HashMap<String, Durak> gameMap = new HashMap<>();
    private HashMap<String, Thread> gameThreads = new HashMap<>();

    @RequestMapping(value= "/createGame", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(@RequestBody String json) {
        Durak durak = new Durak();
        JSONObject obj = new JSONObject(json);
        durak.getPlayerOne().setName(obj.getString("playerName"));
        gameMap.put(durak.getID().toString(), durak);
        return new ResponseEntity<>(Message.formGameCreated(durak.getID().toString(), durak.getPlayerOne()), HttpStatus.OK);
    }

    @RequestMapping(value = "/joinGame", method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame(@RequestBody String json) {
        JSONObject obj = new JSONObject(json);
        gameMap.get(obj.getString("gameID")).getPlayerTwo().setName(obj.getString("playerName"));
        gameThreads.put(obj.getString("gameID"), new Thread(gameMap.get(obj.getString("gameID"))));
        gameThreads.get(obj.getString("gameID")).start();
        return new ResponseEntity<>(Message.formPlayerJoined(gameMap.get(obj.getString("gameID")).getPlayerTwo()) ,HttpStatus.OK);
    }

    @RequestMapping(value = "/input", method = RequestMethod.POST)
    public ResponseEntity<Object> input(@RequestBody String json) {
        JSONObject obj = new JSONObject(json);
        Durak durak = gameMap.get(obj.getString("gameID"));
        durak.addInput(obj.getString("playerID"), obj.getInt("command"));
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @RequestMapping("/poll/{gameID}/{playerID}")
    public ResponseEntity<Object> poll(@PathVariable String gameID, @PathVariable String playerID){
        if(gameMap.get(gameID).getPlayerByID(playerID) != null){
            return new ResponseEntity<>(gameMap.get(gameID).getPlayerByID(playerID).popMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
    }

}
