package com.stuypulse.robot.subsystems.spindexer;

import com.stuypulse.robot.constants.Gains;
import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Spindexer extends SubsystemBase {
    private static final Spindexer instance;
    private SpindexerState spindexerState;
    private double voltage; 

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

    public Spindexer() {
        spindexerState = SpindexerState.STOP;
    }

    public SpindexerState getSpindexerState() {
        return spindexerState;
    }

    public void setSpindexerState(SpindexerState state) {
        this.spindexerState = state;
    }

    public void setTargetVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getTargetVoltage(){
        return switch(getSpindexerState()) {
            case STOP -> 0;
            case DYNAMIC -> getVoltageBasedOnDistance();            
            case FORWARD -> Settings.Spindexer.FORWARD_VOLTAGE;
            case REVERSE -> Settings.Spindexer.REVERSE_VOLTAGE;
        };
    }

    public double getVoltageBasedOnDistance(){
        return 0;
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Spindexer/State", getSpindexerState().toString());
        
    }
}