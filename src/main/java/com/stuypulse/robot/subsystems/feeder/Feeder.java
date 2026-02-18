package com.stuypulse.robot.subsystems.feeder;

import java.util.Optional;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public abstract class Feeder extends SubsystemBase {
    private static final Feeder instance;
    private FeederState state;

    static {
        instance = new FeederImpl();
    }

    public static Feeder getInstance() {
        return instance;
    }

    public enum FeederState {
        STOW,
        FORWARD,
        REVERSE,
        STOP;
    }

    public Feeder() {
        state = FeederState.STOP;
    }

    /**
     * @return target RPM based on current state
     */
    public double getTargetRPM() {
        return switch (getFeederState()) {
            case STOP -> 0;
            case STOW -> Settings.Feeder.STOW_RPM; 
            case FORWARD -> Settings.Feeder.FORWARD_RPM;
            case REVERSE -> Settings.Feeder.REVERSE_RPM;

        };
    }

    /**
     * @return current feeder state
     */
    public FeederState getFeederState() {
        return state;
    }

    /**
     * @param state to set new feeder state
     */
    public void setFeederState(FeederState state) {
        this.state = state;
    }

    public boolean atTolerance() {
        double diff = Math.abs(getTargetRPM() - getFeederRPM());
        return diff < Settings.Feeder.RPM_TOLERANCE;
    }

    public abstract double getFeederRPM();

    public abstract SysIdRoutine getSysIdRoutine();
    public abstract double getVoltageOverride();
    public abstract void setVoltageOverride(Optional<Double> voltage);

    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putString("Feeder/State", getFeederState().toString());
        SmartDashboard.putNumber("Feeder/Speed", getTargetRPM());
    }
}
