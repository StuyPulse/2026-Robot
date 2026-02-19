package com.stuypulse.robot.commands.handoff;

import com.stuypulse.robot.subsystems.handoff.Handoff;
import com.stuypulse.robot.subsystems.handoff.Handoff.HandoffState;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetHandoffState extends InstantCommand{
    private final Handoff handoff;
    private HandoffState state;

    public SetHandoffState(HandoffState state) {
        this.handoff = Handoff.getInstance();
        this.state = state;
        addRequirements(handoff);
    }

    @Override
    public void initialize() {
        handoff.setState(state);
    }
}



