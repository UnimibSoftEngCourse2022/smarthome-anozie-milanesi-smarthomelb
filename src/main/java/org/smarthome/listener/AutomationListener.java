package org.smarthome.listener;

public interface AutomationListener extends ElementListener {
    void onChangeState(boolean automationActive);
}
