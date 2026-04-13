/************************ PROJECT TRIBECBOT *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.commands.vision;

import com.stuypulse.robot.constants.Cameras.Camera.Pipeline;
import com.stuypulse.robot.subsystems.vision.LimelightVision;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetPipeline extends InstantCommand {
    private LimelightVision vision;
    private Pipeline pipeline;

    public SetPipeline(Pipeline pipeline) {
        this.vision = LimelightVision.getInstance();
        this.pipeline = pipeline;
    }

    @Override
    public void initialize() {
        vision.setPipeline(pipeline);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}