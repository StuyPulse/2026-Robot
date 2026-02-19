package com.stuypulse.robot.subsystems.climberhopper;
import java.util.Optional;

import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.streams.booleans.BStream;
import com.stuypulse.stuylib.streams.booleans.filters.BDebounce;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.stuypulse.robot.constants.Constants;
//import com.stuypulse.stuylib.streams.booleans.filters.BFilter;
import com.stuypulse.robot.constants.Motors;


public class ClimberHopperImpl extends ClimberHopper {
    private final TalonFX motor;
    private final BStream stalling;
    private double voltage;

    private Optional<Double> voltageOverride;

    public ClimberHopperImpl() {
        super();
        motor = new TalonFX(Ports.ClimberHopper.CLIMBER_HOPPER);
        Motors.ClimberHopper.climberHopperMotor.configure(motor);
        motor.setPosition(0); // hopper all the way down according to le henry
        stalling = BStream.create(() -> motor.getStatorCurrent().getValueAsDouble() > Settings.ClimberHopper.STALL)
            .filtered(new BDebounce.Both(Settings.ClimberHopper.DEBOUNCE));
    }

    @Override 
    public boolean getStalling() {
        return stalling.getAsBoolean();
    }

    @Override
    public double getCurrentHeight() { // TODO: convert motor encoder position to meters somehow
        return this.motor.getPosition().getValueAsDouble() * Constants.ClimberHopper.Encoders.POSITION_CONVERSION_FACTOR;
    }

    private boolean isWithinTolerance(double toleranceMeters) {
        return Math.abs(getState().getTargetHeight() - getCurrentHeight()) < toleranceMeters;
    }

    @Override
    public boolean atTargetHeight() {
        return isWithinTolerance(Settings.ClimberHopper.HEIGHT_TOLERANCE_METERS);
    }

    // @Override
    // public void setVoltageOverride(Optional<Double> voltage) {
    //     this.voltageOverride = voltage;
    // }

    @Override
    public void periodic() {
        super.periodic();

        if (!atTargetHeight()) {
            if (getCurrentHeight() < getState().getTargetHeight()) {
                voltage = Settings.ClimberHopper.MOTOR_VOLTAGE;
            } else {
                voltage = - Settings.ClimberHopper.MOTOR_VOLTAGE;
            }
        } else {
            voltage = 0;
        }

        // TODO: Figure out some way to reset the encoder reading when stall
        // if (atTargetHeight() && getState() == ClimberHopperState.HOPPER_DOWN) {
        //     if (voltageOverride.isPresent()) {

        //     }
        // }

        motor.setVoltage(voltage);

        SmartDashboard.putNumber("ClimberHopper/Voltage", voltage);
        SmartDashboard.putNumber("ClimberHopper/Current", motor.getSupplyCurrent().getValueAsDouble());
        SmartDashboard.putBoolean("ClimberHopper/Stalling", getStalling());
    }
}