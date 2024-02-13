package org.smarthome.simulation;

import org.smarthome.util.Constants;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RoomTemperatureSimulation {

    private RoomTemperatureSimulationListener roomTemperatureSimulationListener;
    // temperatura attuale nella stanza
    private int temperature;
    // target di temperatura che si vuole raggiungere
    private int target;
    private final ScheduledExecutorService temperatureExecutor;

    public RoomTemperatureSimulation() {
        this.temperature = Constants.defaultIdealTemperature();
        this.target = Constants.defaultIdealTemperature();
        this.temperatureExecutor = Executors.newSingleThreadScheduledExecutor();
        startTemperatureSimulation();
    }

    public void setRoomTemperatureListener(RoomTemperatureSimulationListener roomTemperatureSimulationListener) {
        this.roomTemperatureSimulationListener = roomTemperatureSimulationListener;
    }

    public synchronized int getTemperature() {
        return temperature;
    }

    public synchronized void setTemperature(int temperature) {
        this.temperature = temperature;
        if (roomTemperatureSimulationListener != null) {
            roomTemperatureSimulationListener.onTemperatureChange(temperature);
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
        }, 0, Constants.temperatureChangeMsDuration(), TimeUnit.MILLISECONDS);
    }

    public void stopTemperatureChange() {
        setTarget(getTemperature());
        if (roomTemperatureSimulationListener != null) {
            roomTemperatureSimulationListener.onStopTemperatureChange();
        }
    }

}
