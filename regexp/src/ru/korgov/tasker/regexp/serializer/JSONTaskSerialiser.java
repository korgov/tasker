package ru.korgov.tasker.regexp.serializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.korgov.tasker.regexp.model.Task;
import ru.korgov.tasker.regexp.model.TaskType;
import ru.korgov.util.JSONUtils;
import ru.korgov.util.UrlUtils;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 24.12.12
 */
public class JSONTaskSerialiser implements TaskSerialiser {

    private static final String TASKS_JSON_ARRAY_KEY = "tasks";

    final String fileUrl;

    public JSONTaskSerialiser(final String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public List<Task> readTasks() throws IOException {
        final List<Task> out = Cf.newList();
        final String tasksAsJsonString = UrlUtils.read(new URL(fileUrl).openConnection());
        try {
            final JSONObject tasksObject = new JSONObject(tasksAsJsonString);
            final JSONArray tasksArray = tasksObject.getJSONArray(TASKS_JSON_ARRAY_KEY);
            for (final JSONObject task : JSONUtils.asJSONObjectList(tasksArray)) {
                out.add(jsonToTask(task));
            }
        } catch (JSONException e) {
            throw new IOException(e);
        }
        return out;
    }

    public void saveTasks(final List<Task> tasks) throws IOException {
        final JSONObject toSave = new JSONObject();
        for (final Task task : tasks) {
            try {
                toSave.append(TASKS_JSON_ARRAY_KEY, taskToJson(task));
            } catch (JSONException e) {
                throw new IllegalArgumentException(e);
            }
        }
        IOUtils.writeFile(fileUrl, toSave.toString());
    }

    private Task jsonToTask(final JSONObject task) throws JSONException {
        final long id = task.getLong("id");
        final String title = task.getString("title");
        final String descr = task.getString("descr");
        final TaskType type = TaskType.valueOf(task.getString("type"));
        final List<String> sourceData = JSONUtils.asStringList(task.optJSONArray("source"));
        final List<String> resultData = JSONUtils.asStringList(task.optJSONArray("result"));
        return new Task(id, type, title, descr, sourceData, resultData);
    }

    private JSONObject taskToJson(final Task task) throws JSONException {
        final JSONObject out = new JSONObject();
            out.put("id", task.getId());
            out.put("title", task.getTitle());
            out.put("descr", task.getDescription());
            out.put("type", task.getType().name());
            out.put("source", new JSONArray(task.getSourceData()));
            out.put("result", new JSONArray(task.getResultData()));
        return out;
    }
}
