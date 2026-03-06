package com.stuypulse.robot.commands.handoff;

import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;

public class HandoffConditionalCommand extends ConditionalCommand {
    public HandoffConditionalCommand() {
        super(new HandoffStop(), new HandoffRun(), () -> (CommandSwerveDrivetrain.getInstance().isBehindTower() || CommandSwerveDrivetrain.getInstance().isBehindHub()));
        // DEBUG
        SmartDashboard.putBoolean("FieldPositions/Should Handoff Stop", (CommandSwerveDrivetrain.getInstance().isBehindTower() || CommandSwerveDrivetrain.getInstance().isBehindHub()));
    }
}
