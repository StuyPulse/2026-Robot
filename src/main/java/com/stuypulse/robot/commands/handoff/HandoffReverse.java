package com.stuypulse.robot.commands.handoff;

import com.stuypulse.robot.subsystems.handoff.Handoff.HandoffState;

public class HandoffReverse extends SetHandoffState {
    public HandoffReverse() {
        super(HandoffState.REVERSE);
    }
}