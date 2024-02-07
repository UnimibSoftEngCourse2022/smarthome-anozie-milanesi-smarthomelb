package org.smarthome.domain.sensor;

import org.smarthome.domain.ObservableElement;
import org.smarthome.listener.SensorListener;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.smarthome.util.Constants.SENSOR_ITERATION_PERIOD_MS_DURATION;

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
                .scheduleAtFixedRate(this::knowledge,
                        0, SENSOR_ITERATION_PERIOD_MS_DURATION, TimeUnit.MILLISECONDS);
    }

    @Override
    public void knowledge() {
        T detected = monitor();
        if (analyze(detected)) {
            plan();
            execute();
        }
    }

}
