package ru.korgov.tasker.regexp.serializer;

import org.json.JSONObject;
import ru.korgov.tasker.regexp.model.RegExpTask;
import ru.korgov.tasker.regexp.model.RegExpTestResult;

import java.util.List;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 24.12.12
 */
public interface RegExpTaskSerialiser {
    RegExpTask jsonToTask(final JSONObject task);
    List<RegExpTask> jsonToTasks(final List<JSONObject> tasks);

    JSONObject taskToJson(RegExpTask task);

    RegExpTestResult jsonToResult(final JSONObject result);

    JSONObject resultToJson(final RegExpTestResult result);

    List<JSONObject> tasksToJson(List<RegExpTask> regExpTasks);
}

