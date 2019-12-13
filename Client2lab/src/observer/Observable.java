package observer;

import chain.AbstractLogger;
import chain.ChainLogger;
import gamedataclasses.Iterator;
import java.util.ArrayList;
import java.util.List;

public abstract class Observable {

    private Object o;
    private List<Observer> observers = new ArrayList<>();
    private ChainLogger loggerChain = new ChainLogger();
    
    public void addObserver(Observer observer) {
        this.observers.add(observer);
        //loggerChain.logMessage(AbstractLogger.PATTERN, "Observer '" + observer.toString() + "' was added.");
    }

    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
        //loggerChain.logMessage(AbstractLogger.PATTERN,"Observer '" + observer.toString() + "' was removed.");
    }

    public void notifyObservers(Object o) {
        this.o = o;
        for (Iterator iter = getIterator(); iter.hasNext();) {
            Observer observer = (Observer) iter.next();
            //loggerChain.logMessage(AbstractLogger.PATTERN,"Observer '" + observer.toString() + "' was notified.");
            observer.update(this.o);
        }
    }

    public Iterator getIterator() {
        return new ObservableObserverIterator();
    }

    public class ObservableObserverIterator implements Iterator {

        int index;

        @Override
        public boolean hasNext() {
            loggerChain.logMessage(AbstractLogger.PATTERN,"Iterator of observable called hasNext.");
            if (index < observers.size()) {
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            loggerChain.logMessage(AbstractLogger.PATTERN,"Iterator of observable called next.");
            if (this.hasNext()) {
                return observers.get(index++);
            }
            return null;
        }

        @Override
        public boolean hasPrevious() {
            loggerChain.logMessage(AbstractLogger.PATTERN,"Iterator of observable called hasPrevious.");
            if (index > 0) {
                return true;
            }
            return false;
        }

        @Override
        public Object previous() {
            loggerChain.logMessage(AbstractLogger.PATTERN,"Iterator of observable called previous.");
            if (this.hasPrevious()) {
                return observers.get(--index);
            }
            return null;
        }

        @Override
        public Object first() {
            loggerChain.logMessage(AbstractLogger.PATTERN,"Iterator of observable called first.");
            return observers.get(0);
        }
    }
}
