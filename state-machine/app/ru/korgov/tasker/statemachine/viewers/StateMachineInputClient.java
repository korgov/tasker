package ru.korgov.tasker.statemachine.viewers;

import javax.swing.*;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 27.05.13 4:53
 */
public class StateMachineInputClient {
    private JPanel mainPanel;
    private JTable trigTable;
    private JTextField textField1;
    private JTextField textField2;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public StateMachineInputClient() {
        trigTable.setModel(null); //todo
    }
}
