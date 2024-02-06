package org.smarthome.domain.listener;

public interface SensorListener<T> extends ElementListener {
    void onDataChange(T data);
}
