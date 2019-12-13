package Game;

import java.util.HashMap;
import java.util.Stack;

public class DurakFactory {
    private final static HashMap<String, Durak> gameMap = new HashMap<>();
    private final static HashMap<String, Thread> gameThreads = new HashMap<>();
    private final static HashMap<String, Stack<Command>>  gameCommands = new HashMap<>();
    private final static HashMap<String, CommandInvoker> invokerMap = new HashMap<>();
    private final static HashMap<String, Caretaker> caretakerMap = new HashMap<>();
    public static Durak getDurak(String gameID) {
        Durak durak = gameMap.get(gameID);

        if(durak == null) {
            durak = new Durak();
            gameMap.put(durak.getID().toString(), durak);
        }

        return durak;
    }

    public static Thread getGameThread(String gameID) {
        Thread thread = gameThreads.get(gameID);

        if(thread == null) {
            Durak durak = getDurak(gameID);
            thread = new Thread(durak);
            gameThreads.put(gameID, thread);
        }

        return thread;
    }

    public static Stack<Command> getGameCommands(String gameID) {
        Stack<Command> commands = gameCommands.get(gameID);

        if(commands == null) {
            commands = new Stack<>();
            gameCommands.put(gameID, commands);
        }

        return commands;
    }

    public static CommandInvoker getCommandInvoker(String gameID) {
        CommandInvoker invoker = invokerMap.get(gameID);

        if(invoker == null) {
            invoker = new CommandInvoker();
            invokerMap.put(gameID, invoker);
        }

        return invoker;
    }

    public static void replaceDurak(String gameID, Durak durak) {
        gameMap.put(gameID, durak);
    }

    public static void replaceGameThread(String gameID, Thread gameThread) {
        gameThreads.put(gameID, gameThread);
    }

    public static Caretaker getCareTaker(String gameID) {
        Caretaker caretaker = caretakerMap.get(gameID);

        if(caretaker == null) {
            caretaker = new Caretaker();
            caretakerMap.put(gameID, caretaker);
        }

        return caretaker;
    }
}
