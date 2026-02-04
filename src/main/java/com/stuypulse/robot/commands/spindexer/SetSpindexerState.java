package com.stuypulse.robot.commands.spindexer;

import com.stuypulse.robot.subsystems.spindexer.Spindexer;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetSpindexerState extends InstantCommand {
    private final Spindexer spindexer;
    private Spindexer.SpindexerState state;

    public SetSpindexerState(Spindexer.SpindexerState state) {
        this.spindexer = Spindexer.getInstance();
        this.state = state;
        addRequirements(spindexer);
    }

    @Override
    public void initialize() {
        spindexer.setSpindexerState(state);
    }
}