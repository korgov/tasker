package ru.korgov.tasker.regexp;

import org.json.JSONObject;
import ru.korgov.tasker.regexp.creators.TaskCreator;

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
        final JFrame form = new JFrame("RegExp Task Creator");
        final TaskCreator taskClient = new TaskCreator(form, "localhost:9000", "loadTasksByType?task-type-id=1", new JSONObject());
        form.setContentPane(taskClient.getMainPanel());
        form.setMinimumSize(new Dimension(700, 550));
        form.pack();
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.setVisible(true);
    }

}
