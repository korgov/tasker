package ru.korgov.tasker.regexp;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 24.12.12
 */
public class MainTaskClient {
    public static void main(final String[] args) {
        clientForm();
    }

    private static void clientForm() {
        final JFrame form = new JFrame("RegExp Tasker");
        final TaskClient taskClient = new TaskClient(form, System.getProperty("user.dir") + "/tasks.tsk");
        form.setContentPane(taskClient.getMainPanel());
        form.setMinimumSize(new Dimension(700, 550));
        form.pack();
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.setVisible(true);
    }
}
