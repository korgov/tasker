package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ru.korgov.util.alias.Cf;
import views.html.index;
import views.html.test;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("some msg"));
    }

    public static Result test() {
        return ok(test.render(Cf.list("Hello1", "Hello2eee")));
    }
  
}
