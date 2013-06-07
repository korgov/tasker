package ru.korgov.tasker.modules;

import org.json.JSONObject;
import ru.korgov.tasker.core.tasks.model.Task;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 30.05.13 3:38
 */


public interface TaskHelper<E> {
    E forTask(final Task task, final JSONObject params) throws Exception;
}
