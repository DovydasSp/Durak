package Game;

import org.bson.types.ObjectId;
import java.util.ArrayList;

public class Field {
    private ObjectId id;
    private boolean completed;
    private Durak durak;
    private ArrayList<Pair> playedRanks;

    public Field(Durak durak){
        id = new ObjectId();
        completed = false;
        this.durak = durak;
        playedRanks = new ArrayList<>();
    }
}
