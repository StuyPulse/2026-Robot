package com.stuypulse.robot.subsystems.climberhopper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberHopper extends SubsystemBase {
    private static final ClimberHopper instance;
    private ClimberHopperState state;

    static {
        instance = new ClimberHopperImpl();
    }

    public static ClimberHopper getInstance() {
        return instance;
    }

    public enum ClimberHopperState {
        STOW();

        private double targetHeight;
        
        private ClimberHopperState() {

        }
    }

    @Override
    public void periodic() {

    }
}
