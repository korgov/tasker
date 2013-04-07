package ru.korgov.tasker.core.app;

import play.Application;
import play.GlobalSettings;
import ru.korgov.tasker.core.app.spring.SpringContext;

/**
 * Author: Kirill Korgov (kirill@korgov.ru))
 * Date: 4/7/13 4:31 AM
 */
@SuppressWarnings("ClassWithoutPackageStatement")
public class Global extends GlobalSettings {
    @Override
    public void onStart(final Application application) {
        super.onStart(application);
        SpringContext.init(application);
    }
}
