package com.stuypulse.robot.constants;

public class Constants {
    public interface Intake {
        //TODO: get actual gear ratio and apply it
        double GEAR_RATIO = 48;
        //TODO: get actual value and put here
        double JKgMetersSquared = 0.001;

        double VOLTAGE_MAX = 12;
        double VOLTAGE_MIN = -12;

        double dT = 0.02;
    }
}
