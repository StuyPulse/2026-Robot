package com.stuypulse.robot.subsystems.spindexer;

import java.util.Optional;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public abstract class Spindexer extends SubsystemBase {
    private static final Spindexer instance;
    private SpindexerState spindexerState;

    static {
        instance = new SpindexerImpl();
    }

    public static Spindexer getInstance() {
        return instance;
    }

    public enum SpindexerState {
        STOP,
        DYNAMIC, // Dynamically updates the RPM based on the robot's distance to hub
        FORWARD,
        REVERSE;
    }

    public Spindexer() {
        spindexerState = SpindexerState.STOP;
    }

    public SpindexerState getSpindexerState() {
        return spindexerState;
    }

    public void setSpindexerState(SpindexerState state) {
        this.spindexerState = state;
    }

    public double getTargetRPM() {
        return switch (getSpindexerState()) {
            case STOP -> 0;
            case DYNAMIC -> getRPMBasedOnDistance(); 
            case FORWARD -> Settings.Spindexer.FORWARD_RPM;
            case REVERSE -> Settings.Spindexer.REVERSE_RPM;
        };
    }

    public abstract SysIdRoutine getSysIdRoutine();
    public abstract double getRPMBasedOnDistance(); // implement in SpindexerImpl
    public abstract double getVoltageOverride();
    public abstract void setVoltageOverride(Optional<Double> voltage);

    @Override
    public void periodic() {
        if (Settings.DEBUG_MODE) {
            SmartDashboard.putString("Spindexer/State", getSpindexerState().toString());
            SmartDashboard.putNumber("Spindexer/Target RPM", getTargetRPM());
        }
    }
}