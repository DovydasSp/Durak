package Game;

import Server.Request;
import Server.RequestAdapter;
import org.javatuples.Pair;

import java.io.*;
import java.util.Base64;

public class PlayerInputCommand implements Command, Serializable {

    private Durak durak;
    private String playerID;
    private int input;
    private String binary;
    private Request request = new RequestAdapter();
    public PlayerInputCommand(Durak durak, String playerID, int input){
        this.durak = durak;
        this.playerID = playerID;
        this.input = input;
    }

    public boolean execute(){
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

    public Pair<Durak, Thread> undo(){
        try{
            byte [] data = Base64.getDecoder().decode(binary);
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(  data ) );
            Object o  = ois.readObject();
            Durak dur = (Durak)o;
            Thread newthread = new Thread(dur);
            newthread.start();
            durak = dur;
            System.out.println("PlayerInputCommand undo");
            durak.sendStatusToClient();
            Pair<Durak, Thread> pair = new Pair<>(durak,newthread);
            return pair;
        } catch (IOException | ClassNotFoundException ex){
            System.out.println(ex.toString());
        }
        return null;
    }
}
