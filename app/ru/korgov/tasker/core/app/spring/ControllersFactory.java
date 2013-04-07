package ru.korgov.tasker.core.app.spring;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Author: Kirill Korgov (kirill@korgov.ru))
 * Date: 4/7/13 6:21 AM
 */
public class ControllersFactory extends Controller {

    public static Result fromContext(final String name) {
        return Spring.getBean(name, SpringController.class).process();
    }
}
