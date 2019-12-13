package Server;

import Game.*;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp;
import org.javatuples.Triplet;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.javatuples.Pair;
import java.util.HashMap;
import java.util.Stack;

@RestController
public class ServerController {

    private HashMap<String, Durak> gameMap = new HashMap<>();
    private HashMap<String, Thread> gameThreads = new HashMap<>();
    private HashMap<String, Stack<Command>>  gameCommands = new HashMap<>();
    private HashMap<String, CommandInvoker> invokerMap = new HashMap<>();
    private HashMap<String, Triplet<Boolean,String,String>> chatMap = new HashMap<>();
    private Request request = new RequestAdapter();


    @RequestMapping(value= "/createGame", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> createGame(@RequestBody String json) {
        JSONObject obj = new JSONObject(json);
        if(obj.has("playerName")){
            Durak durak = DurakFactory.getDurak("");
            durak.getPlayerOne().setName(obj.getString("playerName"));
            return ResponseEntity.status(HttpStatus.OK).body(Message.formGameCreated(durak.getID().toString(), durak.getPlayerOne()).toString());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JSONObject("{ \"error\": \"PlayerName not found\" }").toString());
    }

    @RequestMapping(value = "/joinGame", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> joinGame(@RequestBody String json) {
        JSONObject obj = new JSONObject(json);
        if(request.joinGameRequest(obj)){
            return ResponseEntity.status(HttpStatus.OK).body(Message.formPlayerJoined(DurakFactory.getDurak(obj.getString("gameID")).getPlayerTwo()).toString());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JSONObject("{ \"error\": \"Game not found\" }").toString());
    }

    @RequestMapping(value = "/input", method = RequestMethod.POST)
    public ResponseEntity<String> input(@RequestBody String json) {
        JSONObject obj = new JSONObject(json);
        String gameID = obj.getString("gameID");
        Durak durak = DurakFactory.getDurak(gameID);
        Stack<Command> commands = DurakFactory.getGameCommands(gameID);
        int input = obj.getInt("command");
        Command command;
        if(input != 0)
            command = new PlayerInputCommand(durak, obj.getString("playerID"), input);
        else
            command = new RoundEndCommand(durak, obj.getString("playerID"));
        CommandInvoker invoker = DurakFactory.getCommandInvoker(gameID);
        invoker.sendCommand(command);
        Memento state = durak.saveState();
        DurakFactory.getCareTaker(gameID).add(state);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/poll/{gameID}/{playerID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> poll(@PathVariable String gameID, @PathVariable String playerID){
        String response = request.pollRequest(gameID, playerID);
        if(response != "")
            return ResponseEntity.status(HttpStatus.OK).body(response);
        return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/undo", method = RequestMethod.POST)
    public ResponseEntity<String> undo(@RequestBody String json) {
        JSONObject obj = new JSONObject(json);
        String gameID = obj.getString("gameID");
        CommandInvoker invoker = DurakFactory.getCommandInvoker(gameID);
        Pair<Durak, Thread> pair =  invoker.undoCommand();
        if(pair != null){
            DurakFactory.replaceDurak(gameID,pair.getValue0());
            DurakFactory.replaceGameThread(gameID, pair.getValue1());
        }
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> chat(@RequestBody String json) {
        JSONObject obj = new JSONObject(json);
        if(obj.has("gameID") && obj.has("playerID") && obj.has("message")) {
            String gameID = obj.getString("gameID");
            String playerId = obj.getString("playerID");
            String message = obj.getString("message");
            Durak game = DurakFactory.getDurak(gameID);
            if(!game.getPlayerOne().getID().toString().equals(playerId)) {
                game.getPlayerOne().addMessage(Message.formChat(game.getPlayerTwo().toString(), message));
            } else if(!game.getPlayerTwo().getID().toString().equals(playerId)){
                game.getPlayerTwo().addMessage(Message.formChat(game.getPlayerOne().toString(), message));
            }
            System.out.println("CHAT SUCCESS");
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        }
        System.out.println("CHAT FAILED");
        return new ResponseEntity<>("Something went wrong", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/restart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> restart(@RequestBody String json) {
        JSONObject obj = new JSONObject(json);
        if(obj.has("gameID")) {
            String gameID = obj.getString("gameID");
            Durak game = DurakFactory.getDurak(gameID);
            Thread thread = DurakFactory.getGameThread(gameID);
            thread.interrupt();
            Memento state = DurakFactory.getCareTaker(gameID).get(0);
            game = game.restoreState(state);
            thread = new Thread(game);
            thread.start();
            game.sendStatusToClient();
            DurakFactory.replaceGameThread(gameID, thread);
            DurakFactory.replaceDurak(gameID, game);
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        }
        System.out.println("RESTART FAILED");
        return new ResponseEntity<>("Something went wrong", HttpStatus.NOT_FOUND);
    }

}
