package Game;

public interface Command {
    public void execute();
    public Thread undo();
    public Durak getDurak();
}
