package org.smarthome.domain.protection;

import org.smarthome.listener.ObservableElement;
import org.smarthome.listener.SirenListener;

public class Siren extends ObservableElement<SirenListener> {

    private boolean active;

    public Siren() {
        super();
        this.active = false;
    }

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void setActive(boolean active) {
        if (this.active != active) {
            this.active = active;
            for (SirenListener observer : observers) {
                observer.onChangeState(active);
            }
        }
    }

}
