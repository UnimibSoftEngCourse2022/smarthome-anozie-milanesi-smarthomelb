package org.smarthome.listener;

import org.smarthome.domain.protection.AlarmState;
import org.smarthome.exception.WrongSecurityPinException;

public interface AlarmListener extends ElementListener {
    void onChangeState(AlarmState state);
    void onWrongSecurityPinException(WrongSecurityPinException e);
}
