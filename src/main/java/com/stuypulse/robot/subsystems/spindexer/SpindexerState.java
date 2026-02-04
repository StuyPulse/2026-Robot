package com.stuypulse.robot.subsystems.spindexer;

import com.stuypulse.robot.constants.Gains;
import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Spindexer extends SubsystemBase {
    private static final Spindexer instance;
    private SpindexerState spindexerState;
    public double voltage;

    static {
        instance = new SpindexerImpl();
    }

    public static Spindexer getInstance() {
        return instance;
    }

    public enum SpindexerState {
        STOP,
        DYNAMIC,
        FORWARD,
        REVERSE;
    }

    public SpindexerState getSpindexerState() {
        return spindexerState;
    }

    public void setSpindexerState(SpindexerState state) {
        this.spindexerState = state;
    }

    public double setTargetVolage(voltage) {
        this.voltage = voltage;
    }

    public double getTargetVoltage(){
        return switch(getSpindexerState()) {
            case STOP -> 0;
            case DYNAMIC -> getVoltageBasedOnDistance();            
            case FORWARD -> getForwardVoltage();
            case REVERSE -> getReverseVoltage();
        };
    }

    public abstract double getVoltageBasedOnDistance();

    @Override
    public void periodic() {
        SmartDashboard.putString("Spindexer/state", state.);

    }
}
