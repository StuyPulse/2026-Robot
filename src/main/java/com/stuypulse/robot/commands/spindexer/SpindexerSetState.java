package com.stuypulse.robot.commands.spindexer;

import com.stuypulse.robot.subsystems.spindexer.Spindexer;
import com.stuypulse.robot.subsystems.spindexer.Spindexer.SpindexerState;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SpindexerSetState extends InstantCommand {
    private final Spindexer;
    private SpindexerState state;

    public SpindexerSetState() {
        
    }
}