/************************ PROJECT TRIBECBOT *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.commands.auton.test;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import com.pathplanner.lib.path.PathPlannerPath;

public class BoxTest extends SequentialCommandGroup {

    // private RobotContainer robot = new RobotContainer();
    // private double waitTime = robot.getWaitTime();
    
    public BoxTest(PathPlannerPath... paths) {

        addCommands(
            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[0])
        );

    }

}
