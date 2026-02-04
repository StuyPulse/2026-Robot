package com.stuypulse.robot.subsystems.intake;
//CLAMP VOLTAGE
//CLAMP ANGLE
//RENAME
//SMART NUMBERS
//ORGANIZATION

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.math.SLMath;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeSim extends Intake {
    private final DCMotorSim intakeMotorSimulated;
    private PIDController pidController;
    private ArmFeedforward ffController;

    //Make a new Mechanism 2d
    //Mechanism2d object is your canvas, so the x and y that you input should be representative of the 
    //MechanismRoot2d is your name to find in GLASS (important) and the starting vector (root) of your drawing
    //MechanismLigament2d, this is where you input your starting angle and length (might be more but figure it out)

    //REMEMBER: declare outside, initialize inside.

    //Update the values in periodic (at the end of the webpage) in periodic based on what you know

    //https://docs.wpilib.org/en/stable/docs/software/dashboards/glass/mech2d-widget.html
    Mechanism2d pivotCanvas;
    MechanismRoot2d pivotRoot;
    MechanismLigament2d ligament;

    public IntakeSim() {
        //TODO: get actual gear ratio and apply it
        //TODO: potentially add std devs
        intakeMotorSimulated = new DCMotorSim(LinearSystemId.createDCMotorSystem(DCMotor.getKrakenX60(1), 0.001, 48), DCMotor.getKrakenX60(1));
        pivotCanvas = new Mechanism2d(   
            4,
            4
        );  
        pivotRoot = pivotCanvas.getRoot("PivotSim", 2 ,2);
        ligament = pivotRoot.append(new MechanismLigament2d(
            "ligament", 
            2, 
            90
        ));
        

        intakeMotorSimulated.setAngle(Math.toRadians(90));
        //TODO: set starting angle NTS (MIGHT NOT BE NEEDED, TEST FIRST)
        

    }

    @Override
    public boolean isAtTargetAngle() {
        return Math.abs(getCurrentAngle().getRadians() - getIntakeState().getTargetAngle().getRadians()) < Settings.Intake.PIVOT_ANGLE_TOLERANCE; 
    }

    @Override
    public Rotation2d getCurrentAngle() {
        return Rotation2d.fromRadians(intakeMotorSimulated.getAngularPositionRad());
    }



    @Override
    public void periodic() {
        pidController = new PIDController(
            1,
            0,
            0
        );
        ffController = new ArmFeedforward(
            0.0,
            0.0,
            0.0,
            0.0,
            0.02
        );

        //TODO: change from state to accurate value NTS
        SmartDashboard.putData("PIVOT SIM",pivotCanvas);
        //TODO: check in on Rotation2ds, and change velocity value for ffController if deemed neccessary
        double pidOutput = pidController.calculate(Math.toDegrees(intakeMotorSimulated.getAngularPositionRad()), getIntakeState().getTargetAngle().getDegrees());
        double ffOutput = ffController.calculate(pidController.getSetpoint(), 1);
        
        //pid + ff = voltage
        double voltageInput = SLMath.clamp(
            pidOutput + ffOutput,
            190,
            80
        );
        
        intakeMotorSimulated.setInputVoltage(voltageInput);
        intakeMotorSimulated.update(0.02);

        ligament.setAngle(Math.toDegrees(intakeMotorSimulated.getAngularPositionRad()));

        SmartDashboard.putNumber("INTAKE_SIMULATION/ Voltage", voltageInput);
        SmartDashboard.putNumber("INTAKE_SIMULATION/ Actual Angle (DEG)", Math.toDegrees(intakeMotorSimulated.getAngularPositionRad()));
        SmartDashboard.putNumber("INTAKE_SIMULATION/ Target Angle (DEG)", getIntakeState().getTargetAngle().getDegrees());
        SmartDashboard.putString("INTAKE_SIMULATION/ Target State", getIntakeState().toString());
        SmartDashboard.putNumber("INTAKE_SIMULATION/ PID output", pidOutput);
        SmartDashboard.putNumber("INTAKE_SIMULATION/ FF output", pidOutput);
        SmartDashboard.putNumber("INTAKE_SIMULATION/ PID Setpoint", pidController.getSetpoint());
        
        

        
        
    }

    
    

    
}