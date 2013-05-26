package ru.korgov.tasker.regexp.creators;

import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;
import ru.korgov.tasker.regexp.StringsListModel;
import ru.korgov.tasker.regexp.model.RegExpTask;
import ru.korgov.tasker.regexp.serializer.RegExpJSONTaskSerialiser;
import ru.korgov.tasker.regexp.serializer.RegExpTaskSerialiser;
import ru.korgov.util.JSONUtils;
import ru.korgov.util.UrlUtils;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Cu;
import ru.korgov.util.alias.Fu;
import ru.korgov.util.alias.Su;
import ru.korgov.util.func.Function;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 24.12.12
 */
public class TaskCreator {
    private JTextField titleTextField;
    private JTextArea descrTextArea;
    private JTextArea sourceDataTextArea;
    private JTextArea resultTextArea;
    private JList tasksList;
    private JButton updateTasksButton;
    private JButton newTaskButton;
    private JButton saveTaskButton;
    private JLabel taskIdLabel;
    private JPanel mainPanel;
    private JLabel newTaskLabel;

    private JFrame frame;

    private Map<Long, RegExpTask> taskIdToTask = Cf.newLinkedMap();
    private Map<Integer, Long> indexToTaskId = Cf.newMap();

    private RegExpTaskSerialiser taskSerialiser;
    private String hostname;
    private String tasksDataQuery;
    private JSONObject saveMetaInfo;

    private static final String JSON_CONTENT_TYPE = "application/json";

    public TaskCreator(final JFrame frame, final String hostname, final String tasksDataQuery, final JSONObject saveMetaInfo) {
        this.frame = frame;
        this.saveMetaInfo = saveMetaInfo;
        this.taskSerialiser = new RegExpJSONTaskSerialiser();
        this.hostname = hostname;
        this.tasksDataQuery = tasksDataQuery;

        updateTasksButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                reloadTasksAction();
            }
        });
        tasksList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                final int taskIndex = tasksList.getSelectedIndex();
                if (taskIndex != -1) {
                    final Long taskId = indexToTaskId.get(taskIndex);
                    final RegExpTask task = taskIdToTask.get(taskId);
                    newTaskLabel.setVisible(false);
                    showTaskInForm(task);
                }
            }
        });
        newTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                newTaskLabel.setVisible(true);
                tasksList.clearSelection();
                showTaskInForm(emptyTask(-1L));
            }
        });
        saveTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final Long taskId = Long.valueOf(taskIdLabel.getText());
                if (taskId != null) {
                    final String taskTitle = titleTextField.getText();
                    final String taskDesc = descrTextArea.getText();
                    final List<String> sourceData = Cf.list(sourceDataTextArea.getText().split("\\n"));
                    final List<String> resultData = Cf.list(resultTextArea.getText().split("\\n"));
                    final RegExpTask taskToPut = RegExpTask.matchFromListTask(taskId, taskTitle, taskDesc, sourceData, resultData);
                    taskIdToTask.put(taskId, taskToPut);
                    try {
                        final String newTasksSet = UrlUtils.sendPostRequest("http://" + hostname + "/saveTasks", buildSaveReqData().toString(), JSON_CONTENT_TYPE);
                        reloadTasksAction(newTasksSet);
                    } catch (Exception exp) {
                        JOptionPane.showMessageDialog(frame, exp.getMessage(), "Ошибка при сохранении задания!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        reloadTasksAction();
    }

    private void reloadTasksAction() {
        try {
            reloadTasksAction(loadRawTasks());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Ошибка при загрузке заданий!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reloadTasksAction(final String rawTasksArray) throws JSONException {
        updateTasksFromReq(rawTasksArray);
        refillTasksList();
        tasksList.setSelectedIndex(0);
    }

    private JSONObject buildSaveReqData() throws JSONException {
        final List<JSONObject> rawTasksData = Cu.map(new Function<RegExpTask, JSONObject>() {
            @Override
            public JSONObject apply(final RegExpTask task) {
                final JSONObject out = new JSONObject();
                try {
                    out.put("id", task.getId());
                    out.put("desc", task.getDescription());
                    out.put("title", task.getTitle());
                    out.put("info", taskSerialiser.taskToJson(task));
                } catch (JSONException exp) {
                    throw new RuntimeException(exp);
                }
                return out;
            }
        }, taskIdToTask.values());
        final JSONObject reqData = new JSONObject();
        reqData.put("tasks", new JSONArray(rawTasksData));
        reqData.put("meta-info", saveMetaInfo);
        return reqData;
    }

    private RegExpTask emptyTask(final long taskId) {
        return RegExpTask.matchFromListTask(taskId, "Задание №" + taskId, "", Collections.<String>emptyList(), Collections.<String>emptyList());
    }

    private void showTaskInForm(final RegExpTask task) {
        if (task != null) {
            taskIdLabel.setText(String.valueOf(task.getId()));
            titleTextField.setText(task.getTitle());
            descrTextArea.setText(task.getDescription());
            sourceDataTextArea.setText(Su.join(task.getSourceData(), "\n"));
            resultTextArea.setText(Su.join(task.getResultData(), "\n"));
        }
    }

    private void refillTasksList() {
        final Collection<RegExpTask> tasks = taskIdToTask.values();
        final List<String> titles = createTaskTitles(tasks);
        int i = 0;
        for (final RegExpTask task : tasks) {
            indexToTaskId.put(i, task.getId());
            i++;
        }
        tasksList.setModel(new StringsListModel(titles));
    }

    private List<String> createTaskTitles(final Collection<RegExpTask> tasks) {
        return Cu.map(tasks, new Fu<RegExpTask, String>() {
            @Override
            public String apply(final RegExpTask task) {
                return "#" + task.getId() + " - " + task.getTitle();
            }
        });
    }

    private String loadRawTasks() throws IOException {
        return UrlUtils.sendGetRequest("http://" + hostname + "/" + tasksDataQuery);

//        taskIdToTask.put(1L, Task.matchFromListTask(1L, "Задание №1",
//                "Напиши регулярное выражение для выборки из заданных строк номеров телефонов с кодом города.",
//                Arrays.asList("+7 921 9285726", "123-45-67", "8(921)9285726", "+7 (921) 928-5726", "333-278"),
//                Arrays.asList("+7 921 9285726", "8(921)9285726", "+7 (921) 928-5726")
//        ));
//
//        taskIdToTask.put(2L, Task.matchFromListTask(2L, "Задание №2",
//                "Напиши регулярное выражение для поиска всех адресов электронной почты в заданных строках",
//                Arrays.asList("e-mail: kirill@korgov.ru, korgov@yandex.ru", "phone: +79219285726", "url: www.korgov.ru", "Коргов Кирилл (korgovk@gmail.com)"),
//                Arrays.asList("kirill@korgov.ru", "korgov@yandex.ru", "korgovk@gmail.com")
//        ));
    }

    private void updateTasksFromReq(final String rawTasksArray) throws JSONException {
        final List<JSONObject> rawTasksAsList = JSONUtils.asJSONObjectList(new JSONObject(rawTasksArray).getJSONArray("tasks"));

        final List<JSONObject> regExpTasks = Cu.map(new Function<JSONObject, JSONObject>() {
            @Override
            public JSONObject apply(final JSONObject rawTask) {
                try {
                    final JSONObject info = rawTask.getJSONObject("info");
                    info.put("id", rawTask.getLong("id"));
                    return info;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, rawTasksAsList);

        final List<RegExpTask> tasks = taskSerialiser.jsonToTasks(regExpTasks);
        taskIdToTask.clear();
        for (final RegExpTask task : tasks) {
            taskIdToTask.put(task.getId(), task);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
