package state;

import durak.Game;

public interface State {
    public void doAction(Context context, Game game, String message);
    public int stateNr();
}
