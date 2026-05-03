package food_delivery_system.observer;

import food_delivery_system.observer.Observer;

// Subject interface
public interface OrderSubject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}