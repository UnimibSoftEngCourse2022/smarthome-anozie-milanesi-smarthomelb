package org.smarthome.domain;

import org.smarthome.controller.SensorControl;

public class AutomaticSystem {

    private static AutomaticSystem instance = null;

    private AutomaticSystem() {}

    public static synchronized AutomaticSystem getInstance() {
        if (instance == null) {
            instance = new AutomaticSystem();
        }
        return instance;
    }

    public void automateSensorControl(SensorControl<?> sensorControl) {
        sensorControl.monitorSensor();
    }

}
