package Game;

import java.io.*;
import java.util.Base64;

public class PlayerInputCommand implements Command, Serializable {

    private Durak durak;
    private String playerID;
    private int input;
    private String binary;
    public PlayerInputCommand(Durak durak, String playerID, int input){
        this.durak = durak;
        this.playerID = playerID;
        this.input = input;
    }

    public void execute(){
        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( stream );
            oos.writeObject( durak );
            oos.close();
            binary = Base64.getEncoder().encodeToString(stream.toByteArray());
        } catch (IOException ex){
            System.out.println(ex.toString());
        }
        if(playerID.compareTo(durak.getPlayerOne().getID().toString()) == 0){
            durak.getPlayerOne().addInput(input);
        }
        else if(playerID.compareTo(durak.getPlayerTwo().getID().toString()) == 0){
            durak.getPlayerTwo().addInput(input);
        }
        System.out.println("PlayerInputCommand execute" + input);
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
            System.out.println("PlayerInputCommand undo" + input);
            durak.sendStatusToClient();
            return newthread;
        } catch (IOException | ClassNotFoundException ex){
            System.out.println(ex.toString());
        }
        return null;
    }

    public Durak getDurak() {return durak; }
}
