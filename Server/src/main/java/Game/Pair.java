package Game;

import org.bson.types.ObjectId;

public class Pair {
    private ObjectId id;
    private Card attacker;
    private Card defender;
    private Field field;

    public Pair(Field field, Card attacker, Card defender){
        id = new ObjectId();
        this.field = field;
        this.attacker = attacker;
        this.defender = defender;
    }
}
