package Game;

import Server.Request;
import Server.RequestAdapter;
import org.javatuples.Pair;

import java.io.*;
import java.util.Base64;

public class RoundEndCommand extends Command implements Serializable {

    public RoundEndCommand(Durak durak, String playerID){
        super(durak, playerID, 0);
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
