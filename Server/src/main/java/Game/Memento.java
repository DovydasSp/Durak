package Game;

public class Memento {

    String state;

    public void getState(Durak org) {
        org.setState(this.state);
    }

    public Memento(String newState){
        state = newState;
    }

    private String getState(){
        return state;
    }



}
