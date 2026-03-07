package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.intake.Intake.PivotState;

public class IntakeDigestionDown extends IntakeSetState{
    public IntakeDigestionDown() {
        super(PivotState.DIGESTION_DOWN);
    }
}
