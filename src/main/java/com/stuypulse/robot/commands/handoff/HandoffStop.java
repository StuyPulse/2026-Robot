package com.stuypulse.robot.commands.handoff;
import com.stuypulse.robot.subsystems.handoff.Handoff.HandoffState;

public class HandoffStop extends SetHandoffState {
    public HandoffStop() {
        super(HandoffState.STOP);
    }
}