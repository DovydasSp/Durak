package Game;

import Server.Request;
import Server.RequestAdapter;
import org.javatuples.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

public abstract class Command {
    protected Durak durak;
    protected String playerID;
    protected int input;
    protected String binary;
    protected Request request = new RequestAdapter();

    public Command(Durak durak, String playerID, int input){
        this.durak = durak;
        this.playerID = playerID;
        this.input = input;
    }

    public final boolean execute(){
        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( stream );
            oos.writeObject( durak );
            oos.close();
            binary = Base64.getEncoder().encodeToString(stream.toByteArray());
        } catch (IOException ex){
            System.out.println(ex.toString());
        }
        boolean rez = request.inputRequest(durak, playerID, input);
        System.out.println("PlayerInputCommand execute " + input);
        return rez;
    }
    public abstract Pair<Durak, Thread> undo();
}
