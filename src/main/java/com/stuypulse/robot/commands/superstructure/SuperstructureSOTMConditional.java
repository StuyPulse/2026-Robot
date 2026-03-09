package com.stuypulse.robot.commands.superstructure;

import com.stuypulse.robot.subsystems.superstructure.Superstructure;
import com.stuypulse.robot.subsystems.superstructure.Superstructure.SuperstructureState;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;

public class SuperstructureSOTMConditional extends ConditionalCommand {
    public SuperstructureSOTMConditional() {
        super(
            new SuperstructureInterpolation(), 
            new SuperstructureSOTM(), 
            () -> Superstructure.getInstance().getState() == SuperstructureState.SOTM
        );
    }
}
