package Server;

import Game.*;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Stack;

@RestController
public class ServerController {

    private HashMap<String, Durak> gameMap = new HashMap<>();
    private HashMap<String, Thread> gameThreads = new HashMap<>();
    private HashMap<String, Stack<Command>>  gameCommands = new HashMap<>();

    @RequestMapping(value= "/createGame", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> createGame(@RequestBody String json) {
        Durak durak = new Durak();
        JSONObject obj = new JSONObject(json);
        durak.getPlayerOne().setName(obj.getString("playerName"));
        gameMap.put(durak.getID().toString(), durak);
        return ResponseEntity.status(HttpStatus.OK).body(Message.formGameCreated(durak.getID().toString(), durak.getPlayerOne()).toString());
    }

    @RequestMapping(value = "/joinGame", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> joinGame(@RequestBody String json) {
        JSONObject obj = new JSONObject(json);
        gameMap.get(obj.getString("gameID")).getPlayerTwo().setName(obj.getString("playerName"));
        gameThreads.put(obj.getString("gameID"), new Thread(gameMap.get(obj.getString("gameID"))));
        gameThreads.get(obj.getString("gameID")).start();
        return ResponseEntity.status(HttpStatus.OK).body(Message.formPlayerJoined(gameMap.get(obj.getString("gameID")).getPlayerTwo()).toString());
    }

    @RequestMapping(value = "/input", method = RequestMethod.POST)
    public ResponseEntity<String> input(@RequestBody String json) {
        JSONObject obj = new JSONObject(json);
        String gameID = obj.getString("gameID");
        Durak durak = gameMap.get(gameID);
        Thread gameThread = gameThreads.get(gameID);
        if(gameCommands.get(gameID) == null)
            gameCommands.put(gameID, new Stack<>());
        Stack<Command> commands = gameCommands.get(gameID);
        CommandInvoker invoker = new CommandInvoker();
        int input = obj.getInt("command");
        Command command;
        if(input != 0)
            command = new PlayerInputCommand(durak, obj.getString("playerID"), input);
        else
            command = new RoundEndCommand(durak, obj.getString("playerID"));
        invoker.setCommand(command);
        invoker.sendCommand();
        commands.add(command);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/poll/{gameID}/{playerID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> poll(@PathVariable String gameID, @PathVariable String playerID){
        if(gameMap.get(gameID).getPlayerByID(playerID) != null){
            return ResponseEntity.status(HttpStatus.OK).body(gameMap.get(gameID).getPlayerByID(playerID).popMessage().toString());
        }
        return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/undo", method = RequestMethod.POST)
    public ResponseEntity<String> undo(@RequestBody String json) {
        JSONObject obj = new JSONObject(json);
        String gameID = obj.getString("gameID");
        if(gameCommands.get(gameID).size() != 0){
            CommandInvoker invoker = new CommandInvoker();
            invoker.setCommand(gameCommands.get(gameID).pop());
            Thread thread = invoker.undoCommand();
            gameMap.put(gameID, invoker.getDurak());
            gameThreads.put(gameID, thread);
        }
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

}
