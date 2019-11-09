package Game;

import org.javatuples.Pair;

public interface Command {
    public boolean execute();
    public Pair<Durak, Thread> undo();
}
