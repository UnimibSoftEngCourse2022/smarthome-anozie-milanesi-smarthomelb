package org.smarthome.simulation;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.smarthome.util.Constants.DEFAULT_IDEAL_TEMPERATURE;
import static org.smarthome.util.Constants.TEMPERATURE_CHANGE_MS_DURATION;

public class RoomTemperatureSimulation {

    private RoomTemperatureListener roomTemperatureListener;
    private int temperature;
    private int target;
    private final ScheduledExecutorService temperatureExecutor;

    public RoomTemperatureSimulation() {
        this.temperature = DEFAULT_IDEAL_TEMPERATURE;
        this.target = DEFAULT_IDEAL_TEMPERATURE;
        this.temperatureExecutor = Executors.newSingleThreadScheduledExecutor();
        startTemperatureSimulation();
    }

    public void setRoomTemperatureListener(RoomTemperatureListener roomTemperatureListener) {
        this.roomTemperatureListener = roomTemperatureListener;
    }

    public synchronized int getTemperature() {
        return temperature;
    }

    private synchronized void setTemperature(int temperature) {
        this.temperature = temperature;
        if (roomTemperatureListener != null) {
            roomTemperatureListener.onTemperatureChange(temperature);
        }
    }

    public synchronized int getTarget() {
        return target;
    }

    public synchronized void setTarget(int target) {
        this.target = target;
    }

    public void startTemperatureSimulation() {
        temperatureExecutor.scheduleAtFixedRate(() -> {
            int currentTemperature = getTemperature();
            int currentTarget = getTarget();
            if (currentTemperature < currentTarget) {
                setTemperature(currentTemperature + 1);
            } else if (currentTemperature > currentTarget) {
                setTemperature(currentTemperature - 1);
            }
        }, 0, TEMPERATURE_CHANGE_MS_DURATION, TimeUnit.MILLISECONDS);
    }

    public void stopTemperatureChange() {
        setTarget(getTemperature());
        if (roomTemperatureListener != null) {
            roomTemperatureListener.onStopTemperatureChange();
        }
    }

}
