package org.smarthome.domain.sensor;

import org.smarthome.listener.ObservableElement;
import org.smarthome.listener.SensorListener;
import org.smarthome.util.Constants;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class Sensor<T>
        extends ObservableElement<SensorListener<T>>
        implements MAPEControl<T> {

    protected T data;

    protected Sensor() {
        super();
        startDetection();
    }

    public synchronized T getData() {
        return data;
    }

    public void updateData(T data) {
        this.data = data;
    }

    public void notifyDataChange() {
        observers.forEach(observer -> observer.onDataChange(data));
    }

    public void startDetection() {
        // MAPE feedback control loop
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::MAPEIteration,
                        0, Constants.sensorIterationPeriodMsDuration(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void MAPEIteration() {
        T detected = monitor();
        if (analyze(detected)) {
            plan(detected);
            execute(detected);
        }
    }

}
