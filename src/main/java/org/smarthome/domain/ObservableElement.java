package org.smarthome.domain;

import org.smarthome.domain.listener.ElementListener;

import java.util.ArrayList;
import java.util.List;

public abstract class ObservableElement<T extends ElementListener> {

    protected final List<T> observers;

    public ObservableElement() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(T observer) {
        observers.add(observer);
    }

    public void removeObserver(T observer) {
        observers.remove(observer);
    }

}
