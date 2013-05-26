package ru.korgov.tasker.regexp.model;

import ru.korgov.util.alias.Cf;

import java.util.List;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 17.12.12
 */
public class RegExpTestResult {
    public static final RegExpTestResult EMPTY = correctResult(-1L);

    private final long taskId;
    private final List<String> resultToShow;
    private final int firstIncorrectIndex;
    private final boolean isCorrect;

    public RegExpTestResult(final long taskId, final List<String> resultToShow, final int firstIncorrectIndex, final boolean correct) {
        this.taskId = taskId;
        this.resultToShow = resultToShow;
        this.firstIncorrectIndex = firstIncorrectIndex;
        isCorrect = correct;
    }

    public static RegExpTestResult correctResult(final long taskId) {
        return new RegExpTestResult(taskId, Cf.<String>emptyL(), 0, true);
    }

    public long getTaskId() {
        return taskId;
    }

    public List<String> getResultToShow() {
        return resultToShow;
    }

    public int getFirstIncorrectIndex() {
        return firstIncorrectIndex;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
