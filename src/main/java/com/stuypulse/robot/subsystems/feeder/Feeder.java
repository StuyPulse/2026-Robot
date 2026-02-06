package com.stuypulse.robot.subsystems.feeder;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.feeder.Feeder.FeederState;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {
    private static final Feeder instance;
    private FeederState state;

    static {
        instance = new FeederImpl();
    }

    public static Feeder getInstance() {
        return instance;
    }

    public enum FeederState {
        STOW(),
        FORWARD(),
        REVERSE(),
        STOP();
    }

    public double getTargetRPM() {
        return switch (getFeederState()) {
            case STOP -> 0;
            case FORWARD -> Settings.Feeder.FORWARD_RPM;
            case REVERSE -> Settings.Feeder.REVERSE_RPM;
            case STOW -> Settings.Feeder.STOW_RPM;
        };
    }

        private double RPM;

        private double targetRPM;

        private FeederState feederstate;

        public FeederState getFeederState(){
            return state;
        }

        public void setFeederState(FeederState state){
            this.state = state;
        }

        public void setTargetRPM(double targetRPM){
            this.targetRPM = targetRPM;
        }

        protected void Feeder() {
            state = FeederState.STOP;
        }
    
    

    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putString("Feeder/State", getFeederState().toString());
        SmartDashboard.putNumber("Feeder/speed", getTargetRPM());

        
    }



}
