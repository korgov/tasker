package ru.korgov.tasker.statemachine.viewers;

import org.json.JSONException;
import org.json.JSONObject;
import ru.korgov.tasker.statemachine.model.StTask;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 29.05.13 22:10
 */
public class StateMachineInputClientMain {
    public static void main(final String[] args) throws JSONException {
        final JSONObject context = new JSONObject(args[0]);
        final String hostname = context.getString("hostname");
        final JSONObject rawTask = context.getJSONObject("task-context");
        final StTask stTask = StTask.fromJson(rawTask);
        initForm(hostname, stTask);
    }

    private static void initForm(final String hostname, final StTask task) {
        final JFrame form = new JFrame("StateMachine Tasker");
        final StateMachineInputClient taskClient = new StateMachineInputClient(form, hostname, task);
        form.setContentPane(taskClient.getMainPanel());
        form.setMinimumSize(new Dimension(700, 550));
        form.pack();
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        form.setVisible(true);
    }
}
