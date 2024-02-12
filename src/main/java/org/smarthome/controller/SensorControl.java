package org.smarthome.controller;

import org.smarthome.domain.sensor.Sensor;
import org.smarthome.listener.SensorListener;

public abstract class SensorControl<T> implements SensorListener<T> {

    private final Sensor<T> sensor;

    protected SensorControl(Sensor<T> sensor) {
        this.sensor = sensor;
    }

    public void monitorSensor() {
        if (sensor != null) {
            sensor.addObserver(this);
        }
    }

    public void stopMonitorSensor() {
        if (sensor != null) {
            sensor.removeObserver(this);
        }
    }

}
