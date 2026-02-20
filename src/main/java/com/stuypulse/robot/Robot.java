/************************ PROJECT 2026 ************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot;

import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;

import com.ctre.phoenix6.SignalLogger;
import com.stuypulse.robot.subsystems.intake.Intake;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends LoggedRobot {

    private RobotContainer robot;
    private Command auto;
    private static Alliance alliance;

    private final CommandSwerveDrivetrain swerve = CommandSwerveDrivetrain.getInstance();
    private final Intake intake = Intake.getInstance();
    //StructPublisher<Pose3d> publisher;

    public static boolean isBlue() {
        return alliance == Alliance.Blue;
    }

    /*************************/
    /*** ROBOT SCHEDULEING ***/
    /*************************/

    @Override
    public void robotInit() {
        robot = new RobotContainer();

        DataLogManager.start();
        SignalLogger.start();

        Logger.recordMetadata("TribecBot", "TribecBot Ascope");
        Logger.addDataReceiver(new NT4Publisher());
        Logger.start();

    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();

        //TODO: Apply a atTargetAngle inside of intake
        //TODO: Test if driving works rn
        Logger.recordOutput("DriveTrainPose/Pose3d", swerve.getDrivetrainPose3d());
        Logger.recordOutput("IntakeSim/Component_Pose3d_Array", new Pose3d[] {new Pose3d()}); //zeroed by json
        Logger.recordOutput("IntakeSim/Final_Component_Pose3d_Array", new Pose3d[] {new Pose3d(0.1,0,0.1,new Rotation3d(0, intake.getCurrentAngle().getDegrees(), 0))});
        


    }

    /*********************/
    /*** DISABLED MODE ***/
    /*********************/

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    /***********************/
    /*** AUTONOMOUS MODE ***/
    /***********************/  

    @Override
    public void autonomousInit() {
        auto = robot.getAutonomousCommand();

        if (auto != null) {
            auto.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    /*******************/
    /*** TELEOP MODE ***/
    /*******************/

    @Override
    public void teleopInit() {
        if (auto != null) {
            auto.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    /*****************/
    /*** TEST MODE ***/
    /*****************/

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void testExit() {}
}
