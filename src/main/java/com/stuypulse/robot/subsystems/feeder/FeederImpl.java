package com.stuypulse.robot.subsystems.feeder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    private final VelocityVoltage leadMotorController;
    private final Follower follower;

    public FeederImpl() {
        leadMotor = new TalonFX(Ports.Feeder.FEEDER_LEADER);
        Motors.Feeder.motorConfig.configure(leadMotor);

        followerMotor = new TalonFX(Ports.Feeder.FEEDER_FOLLOWER);
        Motors.Feeder.motorConfig.configure(followerMotor);

        leadMotorController = new VelocityVoltage(0.0);
        follower = new Follower(Ports.Feeder.FEEDER_LEADER, MotorAlignmentValue.Aligned);
    }

    /**
     * @return lead motor RPM
     */
    public double getCurrentLeadMotorRPM() {
        return leadMotor.getVelocity().getValueAsDouble() * Settings.Feeder.SECONDS_IN_A_MINUTE;
    }

    /**
     * @return follower motor RPM
     */
    public double getCurrentFollowerMotorRPM() {
        return followerMotor.getVelocity().getValueAsDouble() * Settings.Feeder.SECONDS_IN_A_MINUTE;
    }

    @Override
    public void periodic() {
        super.periodic();

        if (!Settings.EnabledSubsystems.FEEDER.get()) {
            leadMotor.stopMotor();
        } else {
            leadMotor.setControl(leadMotorController.withVelocity(getTargetRPM() / Settings.Feeder.SECONDS_IN_A_MINUTE));
        }

        if (Settings.DEBUG_MODE) {
            SmartDashboard.putNumber("Feeder/Lead Motor Speed", getCurrentLeadMotorRPM());
            SmartDashboard.putNumber("Feeder/Follower Motor RPM", getCurrentFollowerMotorRPM());
        }
    }
}