package Game;

import java.io.*;
import java.util.Base64;

public class RoundEndCommand implements Command, Serializable {

    private Durak durak;
    private String playerID;
    private String binary;
    public RoundEndCommand(Durak durak, String playerID){
        this.durak = durak;
        this.playerID = playerID;
    }

    public void execute(){
        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( stream );
            oos.writeObject( this );
            oos.close();
            binary = Base64.getEncoder().encodeToString(stream.toByteArray());
        } catch (IOException ex){

        }
        if(playerID.compareTo(durak.getPlayerOne().getID().toString()) == 0){
            durak.getPlayerOne().addInput(0);
        }
        else if(playerID.compareTo(durak.getPlayerTwo().getID().toString()) == 0){
            durak.getPlayerTwo().addInput(0);
        }
        System.out.println("RoundEndCommand execute");
    }

    public Thread undo(){
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
            return newthread;
        } catch (IOException | ClassNotFoundException ex){

        }
        return null;
    }

    public Durak getDurak() {return durak; }
}
