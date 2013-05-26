package ru.korgov.tasker.regexp.viewers;

import org.json.JSONException;
import org.json.JSONObject;
import ru.korgov.tasker.regexp.StringsListModel;
import ru.korgov.tasker.regexp.model.RegExpTask;
import ru.korgov.tasker.regexp.model.RegExpTaskType;
import ru.korgov.tasker.regexp.model.RegExpTestResult;
import ru.korgov.tasker.regexp.serializer.RegExpJSONTaskSerialiser;
import ru.korgov.tasker.regexp.serializer.RegExpTaskSerialiser;
import ru.korgov.util.UrlUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 16.12.12
 */
public class StringListTask {
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
    private static final Color DARK_GREEN = new Color(0, 102, 51);
    private static final Color DARK_RED = new Color(117, 6, 16);
    private static final String JSON_CONTENT_TYPE = "application/json";

    private final String hostUrl;
    private JFrame frame;

    private JPanel mainPanel;
    private JPanel taskInfoPanel;
    private JLabel taskDescription;

    private JPanel matchFromListPanel;
    private JList sourceList;
    private JList resultList;

    private JTextField answerTextField;

    private JButton testAnswerButton;
    private JButton saveAnswerButton;

    private JLabel answerResultLabel;

    private RegExpTask currentTask = null;

    private RegExpTaskSerialiser taskSerialiser;

    public StringListTask(final JFrame frame, final String hostname, final JSONObject taskInfoAsJson) {
        this.frame = frame;
        this.hostUrl = hostname;
        this.taskSerialiser = new RegExpJSONTaskSerialiser();

        final RegExpTask task = readTask(taskInfoAsJson);

        initFrame(task);
        final Action testAnswerAction = testAnswerAction();
        testAnswerButton.setAction(testAnswerAction);
        answerTextField.setAction(testAnswerAction);
    }

    private RegExpTask readTask(final JSONObject taskInfoAsJson) {
        try {
            return taskSerialiser.jsonToTask(taskInfoAsJson);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Ошибка при загрузке задания!", JOptionPane.ERROR_MESSAGE);
        }
        return RegExpTask.EMPTY;
    }

    private List<RegExpTask> loadTasks() {
//        try {
//            return taskSerialiser.readTasks();
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(frame, e.getMessage(), "Ошибка при загрузке заданий!", JOptionPane.ERROR_MESSAGE);
//        }
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
                final String regExp = answerTextField.getText();
                final RegExpTestResult result = testAnswer(regExp);
                if (result.isCorrect()) {
                    answerResultLabel.setText("Правильно!");
                    answerResultLabel.setForeground(DARK_GREEN);
                } else {
                    answerResultLabel.setText("Не правильно!");
                    answerResultLabel.setForeground(DARK_RED);
                }

                final int firstIncorrectIndex = result.getFirstIncorrectIndex();
                final List<String> resultToShow = result.getResultToShow();
                resultList.setModel(new StringsListModel(resultToShow));
                if (firstIncorrectIndex < resultToShow.size()) {
                    resultList.setSelectionInterval(firstIncorrectIndex, resultToShow.size() - 1);
                }

                answerResultLabel.setVisible(true);
            }
        };
    }

    private RegExpTestResult testAnswer(final String regExp) {
        final long taskId = currentTask.getId();
        try {
            final JSONObject json = new JSONObject();
            json.put("task-id", taskId);
            json.put("regexp", regExp);
            final String testResultAsJson = UrlUtils.sendPostRequest("http://" + hostUrl + "/checkTask", json.toString(), JSON_CONTENT_TYPE);
            final JSONObject resultAsJson = new JSONObject(testResultAsJson);
            return taskSerialiser.jsonToResult(resultAsJson);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Ошибка при отправке на сервер!", JOptionPane.ERROR_MESSAGE);
        }
        return RegExpTestResult.EMPTY;
    }

    private void initFrame(final RegExpTask task) {
        taskInfoPanel.setBorder(EMPTY_BORDER);
        matchFromListPanel.setVisible(false);
        currentTask = task;
        showTask();
    }

    private void showTask() {
        if (currentTask != null) {
            taskInfoPanel.setBorder(BorderFactory.createTitledBorder(currentTask.getTitle()));
            taskDescription.setText(wrapInHtml(currentTask.getDescription()));

            final RegExpTaskType type = currentTask.getType();
            final boolean isMatchFromListType = RegExpTaskType.MATCH_FROM_LIST == type;
            matchFromListPanel.setVisible(isMatchFromListType);
            if (isMatchFromListType) {
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
