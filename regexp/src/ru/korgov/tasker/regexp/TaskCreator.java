package ru.korgov.tasker.regexp;

import ru.korgov.tasker.regexp.model.Task;
import ru.korgov.tasker.regexp.serializer.JSONTaskSerialiser;
import ru.korgov.tasker.regexp.serializer.TaskSerialiser;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Cu;
import ru.korgov.util.alias.Fu;
import ru.korgov.util.alias.Su;

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

    private Map<Long, Task> taskIdToTask = Cf.newLinkedMap();
    private Map<Integer, Long> indexToTaskId = Cf.newMap();

    private TaskSerialiser taskSerialiser;

    TaskCreator(final JFrame frame, final String tasksPath) {
        this.frame = frame;
        this.taskSerialiser = new JSONTaskSerialiser(tasksPath);

        updateTasksButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                reloadTasks();
                refillTasksList();
                tasksList.setSelectedIndex(0);
            }
        });
        tasksList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                final int taskIndex = tasksList.getSelectedIndex();
                if (taskIndex != -1) {
                    final Long taskId = indexToTaskId.get(taskIndex);
                    final Task task = taskIdToTask.get(taskId);
                    newTaskLabel.setVisible(false);
                    showTaskInForm(task);
                }
            }
        });
        newTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final long newTaskId = getNewTaskId();
                newTaskLabel.setVisible(true);
                tasksList.clearSelection();
                showTaskInForm(emptyTask(newTaskId));
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
                    final Task taskToPut = Task.matchFromListTask(taskId, taskTitle, taskDesc, sourceData, resultData);
                    taskIdToTask.put(taskId, taskToPut);
                    try {
                        taskSerialiser.saveTasks(Cf.newList(taskIdToTask.values()));
                    } catch (IOException exp) {
                        JOptionPane.showMessageDialog(frame, exp.getMessage(), "Ошибка при сохранении задания!", JOptionPane.ERROR_MESSAGE);
                    }
                    refillTasksList();
                }
            }
        });
    }

    private long getNewTaskId() {
        return Cu.maxBy(taskIdToTask.keySet(), 0L) + 1L;
    }

    private Task emptyTask(final long taskId) {
        return Task.matchFromListTask(taskId, "Задание №" + taskId, "", Collections.<String>emptyList(), Collections.<String>emptyList());
    }

    private void showTaskInForm(final Task task) {
        if (task != null) {
            taskIdLabel.setText(String.valueOf(task.getId()));
            titleTextField.setText(task.getTitle());
            descrTextArea.setText(task.getDescription());
            sourceDataTextArea.setText(Su.join(task.getSourceData(), "\n"));
            resultTextArea.setText(Su.join(task.getResultData(), "\n"));
        }
    }

    private void refillTasksList() {
        final Collection<Task> tasks = taskIdToTask.values();
        final List<String> titles = createTaskTitles(tasks);
        int i = 0;
        for (final Task task : tasks) {
            indexToTaskId.put(i, task.getId());
            i++;
        }
        tasksList.setModel(new StringsListModel(titles));
    }

    private List<String> createTaskTitles(final Collection<Task> tasks) {
        return Cu.map(tasks, new Fu<Task, String>() {
            @Override
            public String apply(final Task task) {
                return "#" + task.getId() + " - " + task.getTitle();
            }
        });
    }

    private void reloadTasks() {
        try {
            final List<Task> tasks = taskSerialiser.readTasks();
            taskIdToTask.clear();
            for (final Task task : tasks) {
                taskIdToTask.put(task.getId(), task);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Ошибка при загрузке заданий!", JOptionPane.ERROR_MESSAGE);
        }
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

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
