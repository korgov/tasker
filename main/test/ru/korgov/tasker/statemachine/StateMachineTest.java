package ru.korgov.tasker.statemachine;

import com.googlecode.stateless4j.StateMachine;
import com.googlecode.stateless4j.delegates.Action;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 27.05.13 3:04
 */
public class StateMachineTest {

    @Test
    public void testDo() throws Exception {
        Action callStartTimer = new Action() {
            @Override
            public void doIt() {
                startCallTimer();
            }
        };
        Action callStopTimer = new Action() {
            @Override
            public void doIt() {
                stopCallTimer();
            }
        };
        StateMachine<State, Trigger> phoneCall = new StateMachine<State, Trigger>(State.OffHook);

        phoneCall.Configure(State.OffHook)
                .Permit(Trigger.CallDialed, State.Ringing);

        phoneCall.Configure(State.Ringing)
                .Permit(Trigger.HungUp, State.OffHook)
                .Permit(Trigger.CallConnected, State.Connected);

        phoneCall.Configure(State.Connected)
                .OnEntry(callStartTimer)
                .OnExit(callStopTimer)
                .Permit(Trigger.LeftMessage, State.OffHook)
                .Permit(Trigger.HungUp, State.OffHook)
                .Permit(Trigger.PlacedOnHold, State.OnHold);


        phoneCall.Fire(Trigger.CallDialed);
        Assert.assertEquals(State.Ringing, phoneCall.getState());

        phoneCall.Fire(Trigger.CallConnected);
        Assert.assertEquals(State.Connected, phoneCall.getState());

//        phoneCall.Fire(Trigger.HungUp);
//        Assert.assertEquals(State.OffHook, phoneCall.getState());

    }

    enum State {
        Ringing, OffHook, OnHold, Connected
    }

    enum Trigger {
        PlacedOnHold, HungUp, LeftMessage, CallConnected, CallDialed
    }

    private void stopCallTimer() {
        System.out.println("Stop called");
    }

    private void startCallTimer() {
        System.out.println("Start called");
    }
}
