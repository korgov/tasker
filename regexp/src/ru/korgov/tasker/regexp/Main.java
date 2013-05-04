package ru.korgov.tasker.regexp;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 16.12.12
 */
public class Main {
    public static void main(final String[] args) {
        creatorForm();
    }

    private static void creatorForm() {
        final JFrame form = new JFrame("RegExp Tasker Creator");
        final TaskCreator taskCreator = new TaskCreator(form,"http://localhost:9000/assets/jars/tasks.tsk");

        form.setContentPane(taskCreator.getMainPanel());
        form.setMinimumSize(new Dimension(700, 550));
        form.pack();
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.setVisible(true);
    }

}
