package com.stuypulse.robot.subsystems.intake;

import java.util.Optional;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.mechanism.LoggedMechanism2d;
import org.littletonrobotics.junction.mechanism.LoggedMechanismLigament2d;
import org.littletonrobotics.junction.mechanism.LoggedMechanismRoot2d;

import com.stuypulse.robot.constants.Gains;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;
import com.stuypulse.robot.util.SysId;
import com.stuypulse.stuylib.math.SLMath;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// TODO: implement the abstract methods in Intake.java inside IntakeSim.java !!!
public class IntakeSim extends Intake {
    private final CommandSwerveDrivetrain swerve = CommandSwerveDrivetrain.getInstance();

    private double latestAppliedKP;   private double latestAppliedKI;   private double latestAppliedKD;
  private final DCMotorSim intakeSimMotor;
    private PIDController pidController;
    private ArmFeedforward ffController;

    private Optional<Double> pivotVoltageOverride;
    private Optional<Double> rollerVoltageOverride;

    @AutoLogOutput
    LoggedMechanism2d pivotCanvas = new LoggedMechanism2d(Units.inchesToMeters(16), Units.inchesToMeters(16));

    @AutoLogOutput
    LoggedMechanismRoot2d pivotRoot;

    @AutoLogOutput
    LoggedMechanismLigament2d pivotLigament;

    public IntakeSim() {
        pivotVoltageOverride = Optional.empty();
        rollerVoltageOverride = Optional.empty();

        //TODO: potentially add std devs
        intakeSimMotor = new DCMotorSim(
            LinearSystemId.createDCMotorSystem(
                DCMotor.getKrakenX60(1),
                Settings.Intake.JKgMetersSquared,
                Settings.Intake.GEAR_RATIO
            ), 
            DCMotor.getKrakenX60(1));

        
        pivotRoot = pivotCanvas.getRoot(
            "Pivot_Root",
            Units.inchesToMeters(8),
            Units.inchesToMeters(8));

        pivotLigament = pivotRoot.append(new LoggedMechanismLigament2d(
            "Pivot_Ligament", 
            Units.inchesToMeters(2), 
            0
        ));

        pidController = new PIDController( 
            Gains.Intake.Pivot.kP.doubleValue(),
            Gains.Intake.Pivot.kI.doubleValue(),
            Gains.Intake.Pivot.kD.doubleValue()
        );
        ffController = new ArmFeedforward(
            Gains.Intake.Pivot.kS.doubleValue(),
            Gains.Intake.Pivot.kG.doubleValue(),
            Gains.Intake.Pivot.kV.doubleValue(),
            Gains.Intake.Pivot.kA.doubleValue(),
            Settings.Intake.dT
        );

        latestAppliedKP = Gains.Intake.Pivot.kP.doubleValue();
        latestAppliedKI = Gains.Intake.Pivot.kI.doubleValue();
        latestAppliedKD = Gains.Intake.Pivot.kD.doubleValue();

        intakeSimMotor.setAngle(Math.toRadians(0));
    }

    @Override
    public boolean isAtTargetAngle() {
        return Math.abs(getCurrentAngle().getRotations() - getIntakeState().getTargetAngle().getRotations()) < Settings.Intake.PIVOT_ANGLE_TOLERANCE.getRotations(); 
    }

    @Override
    public Rotation2d getCurrentAngle() {
        return Rotation2d.fromDegrees(SLMath.clamp(
            Math.toDegrees(intakeSimMotor.getAngularPositionRad()),
            Settings.Intake.PIVOT_MIN_ANGLE.getDegrees(),
            Settings.Intake.PIVOT_MAX_ANGLE.getDegrees()
        ));
    }

    
    
    public void updatePidContoller() {
        if (Gains.Intake.Pivot.kP.doubleValue() != latestAppliedKP){
           pidController.setP(Gains.Intake.Pivot.kP.doubleValue());
            latestAppliedKP = Gains.Intake.Pivot.kP.doubleValue();
      }
        if (Gains.Intake.Pivot.kI.doubleValue() != latestAppliedKI){
           pidController.setI(Gains.Intake.Pivot.kI.doubleValue());
            latestAppliedKI = Gains.Intake.Pivot.kI.doubleValue();
      }
        if (Gains.Intake.Pivot.kD.doubleValue() != latestAppliedKD){
           pidController.setD(Gains.Intake.Pivot.kD.doubleValue());
            latestAppliedKD = Gains.Intake.Pivot.kD.doubleValue();
      }


        //Relaized this was setting arbitrarily when it was the same to the same value which is pretty much what we were trying to avoid
        // pidController.setP(Gains.Intake.Pivot.kP.doubleValue() == pidController.getP() ? pidController.getP() : pidController.getP() + (Gains.Intake.Pivot.kP.doubleValue() - pidController.getP()));
        // pidController.setI(Gains.Intake.Pivot.kI.doubleValue() == pidController.getI() ? pidController.getI() : pidController.getI() + (Gains.Intake.Pivot.kI.doubleValue() - pidController.getI()));
        // pidController.setD(Gains.Intake.Pivot.kD.doubleValue() == pidController.getD() ? pidController.getD() : pidController.getD() + (Gains.Intake.Pivot.kD.doubleValue() - pidController.getD()));
    }

    // @Override
    // public Pose3d getMechanismIntakePose3d() {
    //     return pivotCanvas.generate3dMechanism().get(0);
    // }

    // @Override
    // public Pose3d getActualIntakePose3d() {
    //     //XYZ from drivetrain, angle from the mechanism pose then add pitch
    //     // return new Pose3d(
    //     //     swerve.getDrivetrainPose3d().getX(), 
    //     //     swerve.getDrivetrainPose3d().getY(), 
    //     //     swerve.getDrivetrainPose3d().getZ(), 
    //     //     new Rotation3d(
    //     //         swerve.getDrivetrainPose3d().getRotation().getX(),
    //     //         getCurrentAngle().getDegrees(), //should only vary from drivetrain in terms of pitch
    //     //         swerve.getDrivetrainPose3d().getRotation().getZ()
    //     //     ));

    //     return new Pose3d(
    //         0,
    //         0,
    //         0,
    //         new Rotation3d(
    //             0,
    //             getCurrentAngle().getDegrees(), //should only vary from drivetrain in terms of pitch
    //             0
    //         ));
    // }

     /**
     * @param voltage as an Optional<Double> to handle the case of null values;
     *                in the case that voltage is not null, voltageOverride will be
     *                set to thhe passed in voltage
     */
    
    @Override
    public void setRollerVoltageOverride(Optional<Double> voltage) {
        this.rollerVoltageOverride = voltage;
    }

    /**
     * @param voltage as an Optional<Double> to handle the case of null values;
     *                in the case that voltage is not null, voltageOverride will be
     *                set to thhe passed in voltage
     */

    @Override
    public void setPivotVoltageOverride(Optional<Double> voltage) {
        this.pivotVoltageOverride = voltage;
    }

    @Override
    public SysIdRoutine getPivotSysIdRoutine() {

        //TODO: fill in later (will probs never be used though) ?
        return null;
    }

    @Override
    public SysIdRoutine getRollerSysIdRoutine() {
        return null;
    }


    @Override
    public void periodic() {
        // Logger.recordOutput("Intake Logged Mech2d", pivotCanvas);
        updatePidContoller();

        SmartDashboard.putData("Pivot_Mechanism2d_SIM", pivotCanvas);
        //IntakeVisualizer.getInstance().updateIntakeStuff(getCurrentAngle(), getIntakeState().getTargetDutyCycle(), isAtTargetAngle())
        //TODO: change velocity value for ffController (?)
        double pidOutput = pidController.calculate(getCurrentAngle().getDegrees(), getIntakeState().getTargetAngle().getDegrees());
        double ffOutput = ffController.calculate(pidController.getSetpoint(), 1);
        
        double voltageInput = SLMath.clamp(
            pidOutput + ffOutput,
            Settings.Intake.VOLTAGE_MIN,
            Settings.Intake.VOLTAGE_MAX
        );
    
        intakeSimMotor.setInputVoltage(voltageInput);
        intakeSimMotor.update(Settings.Intake.dT);

        pivotLigament.setAngle(getCurrentAngle().getDegrees());
        //pivotRoot.setPosition(null, null); //update cad for Ascope or calculate this

        SmartDashboard.putNumber("INTAKE_SIMULATION/ Voltage", voltageInput);
        SmartDashboard.putNumber("INTAKE_SIMULATION/ Actual Angle (DEG)", getCurrentAngle().getDegrees());
        SmartDashboard.putNumber("INTAKE_SIMULATION/ Target Angle (DEG)", getIntakeState().getTargetAngle().getDegrees());
        SmartDashboard.putString("INTAKE_SIMULATION/ Target State", getIntakeState().toString());
        SmartDashboard.putNumber("INTAKE_SIMULATION/ PID output", pidOutput);
        SmartDashboard.putNumber("INTAKE_SIMULATION/ FF output", pidOutput);
        SmartDashboard.putNumber("INTAKE_SIMULATION/ PID Setpoint", pidController.getSetpoint());
        SmartDashboard.putBoolean("INTAKE_SIMULATION/ at Target Angle?", isAtTargetAngle());  
        SmartDashboard.putNumber("Intake/Settings/Pivot/MAX ANGLE", Settings.Intake.PIVOT_MAX_ANGLE.getDegrees());
        SmartDashboard.putNumber("Intake/Settings/Pivot/MIN ANGLE", Settings.Intake.PIVOT_MIN_ANGLE.getDegrees());

        

        SmartDashboard.putNumber("INTAKE_SIMULATION/ PID kP value FROM the controller", pidController.getP());
        //Learned from test that you would theoretically need a supplier post initialization
    }
}