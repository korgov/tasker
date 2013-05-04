package ru.korgov.tasker.regexp;

import ru.korgov.tasker.regexp.model.Task;
import ru.korgov.tasker.regexp.model.TaskType;
import ru.korgov.tasker.regexp.serializer.JSONTaskSerialiser;
import ru.korgov.tasker.regexp.serializer.TaskSerialiser;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Cu;
import ru.korgov.util.alias.Fu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 16.12.12
 */
public class TaskClient {
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
    private static final Color DARK_GREEN = new Color(0, 102, 51);
    private static final Color DARK_RED = new Color(117, 6, 16);
    private JFrame frame;

    private JPanel mainPanel;
    private JPanel taskInfoPanel;
    private JLabel taskDescription;

    private JPanel matchFromListPanel;
    private JList sourceList;
    private JList resultList;

    private JTextField answerTextField;

    private JButton loadTasksButton;
    private JButton prevTaskButton;
    private JButton testAnswerButton;
    private JButton nextTaskButton;

    private JLabel answerResultLabel;

    private final List<Task> loadedTasks = new ArrayList<Task>();
    private ListIterator<Task> currentTaskIter = loadedTasks.listIterator();
    private Task currentTask = null;

    private TaskSerialiser taskSerialiser;

    public TaskClient(final JFrame frame, final String tasksPath) {
        this.frame = frame;
        this.taskSerialiser = new JSONTaskSerialiser(tasksPath);

        initFrame();

        loadTasksButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                loadedTasks.clear();
                loadedTasks.addAll(loadTasks());
                currentTask = null;
                currentTaskIter = loadedTasks.listIterator();
                nextTaskButton.setEnabled(currentTaskIter.hasNext());
                showNextIfHas();
                prevTaskButton.setEnabled(false);
            }
        });
        nextTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                showNextIfHas();
            }
        });
        prevTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                showPrevIfHas();
            }
        });
        final Action testAnswerAction = testAnswerAction();
        testAnswerButton.setAction(testAnswerAction);
        answerTextField.setAction(testAnswerAction);
    }

    private List<Task> loadTasks() {
        try {
            return taskSerialiser.readTasks();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Ошибка при загрузке заданий!", JOptionPane.ERROR_MESSAGE);
        }
        return Collections.emptyList();
//        return Arrays.asList(
//                Task.matchFromListTask(1L, "Задание №1",
//                        "Напиши регулярное выражение для выборки из заданных строк номеров телефонов с кодом города.",
//                        Arrays.asList("+7 921 9285726", "123-45-67", "8(921)9285726", "+7 (921) 928-5726", "333-278"),
//                        Arrays.asList("+7 921 9285726", "8(921)9285726", "+7 (921) 928-5726")
//                ),
//                Task.matchFromListTask(2L, "Задание №2",
//                        "Напиши регулярное выражение для поиска всех адресов электронной почты в заданных строках",
//                        Arrays.asList("e-mail: kirill@korgov.ru, korgov@yandex.ru", "phone: +79219285726", "url: www.korgov.ru", "Коргов Кирилл (korgovk@gmail.com)"),
//                        Arrays.asList("kirill@korgov.ru", "korgov@yandex.ru", "korgovk@gmail.com")
//                )
//        );
    }

    @SuppressWarnings({"CloneableClassWithoutClone", "CloneableClassInSecureContext", "SerializableHasSerializationMethods"})
    private AbstractAction testAnswerAction() {
        return new AbstractAction("Проверить") {
            public void actionPerformed(final ActionEvent e) {
                if (currentTask != null) {
                    final List<String> resultsByMatch = new ArrayList<String>();
                    final List<String> sourceData = currentTask.getSourceData();
                    final List<String> validResultData = currentTask.getResultData();
                    final String regExp = answerTextField.getText();
                    try {
                        final Pattern pattern = Pattern.compile(regExp);
                        for (final String src : sourceData) {
                            resultsByMatch.addAll(matchStrings(src, pattern));
                        }
                    } catch (Exception ignored) {
                    }

                    final List<String> resultToShow = Cf.newList(resultsByMatch);
                    int firstIncorrectIndex = resultToShow.size();
                    if (set(validResultData).equals(set(resultsByMatch))) {
                        answerResultLabel.setText("Правильно!");
                        answerResultLabel.setForeground(DARK_GREEN);
                    } else {
                        answerResultLabel.setText("Не правильно!");
                        answerResultLabel.setForeground(DARK_RED);

                        final Set<String> invalidMissing = Cu.minus(validResultData, resultsByMatch);
                        final Set<String> invalidIncorrect = Cu.minus(resultsByMatch, validResultData);
                        resultToShow.removeAll(invalidIncorrect);
                        firstIncorrectIndex = resultToShow.size();
                        resultToShow.addAll(Cu.map(invalidMissing, prefixFu("[-] ")));
                        resultToShow.addAll(Cu.map(invalidIncorrect, prefixFu("[+] ")));
                    }

                    resultList.setModel(new StringsListModel(resultToShow));
                    if (firstIncorrectIndex < resultToShow.size()) {
                        resultList.setSelectionInterval(firstIncorrectIndex, resultToShow.size() - 1);
                    }

                    answerResultLabel.setVisible(true);
                }
            }
        };
    }

    private Fu<String, String> prefixFu(final String prefix) {
        return new Fu<String, String>() {
            @Override
            public String apply(final String str) {
                return prefix + str;
            }
        };
    }

    private Set<String> set(final Collection<String> c) {
        return new HashSet<String>(c);
    }

    private List<String> matchStrings(final String src, final Pattern pattern) {
        final List<String> out = new ArrayList<String>();
        final Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            out.add(matcher.group());
        }
        return out;
    }

    private void initFrame() {
        taskInfoPanel.setBorder(EMPTY_BORDER);
        taskDescription.setText(wrapInHtml("Для загрузки заданий, нажмите кнопку \"" + loadTasksButton.getText() + "\"."));
        matchFromListPanel.setVisible(false);
        nextTaskButton.setEnabled(false);
        prevTaskButton.setEnabled(false);
    }

    private void showNextIfHas() {
        if (currentTaskIter.hasNext()) {
            final Task next = currentTaskIter.next();
            currentTask = next.equals(currentTask) ? currentTaskIter.next() : next;

            if (!currentTaskIter.hasNext()) {
                currentTaskIter.previous();
                nextTaskButton.setEnabled(false);
            }
            showTask();
            prevTaskButton.setEnabled(true);
        }
    }


    private void showPrevIfHas() {
        if (currentTaskIter.hasPrevious()) {
            final Task previous = currentTaskIter.previous();
            currentTask = previous.equals(currentTask) ? currentTaskIter.previous() : previous;
            if (!currentTaskIter.hasPrevious()) {
                currentTaskIter.next();
                prevTaskButton.setEnabled(false);
            }
            showTask();
            nextTaskButton.setEnabled(true);
        }
    }

    private void showTask() {
        if (currentTask != null) {
            taskInfoPanel.setBorder(BorderFactory.createTitledBorder(currentTask.getTitle()));
            taskDescription.setText(wrapInHtml(currentTask.getDescription()));
//        answerTextField.setText("");

            final TaskType type = currentTask.getType();
            matchFromListPanel.setVisible(TaskType.MATCH_FROM_LIST == type);
            if (type == TaskType.MATCH_FROM_LIST) {
                sourceList.setModel(new StringsListModel(currentTask.getSourceData()));
                resultList.setModel(new StringsListModel(currentTask.getResultData()));
            }
            answerResultLabel.setVisible(false);
        }
    }

    private String wrapInHtml(final String str) {
        return "<html>" + str + "</html>";
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
