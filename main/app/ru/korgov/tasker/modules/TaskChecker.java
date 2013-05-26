package ru.korgov.tasker.modules;

import org.json.JSONObject;
import ru.korgov.tasker.core.tasks.model.Task;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 15.05.13 2:45
 */
public interface TaskChecker {
    JSONObject checkTask(final Task task, final JSONObject params) throws Exception;
}
