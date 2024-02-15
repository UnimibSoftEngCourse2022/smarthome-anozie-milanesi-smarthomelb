package org.smarthome.gui.dialog;

import javax.swing.*;

import static org.smarthome.util.Constants.*;

public class VacuumDialog extends JDialog {
    private JPanel contentPane;
    private JLabel label;

    public VacuumDialog() {
        setContentPane(contentPane);
        setModal(true);
        setSize(defaultDialogWidthSetting(), defaultDialogHeightSetting());
    }

    public void setTitleDialog(String title) {
        setTitle(title);
    }

    public void setMessageDialog(String message) {
        label.setText(message);
    }

}
