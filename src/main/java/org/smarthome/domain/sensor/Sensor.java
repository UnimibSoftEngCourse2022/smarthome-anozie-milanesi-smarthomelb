package org.smarthome.domain.sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public abstract class Sensor<T> implements MAPEControl<T> {

    private final List<SensorListener<T>> observers;
    protected T data;

    public Sensor() {
        this.observers = new ArrayList<>();
        startDetection();
    }

    public void addObserver(SensorListener<T> observer) {
        observers.add(observer);
    }

    public void removeObserver(SensorListener<T> observer) {
        observers.remove(observer);
    }

    public void notifyDataChange() {
        observers.forEach(observer -> observer.onDataChange(data));
    }

    public void startDetection() {
        Executors.newSingleThreadExecutor().submit((Runnable) () -> {
            // MAPE feedback control loop
            while (true) {
                knowledge();
            }
        });
    }

    private void knowledge() {
        T detected = monitor();
        if (analyze(detected)) {
            plan();
            execute();
        }
    }

}
