package org.smarthome;

import org.smarthome.gui.SmartHomeGUI;
import org.smarthome.util.MockSmartHome;

public class App {

    public static void main(String[] args) {
        SmartHomeGUI.init(MockSmartHome.mock()).setVisible(true);
    }

}
