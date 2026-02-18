package com.stuypulse.robot.subsystems.climberhopper;

import com.stuypulse.robot.Robot;
import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.math.SLMath;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public abstract class ClimberHopper extends SubsystemBase {
    private static final ClimberHopper instance;
    
    static {
        if (Robot.isReal()) {
            instance = new ClimberHopperImpl();
        } else {
            instance = new ClimberHopperSim();
        }
    }

    public static ClimberHopper getInstance() {
        return instance;
    }

    public enum ClimberHopperState {
        CLIMBER_UP(Settings.ClimberHopper.CLIMBER_UP_HEIGHT_METERS),
        CLIMBER_DOWN(Settings.ClimberHopper.CLIMBER_DOWN_HEIGHT_METERS),
        HOPPER_UP(Settings.ClimberHopper.HOPPER_UP_HEIGHT_METERS),
        HOPPER_DOWN(Settings.ClimberHopper.HOPPER_DOWN_HEIGHT_METERS);
    
        private double targetHeight;
        
        private ClimberHopperState(double targetHeight) {
            this.targetHeight = targetHeight;
        }
        
        public double getTargetHeight() {
            return SLMath.clamp(targetHeight, Constants.ClimberHopper.MIN_HEIGHT_METERS, Constants.ClimberHopper.MAX_HEIGHT_METERS);
        }

    }
    
    protected ClimberHopperState state;

    protected ClimberHopper() {
        this.state = ClimberHopperState.HOPPER_UP;
    }
        
    public ClimberHopperState getState() {
        return state;
    }

    public void setState(ClimberHopperState state) {
        this.state = state;
        // TODO: Voltage Override?
    }

    public abstract boolean getStalling();
    public abstract double getCurrentHeight();
    public abstract boolean atTargetHeight();

    // TODO: Write SysID Routines
    // public abstract SysIdRoutine getSysIdRoutine();

    @Override
    public void periodic() {
        SmartDashboard.putString("ClimberHopper/State", getState().toString());

        SmartDashboard.putNumber("ClimberHopper/Target Height (m)", getState().getTargetHeight());
        SmartDashboard.putNumber("ClimberHopper/Current Height (m)", getCurrentHeight());
        SmartDashboard.putBoolean("ClimberHopper/At Target Height", atTargetHeight());
    }
}