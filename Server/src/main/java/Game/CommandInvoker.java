package Game;

public class CommandInvoker {

    private Command command;

    public void setCommand(Command command){
        this.command = command;
    }

    public void sendCommand(){
        command.execute();
    }

    public Thread undoCommand(){
        return command.undo();
    }

    public Durak getDurak() { return command.getDurak(); }



}
