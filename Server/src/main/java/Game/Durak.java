package Game;

import org.bson.types.ObjectId;

public class Durak {
    private ObjectId id;
    private String trump;
    private int round;
    private boolean roundInitiated;
    private Player attacker;
    private Player defender;

    public Durak(Player attacker, Player defender){
        id = new ObjectId();
        this.attacker = attacker;
        this.defender = defender;
        trump = "";
        round = 1;
        roundInitiated = false;
    }
}
