package Game;

import Server.Request;
import Server.RequestAdapter;
import org.javatuples.Pair;

import java.io.*;
import java.util.Base64;

public class RoundEndCommand implements Command, Serializable {

    private Durak durak;
    private String playerID;
    private String binary;
    private Request request = new RequestAdapter();
    public RoundEndCommand(Durak durak, String playerID){
        this.durak = durak;
        this.playerID = playerID;
    }

    public boolean execute(){
        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( stream );
            oos.writeObject( durak );
            oos.close();
            binary = Base64.getEncoder().encodeToString(stream.toByteArray());
        } catch (IOException ex){

        }
        boolean rez = request.inputRequest(durak, playerID, 0);
        System.out.println("RoundEndCommand execute");
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
            System.out.println("RoundEndCommand undo");
            durak.sendStatusToClient();
            Pair<Durak, Thread> pair = new Pair<>(durak, newthread);
            return pair;
        } catch (IOException | ClassNotFoundException ex){

        }
        return null;
    }
}
