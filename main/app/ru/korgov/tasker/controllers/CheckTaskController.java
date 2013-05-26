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
import ru.korgov.tasker.modules.TaskChecker;
import views.html.xerror;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 15.05.13 5:20
 */
public class CheckTaskController extends Controller implements SpringController, ApplicationContextAware {
    private static final String JSON_CONTENT_TYPE = MimeTypes.forExtension("json").get();

    @Autowired
    private TasksService tasksService;

    private ApplicationContext applicationContext;

    @Override
    @BodyParser.Of(BodyParser.Json.class)
    public Result process() {
        final Http.Request request = request();
        try {
            new JSONObject();
            final JsonNode reqAsJson = request.body().asJson();
            System.out.println("text:" + reqAsJson.toString());
            final JSONObject req = new JSONObject(reqAsJson.toString());
            final long taskId = req.getLong("task-id");
            final Map<Long, TaskType> taskTypesMap = tasksService.loadTypesInfo();
            final Task task = tasksService.loadTask(taskId);
            final TaskChecker taskChecker = loadCheckerForTask(taskTypesMap, task);
            final JSONObject responseAsJson = taskChecker.checkTask(task, req);
            return ok(responseAsJson.toString().getBytes(Charset.forName("UTF-8"))).as(JSON_CONTENT_TYPE);

        } catch (Exception e) {
            e.printStackTrace();
            return ok(xerror.render(e.getMessage()));
        }
    }

    private TaskChecker loadCheckerForTask(final Map<Long, TaskType> taskTypesMap, final Task task) throws JSONException {
        final TaskType type = taskTypesMap.get(task.getTaskTypeId());
        final JSONObject solveInfo = new JSONObject(type.getSolveInfoAsJson());
        final String checkerBeanName = solveInfo.getString("checker-bean");
        return applicationContext.getBean(checkerBeanName, TaskChecker.class);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
