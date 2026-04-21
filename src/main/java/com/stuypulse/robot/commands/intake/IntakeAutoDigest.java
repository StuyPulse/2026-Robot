package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.intake.Intake;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class IntakeAutoDigest extends SequentialCommandGroup {

    public IntakeAutoDigest() {
        Intake.getInstance().setIntakeDigesting(true);
        addCommands(

            new IntakeDigest().andThen(new WaitCommand(0.5)).andThen(new IntakeDeploy()).andThen(new WaitCommand(0.5))

        );
        finallyDo(() -> Intake.getInstance().setIntakeDigesting(false));
    }
}
