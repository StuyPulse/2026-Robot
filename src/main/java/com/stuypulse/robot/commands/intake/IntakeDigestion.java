package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.intake.Intake;
import com.stuypulse.robot.subsystems.intake.Intake.PivotState;
import com.stuypulse.stuylib.streams.booleans.BStream;
import com.stuypulse.stuylib.streams.booleans.filters.BDebounce;
import com.stuypulse.robot.commands.intake.IntakeDigestionup;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class IntakeDigestion extends RepeatCommand {
    public IntakeDigestion() {
        super(
            new SequentialCommandGroup(
                new IntakeDigestionup(), 
                new WaitUntilCommand(() -> Intake.getInstance().pivotAtTolerance()).withTimeout(0.4),
                new WaitCommand(1.0),
                new IntakeDigestionDown(),
                new WaitUntilCommand(() -> Intake.getInstance().pivotAtTolerance()).withTimeout(0.4),
                new WaitCommand(0.5)
            )
        );
    }
}
