package ru.korgov.tasker.regexp.serializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.korgov.tasker.regexp.model.RegExpTask;
import ru.korgov.tasker.regexp.model.RegExpTaskType;
import ru.korgov.tasker.regexp.model.RegExpTestResult;
import ru.korgov.util.JSONUtils;
import ru.korgov.util.alias.Cu;
import ru.korgov.util.func.Function;

import java.util.List;

import static ru.korgov.tasker.regexp.serializer.RegExpConstants.*;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 24.12.12
 */
public class RegExpJSONTaskSerialiser implements RegExpTaskSerialiser {

    public RegExpJSONTaskSerialiser() {
    }

    @Override
    public RegExpTask jsonToTask(final JSONObject task) {
        try {
            final long id = task.getLong(TASK_ID_ATTR);
            final String title = task.getString(TASK_TITLE_ATTR);
            final String descr = task.getString(TASK_DESCR_ATTR);
            final RegExpTaskType type = RegExpTaskType.valueOf(task.getString(TASK_TYPE_ATTR));
            final List<String> sourceData = JSONUtils.asStringList(task.optJSONArray(TASK_SOURCE_ATTR));
            final List<String> resultData = JSONUtils.asStringList(task.optJSONArray(TASK_RESULT_ATTR));
            return new RegExpTask(id, type, title, descr, sourceData, resultData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RegExpTask> jsonToTasks(final List<JSONObject> tasks) {
        return Cu.map(new Function<JSONObject, RegExpTask>() {
            @Override
            public RegExpTask apply(final JSONObject task) {
                    return jsonToTask(task);
            }
        }, tasks);
    }

    @Override
    public JSONObject taskToJson(final RegExpTask task) {
        try {
            final JSONObject out = new JSONObject();
            out.put(TASK_ID_ATTR, task.getId());
            out.put(TASK_TITLE_ATTR, task.getTitle());
            out.put(TASK_DESCR_ATTR, task.getDescription());
            out.put(TASK_TYPE_ATTR, task.getType().name());
            out.put(TASK_SOURCE_ATTR, new JSONArray(task.getSourceData()));
            out.put(TASK_RESULT_ATTR, new JSONArray(task.getResultData()));
            return out;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RegExpTestResult jsonToResult(final JSONObject result) {
        try {
            final long taskId = result.getLong(RESULT_TASK_ID_ATTR);
            final List<String> resultToShow = JSONUtils.asStringList(result.optJSONArray(RESULT_SHOW_RESULT_ATTR));
            final boolean isCorrect = result.getBoolean(RESULT_IS_CORRECT_ATTR);
            final int firstIncorrectIndex = result.getInt(RESULT_FAILED_INDEX_ATTR);
            return new RegExpTestResult(taskId, resultToShow, firstIncorrectIndex, isCorrect);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject resultToJson(final RegExpTestResult result) {
        try {
            final JSONObject out = new JSONObject();
            out.put(RESULT_TASK_ID_ATTR, result.getTaskId());
            out.put(RESULT_SHOW_RESULT_ATTR, new JSONArray(result.getResultToShow()));
            out.put(RESULT_IS_CORRECT_ATTR, result.isCorrect());
            out.put(RESULT_FAILED_INDEX_ATTR, result.getFirstIncorrectIndex());
            return out;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<JSONObject> tasksToJson(final List<RegExpTask> regExpTasks) {
        return Cu.map(new Function<RegExpTask, JSONObject>() {
            @Override
            public JSONObject apply(final RegExpTask task) {
                    return taskToJson(task);
            }
        }, regExpTasks);
    }
}
