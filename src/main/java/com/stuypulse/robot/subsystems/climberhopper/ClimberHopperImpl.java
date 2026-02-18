package com.stuypulse.robot.subsystems.climberhopper;
import java.util.Optional;

import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.math.SLMath;
import com.stuypulse.stuylib.streams.booleans.BStream;
import com.stuypulse.stuylib.streams.booleans.filters.BDebounce;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Motors;


public class ClimberHopperImpl extends ClimberHopper {
    private final TalonFX motor;
    private final BStream stalling;
    private double voltage;

    public ClimberHopperImpl() {
        super();
        motor = new TalonFX(Ports.ClimberHopper.CLIMBER_HOPPER);
        Motors.ClimberHopper.climberHopperMotor.configure(motor);
        motor.setPosition(Constants.ClimberHopper.MIN_HEIGHT_METERS);
        
        stalling = BStream.create(() -> motor.getStatorCurrent().getValueAsDouble() > Settings.ClimberHopper.STALL)
            .filtered(new BDebounce.Both(Settings.ClimberHopper.DEBOUNCE));
    }

    // TODO: Write SysID Routines

    @Override
    public double getCurrentHeight() {
        return this.motor.getPosition().getValueAsDouble() * Constants.ClimberHopper.Encoders.POSITION_CONVERSION_FACTOR;
    }

    private double getTargetHeight() {
        return SLMath.clamp(getState().getTargetHeight(), Constants.ClimberHopper.MIN_HEIGHT_METERS, Constants.ClimberHopper.MAX_HEIGHT_METERS);
    }

     private boolean isWithinTolerance(double toleranceMeters) {
        return Math.abs(getTargetHeight() - getCurrentHeight()) < toleranceMeters;
    }

    @Override
    public boolean atTargetHeight() {
        return isWithinTolerance(Settings.ClimberHopper.HEIGHT_TOLERANCE_METERS);
    }

    @Override 
    public boolean getStalling() {
        return stalling.getAsBoolean();
    }

    @Override
    public void periodic() {
        

        motor.setVoltage(voltage);
        SmartDashboard.putNumber("ClimberHopper/Voltage", voltage);
        SmartDashboard.putNumber("ClimberHopper/Current", motor.getSupplyCurrent().getValueAsDouble());
        SmartDashboard.putBoolean("ClimberHopper/Stalling", getStalling());

    }
}