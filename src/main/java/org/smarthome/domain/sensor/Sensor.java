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

    public void notifyDataChange() {
        observers.forEach(observer -> observer.onDataChange(data));
    }

    public void startDetection() {
        // MAPE feedback control loop
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::loop,
                        0, Constants.sensorIterationPeriodMsDuration(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void loop() {
        T detected = monitor();
        if (analyze(detected)) {
            plan();
            execute();
        }
    }

}
