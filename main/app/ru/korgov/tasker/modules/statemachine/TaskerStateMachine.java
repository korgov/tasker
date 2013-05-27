package ru.korgov.tasker.modules.statemachine;

import com.googlecode.stateless4j.StateMachine;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 27.05.13 3:18
 */
public class TaskerStateMachine extends StateMachine<String, String> {
    public TaskerStateMachine(final String initialState) {
        super(initialState);
    }
}
