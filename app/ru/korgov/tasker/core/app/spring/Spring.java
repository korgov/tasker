package ru.korgov.tasker.core.app.spring;

import play.Play;
import ru.korgov.util.alias.Cu;

import java.util.Map;

/**
 * Author: Kirill Korgov (kirill@korgov.ru))
 * Date: 4/7/13 5:07 AM
 */
public class Spring {

    private Spring() {
    }

    public static Object getBean(final String name) {
        return SpringContext.getContext().getBean(name);
    }

    public static <T> T getBean(final String name, final Class<T> clazz) {
        return SpringContext.getContext().getBean(name, clazz);
    }

    public static <T> T getBeanOfType(final Class<T> type) {
        return Cu.firstOrNull(getBeansOfType(type).values());
    }

    public static Object getBeanOfType(final String type) {
        try {
            return getBeanOfType(Play.application().classloader().loadClass(type));
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> Map<String, T> getBeansOfType(final Class<T> type) {
        return SpringContext.getContext().getBeansOfType(type);
    }

}
