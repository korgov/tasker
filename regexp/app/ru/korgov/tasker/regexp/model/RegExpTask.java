package ru.korgov.tasker.regexp.model;

import ru.korgov.util.alias.Cf;

import java.util.Collections;
import java.util.List;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 17.12.12
 */
public class RegExpTask {
    long id;
    RegExpTaskType type;
    String title;
    String description;
    List<String> sourceData;
    List<String> resultData;

    public static final RegExpTask EMPTY = new RegExpTask(-1, RegExpTaskType.MATCH_FROM_LIST, "", "", Cf.<String>emptyL(), Cf.<String>emptyL());

    public RegExpTask(final long id, final RegExpTaskType type, final String title, final String description, final List<String> sourceData, final List<String> resultData) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.sourceData = sourceData;
        this.resultData = resultData;
    }

    public static RegExpTask matchFromListTask(final long id, final String title, final String description, final List<String> sourceData, final List<String> resultData){
        return new RegExpTask(id, RegExpTaskType.MATCH_FROM_LIST, title, description, sourceData, resultData);
    }

    public long getId() {
        return id;
    }

    public RegExpTaskType getType() {
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

        final RegExpTask task = (RegExpTask) o;

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

    @Override
    public String toString() {
        return "RegExpTask{" +
                "id=" + id +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", sourceData=" + sourceData +
                ", resultData=" + resultData +
                '}';
    }
}
