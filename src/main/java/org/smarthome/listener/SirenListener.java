package org.smarthome.listener;

public interface SirenListener extends ElementListener {
    void onChangeState(boolean active);
}
