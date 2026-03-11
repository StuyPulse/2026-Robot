/************************ PROJECT TRIBECBOT *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.commands.auton.regular;

import com.stuypulse.robot.commands.handoff.HandoffRun;
import com.stuypulse.robot.commands.handoff.HandoffStop;
import com.stuypulse.robot.commands.intake.IntakeDeploy;
import com.stuypulse.robot.commands.intake.IntakeRunRollers;
import com.stuypulse.robot.commands.intake.IntakeStopRollers;
import com.stuypulse.robot.commands.spindexer.SpindexerRun;
import com.stuypulse.robot.commands.spindexer.SpindexerStop;
import com.stuypulse.robot.commands.superstructure.SuperstructureInterpolation;
import com.stuypulse.robot.subsystems.handoff.Handoff;
import com.stuypulse.robot.subsystems.superstructure.Superstructure;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

import com.pathplanner.lib.path.PathPlannerPath;

public class RightTwoCycle extends SequentialCommandGroup {
    
    public RightTwoCycle(PathPlannerPath... paths) {

        addCommands(

            // NZ Trip 1
            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[0]).alongWith(
                new WaitCommand(0.5).andThen(new IntakeDeploy())
            ),
            new SuperstructureInterpolation(),

            // Trip 1 To Score
            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[1]).alongWith(
                new WaitCommand(0.5).andThen(new SuperstructureInterpolation())
            ),
            new WaitUntilCommand(() -> Superstructure.getInstance().atTolerance()),
            new HandoffRun().alongWith(new WaitUntilCommand(() -> Handoff.getInstance().atTolerance())).andThen(
                new SpindexerRun()
            ).andThen(new WaitCommand(5.0)),

            // NZ Trip 2
            new ParallelCommandGroup(
                CommandSwerveDrivetrain.getInstance().followPathCommand(paths[2]),
                new HandoffStop(),
                new SpindexerStop()
            ),

            new ParallelCommandGroup(
                new WaitUntilCommand(() -> Superstructure.getInstance().atTolerance())
                // new SwerveClimbAlign()
            ),
            new HandoffRun().alongWith(new WaitUntilCommand(() -> Handoff.getInstance().atTolerance())).andThen(
                new SpindexerRun()
            )
            // .until(() -> DriverStation.getMatchTime() < 2).andThen(
            //     new ParallelCommandGroup(
            //         new HandoffStop(),
            //         new SpindexerStop(),
            //         new ClimberDown()
            //     )
            // )
        
        );

    }

}
