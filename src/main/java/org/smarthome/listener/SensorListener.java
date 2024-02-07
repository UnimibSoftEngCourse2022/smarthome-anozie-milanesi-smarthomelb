package org.smarthome.listener;

public interface SensorListener<T> extends ElementListener {
    void onDataChange(T data);
}
