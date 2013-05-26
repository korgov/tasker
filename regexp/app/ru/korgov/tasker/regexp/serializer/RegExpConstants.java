package ru.korgov.tasker.regexp.serializer;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 15.05.13 2:59
 */
public class RegExpConstants {
    private RegExpConstants(){
    }

    public static final String REGEXP_ATTR = "regexp";

    public static final String RESULT_TASK_ID_ATTR = "task-id";
    public static final String RESULT_FAILED_INDEX_ATTR = "failed-index";
    public static final String RESULT_SHOW_RESULT_ATTR = "show-result";
    public static final String RESULT_IS_CORRECT_ATTR = "is-correct";

    private static final String TASKS_JSON_ARRAY_KEY = "tasks";

    public static final String TASK_ID_ATTR = "id";
    public static final String TASK_TITLE_ATTR = "title";
    public static final String TASK_DESCR_ATTR = "descr";
    public static final String TASK_TYPE_ATTR = "type";
    public static final String TASK_SOURCE_ATTR = "source";
    public static final String TASK_RESULT_ATTR = "result";
}
