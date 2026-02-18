package com.stuypulse.robot.subsystems.climberhopper;

import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.streams.booleans.BStream;
import com.stuypulse.stuylib.streams.booleans.filters.BDebounce;

import edu.wpi.first.math.system.plant.DCMotor;

public class ClimberHopperSim extends ClimberHopper {
    private final ElevatorSim sim;
    private final ClimberHopperVisualizer visualizer;
    private double voltage;

    public ClimberHopperSim() {
        visualizer = ClimberHopperVisualizer.getInstance();

        sim = new ElevatorSim(
            DCMotor.getKrakenX60(1),
            Constants.ClimberHopper.Encoders.GEARING,
            Constants.ClimberHopper.MASS_KG,
            Constants.ClimberHopper.DRUM_RADIUS_METERS,
            Constants.ClimberHopper.MIN_HEIGHT_METERS,
            Constants.ClimberHopper.MAX_HEIGHT_METERS,
            false,
            Constants.ClimberHopper.MIN_HEIGHT_METERS
        );
    }

    public boolean getStalling() {
        return sim.getCurrentDrawAmps() > Settings.ClimberHopper.STALL;
    }

    private double getTargetHeight() {
        return getState().getTargetHeight();
    }

    public double getCurrentHeight() {
        return sim.getPositionMeters();
    }

    private boolean isWithinTolerance(double toleranceMeters) {
        return Math.abs(getTargetHeight() - getCurrentHeight()) < toleranceMeters;
    }

    @Override
    public boolean atTargetHeight() {
        return isWithinTolerance(Settings.ClimberHopper.HEIGHT_TOLERANCE_METERS);
    }

    @Override
    public void periodic() {
        super.periodic();

        voltage = getState().getTargetHeight();

        sim.setInputVoltage(voltage);
        SmartDashboard.putNumber("ClimberHopper/Voltage", voltage);
        SmartDashboard.putNumber("ClimberHopper/Current", sim.getCurrentDrawAmps());
        SmartDashboard.putBoolean("ClimberHopper/Stalling", getStalling());
        SmartDashboard.putNumber("ClimberHopper/Position", getCurrentHeight());
        visualizer.update(getCurrentHeight());
        sim.update(0.02);
    }
}