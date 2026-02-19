/************************ PROJECT TRIBECBOT *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.util.hoodedshooter;

import com.stuypulse.robot.constants.Constants;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public final class ShotCalculator {
    public static final double g = 9.81;

    public record StationarySolution(
        Rotation2d launchPitchAngle,
        double flightTimeSeconds) {
    }

    // calculates the launch angle for a stationary robot to shoot into the goal
    public static StationarySolution solveBallisticWithSpeed(
        Pose3d shooterPose,
        Pose3d targetGoal,
        double targetRPM) {

        Translation3d s = shooterPose.getTranslation();
        Translation3d t = targetGoal.getTranslation();

        double dx = t.getX() - s.getX();
        double dy = t.getY() - s.getY();
        double dz = t.getZ() - s.getZ();

        double d = Math.hypot(dx, dy);

        double launchSpeed = targetRPM * (Math.PI / 30) * Constants.HoodedShooter.Shooter.FLYWHEEL_RADIUS;
        double v2 = launchSpeed * launchSpeed;

        double discriminant = v2 * v2 - g * (g * d * d + 2.0 * dz * v2);

        if (discriminant < 0) {
            return new StationarySolution(Rotation2d.kZero, 0);
        }

        // LOW-ARC solution (use + for high arc)
        double tanTheta = (v2 - Math.sqrt(discriminant)) / (g * d);

        double launchAngle = Math.atan(tanTheta);

        double v_x = launchSpeed * Math.cos(launchAngle);
        double time = d / v_x;

        return new StationarySolution(Rotation2d.fromRadians(launchAngle), time);
    }


    public record SOTMSolution(
        Rotation2d launchPitchAngle,
        Rotation2d targetYaw,
        Pose3d virtualGoal) {
    }

    public static SOTMSolution solveShootOnTheFly(
        Pose3d shooterPose,
        Pose3d targetGoal,
        ChassisSpeeds fieldRelativeSpeeds,
        double targetRPM,
        int maxIterations,
        double timeTolerance) {

        // We know that v_ball = v_robot + v_shooter, so 
        // (v_robot + v_shooter) * flightTime = distanceToTargetGoal
        // rearranging, we can get (v_shooter) * flight_time = distanceToTargetGoal - v_robot * flightTime

        // so we can instead shoot at a virtual goal and treat the robot as stationary:
        // distance to virtualGoal = distanceToTargetGoal - v_robot * flightTime

        // but to do this we need the flight time, but to get the flight time we need the
        // target hood angle, but that's what we're trying to find in the first place.

        // Thus, we can make an initial guess for the flight time: the flight time if the robot were stationary
        // We want our guess to converge such that the left side equals the right side:
        // (v_shooter) * t_guess = distance - v_robot * t_guess, which would make t_guess = flightTime

        // We do the right side first using our inital guess, and then update t_guess with a new guess by 
        // calculating the flightTime to that virtualPose.

        // The goal is that the flightTime converges within maxIterations.

        StationarySolution sol = solveBallisticWithSpeed(
            shooterPose,
            targetGoal,
            targetRPM
        );
        
        double t_guess = sol.flightTimeSeconds();
        
        Pose3d virtualGoal = targetGoal;

        Translation3d shooterTranslation = shooterPose.getTranslation();
             
        for (int i = 0; i < maxIterations; i++) {

            double dx = fieldRelativeSpeeds.vxMetersPerSecond * t_guess;
            double dy = fieldRelativeSpeeds.vyMetersPerSecond * t_guess;

            virtualGoal = new Pose3d(
                targetGoal.getX() - dx,
                targetGoal.getY() - dy,
                targetGoal.getZ(),
                targetGoal.getRotation());

            StationarySolution newSol = solveBallisticWithSpeed(
                shooterPose,
                virtualGoal,
                targetRPM);

            sol = newSol;
            if (Math.abs(newSol.flightTimeSeconds() - t_guess) < timeTolerance) {
                break;
            }

            t_guess = newSol.flightTimeSeconds();

        }
        
        Translation3d virtualTranslation = virtualGoal.getTranslation();

        double yaw = Math.atan2(
            virtualTranslation.getY() - shooterTranslation.getY(),
            virtualTranslation.getX() - shooterTranslation.getX() 
        ); 

        return new SOTMSolution(
            sol.launchPitchAngle(),
            Rotation2d.fromRadians(yaw),
            virtualGoal);
    }
}