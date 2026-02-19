package com.stuypulse.robot.commands.handoff;
import com.stuypulse.robot.subsystems.handoff.Handoff.HandoffState;

public class HandoffRun extends SetHandoffState {
    public HandoffRun() {
        super(HandoffState.FORWARD);
    }
}