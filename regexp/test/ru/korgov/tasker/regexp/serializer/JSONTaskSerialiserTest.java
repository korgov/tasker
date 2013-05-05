package ru.korgov.tasker.regexp.serializer;

/**
 * Author: Kirill Korgov (korgov@yandex-team.ru)
 * Date: 05.05.13 2:35
 */
public class JSONTaskSerialiserTest {


    @org.junit.Test
    public void testXXX() throws Exception {

        final JSONTaskSerialiser jsonTaskSerialiser = new JSONTaskSerialiser("http://localhost:9000/assets/jars/tasks.tsk");
        System.out.println(jsonTaskSerialiser.readTasks());

    }
}
