package com.stuypulse.robot.subsystems.spindexer;

import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;

public class SpindexerImpl extends Spindexer {

    private TalonFX leadMotor;
    private TalonFX follower;

    public SpindexerImpl() {
        super();
        leadMotor = new TalonFX(Ports.Spindexer.SPINDEXER_1);
        follower = new TalonFX(Ports.Spindexer.SPINDEXER_2);

        Motors.Spindexer.MOTOR_CONFIG.configure(leadMotor);
        Motors.Spindexer.MOTOR_CONFIG.configure(follower);
    }

    

    @Override
    public void periodic(){
        super.periodic();

        if(getTargetVoltage() == 0){
            leadMotor.setVoltage(0);
            follower.setVoltage(0);
        } else{
            leadMotor.setControl(VelocityVoltage(getTargetVoltage()));
            
        }
        
    }
}
