package ru.korgov.tasker.regexp.serializer;

import ru.korgov.tasker.regexp.model.Task;

import java.io.IOException;
import java.util.List;

/**
 * Author: Kirill Korgov (kirill@korgov.ru)
 * Date: 24.12.12
 */
public interface TaskSerialiser {
    List<Task> readTasks() throws IOException;

    void saveTasks(final List<Task> tasks) throws IOException;
}

