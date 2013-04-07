package ru.korgov.tasker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import play.mvc.Controller;
import play.mvc.Result;
import ru.korgov.tasker.core.app.spring.SpringController;
import ru.korgov.tasker.core.users.UserService;
import ru.korgov.tasker.core.users.model.UserInfo;
import ru.korgov.util.alias.Cf;
import ru.korgov.util.alias.Cu;
import views.html.test;

/**
 * Author: Kirill Korgov (kirill@korgov.ru))
 * Date: 4/7/13 6:32 AM
 */
public class ShowAllUsers extends Controller implements SpringController {

    @Autowired
    private UserService userService;

    @Override
    public Result process() {
        return ok(test.render(Cu.join(UserInfo.TO_NAME.map(userService.loadAllUsers()), Cf.list("Hello from Spring!!!"))));
    }
}
