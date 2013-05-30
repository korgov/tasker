package ru.korgov.tasker.controllers;

import org.codehaus.jackson.JsonNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import play.api.libs.MimeTypes;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import ru.korgov.tasker.core.app.spring.SpringController;
import ru.korgov.tasker.core.tasks.TasksService;
import ru.korgov.tasker.core.tasks.model.Task;
import ru.korgov.tasker.core.tasks.model.TaskType;
import ru.korgov.tasker.modules.BinaryTaskHelper;
import views.html.xerror;

import java.util.Map;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 15.05.13 5:20
 */
public class BinaryHelpTaskController extends Controller implements SpringController, ApplicationContextAware {
    private static final String JSON_CONTENT_TYPE = MimeTypes.forExtension("json").get();

    @Autowired
    private TasksService tasksService;

    private ApplicationContext applicationContext;

    @Override
    @BodyParser.Of(BodyParser.Json.class)
    public Result process() {
        final Http.Request request = request();
        try {
            final JsonNode reqAsJson = request.body().asJson();
            System.out.println("text:" + reqAsJson.toString());
            final JSONObject req = new JSONObject(reqAsJson.toString());
            final long taskId = req.getLong("task-id");
            final Map<Long, TaskType> taskTypesMap = tasksService.loadTypesInfo();
            final Task task = tasksService.loadTask(taskId);
            final BinaryTaskHelper taskChecker = loadHelperForTask(taskTypesMap, task);
            return ok(taskChecker.forTask(task, req));
        } catch (Exception e) {
            e.printStackTrace();
            return ok(xerror.render(e.getMessage()));
        }
    }

    private BinaryTaskHelper loadHelperForTask(final Map<Long, TaskType> taskTypesMap, final Task task) throws JSONException {
        final TaskType type = taskTypesMap.get(task.getTaskTypeId());
        final JSONObject viewInfo = new JSONObject(type.getViewInfoAsJson());
        final String helperBeanName = viewInfo.getString("bin-helper-bean");
        return applicationContext.getBean(helperBeanName, BinaryTaskHelper.class);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
