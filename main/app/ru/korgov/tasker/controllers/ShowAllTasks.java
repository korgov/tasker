package ru.korgov.tasker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import play.mvc.Controller;
import play.mvc.Result;
import ru.korgov.tasker.core.app.spring.SpringController;
import ru.korgov.tasker.core.tasks.TasksService;
import views.html.tasksList;

/**
 * Author: Kirill Korgov (kirill@korgov.ru))
 * Date: 4/7/13 6:32 AM
 */
public class ShowAllTasks extends Controller implements SpringController {

    @Autowired
    private TasksService tasksService;

    @Override
    public Result process() {
        return ok(tasksList.render(tasksService.loadAllTasks()));
    }
}
