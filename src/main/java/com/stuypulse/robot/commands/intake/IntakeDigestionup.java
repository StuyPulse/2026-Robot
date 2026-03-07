package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.intake.Intake.PivotState;

public class IntakeDigestionup extends IntakeSetState{
    public IntakeDigestionup(){
        super(PivotState.DIGESTION_UP);
    }
}
