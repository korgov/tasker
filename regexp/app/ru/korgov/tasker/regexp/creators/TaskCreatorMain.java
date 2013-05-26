package ru.korgov.tasker.regexp.creators;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 24.12.12
 */
public class TaskCreatorMain {

    public static void main(final String[] args) throws JSONException {
        final JSONObject context = new JSONObject(args[0]);
        final String hostname = context.getString("hostname");
        final String tasksDataQuery = context.getString("tasks-data-query");
        final JSONObject saveMetaInfo = context.getJSONObject("meta-info");

        initForm(hostname, tasksDataQuery, saveMetaInfo);
    }

    private static void initForm(final String hostname, final String tasksDataQuery, final JSONObject saveMetaInfo) {
        final JFrame form = new JFrame("RegExp Task Creator");
        final TaskCreator taskClient = new TaskCreator(form, hostname, tasksDataQuery, saveMetaInfo);
        form.setContentPane(taskClient.getMainPanel());
        form.setMinimumSize(new Dimension(700, 550));
        form.pack();
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.setVisible(true);
    }

}
