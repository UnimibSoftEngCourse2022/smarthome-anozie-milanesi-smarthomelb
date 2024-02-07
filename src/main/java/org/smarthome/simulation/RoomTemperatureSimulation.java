package org.smarthome.simulation;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.smarthome.util.Constants.DEFAULT_IDEAL_TEMPERATURE;
import static org.smarthome.util.Constants.TEMPERATURE_CHANGE_MS_DURATION;

public class RoomTemperatureSimulation {

    private RoomTemperatureListener roomTemperatureListener;
    private int temperature;
    private ScheduledExecutorService temperatureExecutor;

    public RoomTemperatureSimulation() {
        this.temperature = DEFAULT_IDEAL_TEMPERATURE;
        this.temperatureExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void setRoomTemperatureListener(RoomTemperatureListener roomTemperatureListener) {
        this.roomTemperatureListener = roomTemperatureListener;
    }

    public synchronized int getTemperature() {
        return temperature;
    }

    public synchronized void setTemperature(int temperature) {
        this.temperature = temperature;
        if (roomTemperatureListener != null) {
            roomTemperatureListener.onTemperatureChange(temperature);
        }
    }

    public void startTemperatureChange(int target) {
        shutdownAndReset();

        temperatureExecutor.scheduleAtFixedRate(() -> {
            int currentTemperature = getTemperature();
            if (getTemperature() < target) {
                setTemperature(currentTemperature + 1);
            } else if (currentTemperature > target) {
                setTemperature(currentTemperature - 1);
            }

            if (getTemperature() == target) {
                targetTemperatureReached();
                shutdownAndReset();
            }
        }, 0, TEMPERATURE_CHANGE_MS_DURATION, TimeUnit.MILLISECONDS);
    }

    public void stopTemperatureChange() {
        shutdownAndReset();
        if (roomTemperatureListener != null) {
            roomTemperatureListener.onStopTemperatureTargetGoal();
        }
    }

    private void shutdownAndReset() {
        temperatureExecutor.shutdown();
        temperatureExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    private void targetTemperatureReached() {
        if (roomTemperatureListener != null) {
            roomTemperatureListener.onTargetTemperatureReached();
        }
    }

}
