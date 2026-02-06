package com.stuypulse.robot.subsystems.spindexer;

import com.stuypulse.robot.constants.Gains;

import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class SpindexerSim extends Spindexer {
    private DCMotorSim leadMotor = new DCMotorSim(LinearSystemId.createDCMotorSystem(Gains.Spindexer.kV, Gains.Spindexer.kA));  

    private DCMotorSim followerMotor = new DCMotorSim();
        

    public SpindexerSim() {
        super();

    }

    @Override
    public double getRPMBasedOnDistance() {
        return 0;
    }

    @Override
    public double getTargetRPM() {
        
    }
}
