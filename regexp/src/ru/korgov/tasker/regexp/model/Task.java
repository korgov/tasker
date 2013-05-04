package ru.korgov.tasker.regexp.model;

import java.util.Collections;
import java.util.List;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 17.12.12
 */
public class Task {
    long id;
    TaskType type;
    String title;
    String description;
    List<String> sourceData;
    List<String> resultData;

    public Task(final long id, final TaskType type, final String title, final String description, final List<String> sourceData, final List<String> resultData) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.sourceData = sourceData;
        this.resultData = resultData;
    }

    public static Task matchFromListTask(final long id, final String title, final String description, final List<String> sourceData, final List<String> resultData){
        return new Task(id, TaskType.MATCH_FROM_LIST, title, description, sourceData, resultData);
    }

    public long getId() {
        return id;
    }

    public TaskType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getSourceData() {
        return Collections.unmodifiableList(sourceData);
    }

    public List<String> getResultData() {
        return Collections.unmodifiableList(resultData);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Task task = (Task) o;

        if (id != task.id) return false;
        if (description != null ? !description.equals(task.description) : task.description != null) return false;
        if (resultData != null ? !resultData.equals(task.resultData) : task.resultData != null) return false;
        if (sourceData != null ? !sourceData.equals(task.sourceData) : task.sourceData != null) return false;
        if (title != null ? !title.equals(task.title) : task.title != null) return false;
        if (type != task.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (sourceData != null ? sourceData.hashCode() : 0);
        result = 31 * result + (resultData != null ? resultData.hashCode() : 0);
        return result;
    }
}
