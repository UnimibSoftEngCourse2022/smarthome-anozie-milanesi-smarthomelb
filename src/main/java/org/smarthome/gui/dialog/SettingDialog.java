package org.smarthome.gui.dialog;

import javax.swing.*;

import static org.smarthome.util.Constants.defaultDialogHeightSetting;
import static org.smarthome.util.Constants.defaultDialogWidthSetting;

public class SettingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextField textField;

    public SettingDialog() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        setSize(defaultDialogWidthSetting(), defaultDialogHeightSetting());
        setVisible(true);

        buttonOK.addActionListener(e -> onOK());
    }

    public String  getTemperature() {
        return textField.getText();
    }

    private void onOK() {
        dispose();
    }
}
