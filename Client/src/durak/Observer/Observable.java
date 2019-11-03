package durak.Observer;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private Object o;
    private List<Observer> observers = new ArrayList<>();
 
    public void addObserver(Observer observer) {
        this.observers.add(observer);
        System.out.println ("Observer '"+observer.toString()+"' was added.");
    }
 
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
        System.out.println ("Observer '"+observer.toString()+"' was removed.");
    }
 
    public void notifyObservers(Object o) {
        this.o = o;
        for (Observer observer : this.observers) {
            System.out.println ("Observer '"+observer.toString()+"' was notified.");
            observer.update(this.o);
        }
    }
}
