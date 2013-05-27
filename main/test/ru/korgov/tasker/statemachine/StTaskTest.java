package ru.korgov.tasker.statemachine;

import org.json.JSONObject;
import org.junit.Test;
import ru.korgov.tasker.statemachine.model.StTask;
import ru.korgov.tasker.statemachine.model.StTaskType;
import ru.korgov.tasker.statemachine.model.StTrigger;
import ru.korgov.tasker.statemachine.model.StateMachineConfiguration;
import ru.korgov.util.alias.Cf;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 27.05.13 4:20
 */
public class StTaskTest {
    @Test
    public void testJson() throws Exception {

        final StTask stTask = new StTask(15L, "Task Title", "Task Description, Hello world!", StTaskType.WRITE_STATE,
                new StateMachineConfiguration("Init",
                        Cf.list(
                                new StTrigger("Init", "a", "Middle1"),
                                new StTrigger("Init", "b", "Middle2"),
                                new StTrigger("Middle1", "c", "Finite1"),
                                new StTrigger("Middle1", "x", "Init"),
                                new StTrigger("Middle2", "c", "Finite2"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init"),
                                new StTrigger("Middle2", "x", "Init")
                        ),
                        Cf.set("Finite1", "Finite2")
                )
        );

        final String serializedTask = stTask.asJson().toString();

        System.out.println("Serialized: " + serializedTask);

        final StTask stTaskAfterConverting = StTask.fromJson(new JSONObject(serializedTask));

        System.out.println("Converted: " + stTaskAfterConverting);
    }
}
