package org.smarthome.controller;

public abstract class AutomaticControl<T> {

    public boolean automationActive;

    public AutomaticControl() {
        this.automationActive = false;
    }

    public boolean isAutomationActive() {
        return automationActive;
    }

    public void setAutomationActive(boolean automationActive) {
        this.automationActive = automationActive;
    }

    public abstract void handleAutomation(T data);

}
