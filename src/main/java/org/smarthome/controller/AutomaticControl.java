package org.smarthome.controller;

import org.smarthome.domain.ObservableElement;
import org.smarthome.listener.AutomationListener;

public abstract class AutomaticControl<T> extends ObservableElement<AutomationListener> {

    private boolean automationActive;

    protected AutomaticControl() {
        this.automationActive = false;
    }

    public boolean isAutomationActive() {
        return automationActive;
    }

    public void setAutomationActive(boolean automationActive) {
        if (this.automationActive != automationActive) {
            this.automationActive = automationActive;
            for (AutomationListener observer : observers) {
                observer.onChangeState(automationActive);
            }
        }
    }

    public abstract void handleAutomation(T data);

}
