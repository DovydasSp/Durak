package Game;

import org.javatuples.Pair;

import java.util.Stack;

public class CommandInvoker {

    private Stack<Command> commands = new Stack<>();

    public void sendCommand(Command command){
        commands.add(command);
        command.execute();
    }

    public Pair<Durak, Thread> undoCommand(){
        Command command;
        if(commands.size() > 0){
            command = commands.pop();
            return command.undo();
        }
        return null;
    }



}
