package com.stuypulse.robot.subsystems.feeder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;

public class FeederImpl extends Feeder {

    private final TalonFX leadMotor;
    private final TalonFX followerMotor;

    protected FeederImpl() {
        super();
         leadMotor = new TalonFX(Ports.Feeder.FEEDER1);
         Motors.Feeder.motorConfig.configure(leadMotor);

         followerMotor = new TalonFX(Ports.Feeder.FEEDER2);
         Motors.Feeder.motorConfig.configure(followerMotor);

    }
    public double getCurrentLeadMotorRPM() {
        return leadMotor.getVelocity().getValueAsDouble() * Settings.Feeder.SECONDS_IN_A_MINUTE; 
    }

    public double getCurrentFollowerMotorRPM() {
        return followerMotor.getVelocity().getValueAsDouble() * Settings.Feeder.SECONDS_IN_A_MINUTE; 
    }
    

   @Override
   public void periodic(){
    super.periodic();

    if(getTargetRPM() == 0){
        leadMotor.setVoltage(0);
        followerMotor.setVoltage(0);
    }
    else{
        leadMotor.setControl(new VelocityVoltage(getTargetRPM()));
        followerMotor.setControl(new Follower(Ports.Feeder.FEEDER1,MotorAlignmentValue.Aligned));
    }
    
    SmartDashboard.putNumber("Feeder/Lead Motor Speed", getCurrentLeadMotorRPM());
    SmartDashboard.putNumber("Feeder/Follower Motor RPM", getCurrentFollowerMotorRPM());
   }
}
