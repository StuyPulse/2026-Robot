package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.intake.Intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class IntakeSetDigesting extends InstantCommand{
    public IntakeSetDigesting(boolean digesting) {
        super(() -> Intake.getInstance().setIntakeDigesting(digesting));
    }
}
