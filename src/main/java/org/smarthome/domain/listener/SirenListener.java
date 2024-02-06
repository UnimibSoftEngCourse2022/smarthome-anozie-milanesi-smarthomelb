package org.smarthome.domain.listener;

public interface SirenListener extends ElementListener {
    void onChangeState(boolean active);
}
