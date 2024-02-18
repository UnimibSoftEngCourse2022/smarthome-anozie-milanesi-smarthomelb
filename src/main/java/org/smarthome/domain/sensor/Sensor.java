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
        notifyDataChange();
    }

    public void startDetection() {
        // MAPE feedback control loop
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::mapeIteration,
                        0, Constants.sensorIterationPeriodMsDuration(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void mapeIteration() {
        T detected = monitor();
        if (analyze(detected)) {
            plan(detected);
            execute(detected);
        }
    }

    private void notifyDataChange() {
        observers.forEach(observer -> observer.onDataChange(data));
    }

}
