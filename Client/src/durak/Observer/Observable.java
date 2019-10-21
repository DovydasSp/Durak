package durak.Observer;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private Object o;
    private List<Observer> observers = new ArrayList<>();
 
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }
 
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }
 
    public void notifyObservers(Object o) {
        this.o = o;
        for (Observer observer : this.observers) {
            observer.update(this.o);
        }
    }
}
