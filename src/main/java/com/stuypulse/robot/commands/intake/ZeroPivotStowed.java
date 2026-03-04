package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.intake.Intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ZeroPivotStowed extends InstantCommand {
    private Intake intake;

    public ZeroPivotStowed() {
        intake = Intake.getInstance();
    }

    @Override
    public void initialize() {
        intake.zeroPivotDeployed();
    }
}
