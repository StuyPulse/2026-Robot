package com.stuypulse.robot.subsystems.handoff;

import java.util.Optional;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public abstract class Handoff extends SubsystemBase {
    private static final Handoff instance;
    private HandoffState state;

    static {
        instance = new HandoffImpl();
    }

    public static Handoff getInstance() {
        return instance;
    }

    public enum HandoffState {
        STOP,
        FORWARD,
        REVERSE;
    }

    public Handoff() {
        state = HandoffState.STOP;
    }

    public double getTargetRPM() {
        return switch (getState()) {
            case STOP -> 0;
            case FORWARD -> Settings.Handoff.HANDOFF_RPM.get();
            case REVERSE -> Settings.Handoff.HANDOFF_REVERSE;
        };
    }

    public HandoffState getState() {
        return state;
    }

    public void setState(HandoffState state) {
        this.state = state;
    }

    public boolean atTolerance() {
        double diff = Math.abs(getTargetRPM() - getCurrentRPM());
        return diff < Settings.Handoff.RPM_TOLERANCE;
    }

    public abstract double getCurrentRPM();

    public abstract SysIdRoutine getSysIdRoutine();
    public abstract void setVoltageOverride(Optional<Double> voltage);

    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putString("Handoff/State", getState().toString());
        SmartDashboard.putString("States/Handoff", getState().toString());

        SmartDashboard.putNumber("Handoff/Target RPM", getTargetRPM());
        SmartDashboard.putNumber("Handoff/Current RPM", getCurrentRPM());
    }
}
