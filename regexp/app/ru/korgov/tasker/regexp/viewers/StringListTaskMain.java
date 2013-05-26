package ru.korgov.tasker.regexp.viewers;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 24.12.12
 */
public class StringListTaskMain {
    public static void main(final String[] args) throws JSONException {
        final JSONObject context = new JSONObject(args[0]);
        final String hostname = context.getString("hostname");
        final JSONObject rawTask = context.getJSONObject("task-context");
        final JSONObject regExpTask = rawTask.getJSONObject("info");

        initForm(hostname, regExpTask);
    }

    private static void initForm(final String hostname, final JSONObject regExpTask) {
        final JFrame form = new JFrame("RegExp Tasker");
        final StringListTask taskClient = new StringListTask(form, hostname, regExpTask);
        form.setContentPane(taskClient.getMainPanel());
        form.setMinimumSize(new Dimension(700, 550));
        form.pack();
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.setVisible(true);
    }

}
