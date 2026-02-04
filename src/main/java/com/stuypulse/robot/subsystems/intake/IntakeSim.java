package com.stuypulse.robot.subsystems.intake;

import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj.simulation.LinearSystemSim;

public class IntakeSim extends Intake {
    private final DCMotorSim intakeMotorSimulated;

    public IntakeSim() {
        intakeMotorSimulated = new DCMotorSim(LinearSystemId.createDCMotorSystem(intakeMotorSimulated,,0)

    }
}