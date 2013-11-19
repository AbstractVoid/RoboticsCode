/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import java.util.Timer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTableKeyNotDefined;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Dashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.'
     */
    OI oi;
    Drive drive;
    Shooter shoot;
    GravitySucks grav;
    boolean doClimb = false;
    boolean shooter = false;
    Extenze extender;
    double count;
    Compressor compressor;
    double count2;
    boolean shootup = false;

    public void robotInit() {
        SmartDashboard.putNumber("testValueOne", 9.6);
        SmartDashboard.putNumber("testValueTwo", 6.9);
        this.oi = new OI();
        this.drive = new Drive(oi);
        this.extender = new Extenze(oi);
        this.shoot = new Shooter(oi);
        this.grav = new GravitySucks(oi);
        compressor = new Compressor(5, 1);
        System.out.println("test");
        SmartDashboard.putNumber("tol", 1);
        SmartDashboard.putNumber("strafe", 1.65);
        SmartDashboard.putNumber("trapezoid", 1.085);

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        compressor.start();
        shootup = extender.extender.get();
        if (!shootup) {
            extender.solenoidSwitch(shootup, extender.extender);
            shootup = extender.extender.get();

        }
        System.out.println(extender.extender.get());

        if (count >= 100) {
            extender.solenoidSwitch(shooter, extender.shakeWeight);
            shooter = extender.shakeWeight.get();
            count = 0;
            shoot.autoShoot();
        }
        if (count2 >= 110) {
            count++;
        }
        count2++;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        SmartDashboard.putBoolean("Cupcheck ", grav.FirmlyGraspIt.get());
        SmartDashboard.putNumber("volt1 ", grav.volt1);
        SmartDashboard.putNumber("volt2 ", grav.volt2);
        SmartDashboard.putBoolean("Tipper ", grav.Tipper.get());
        SmartDashboard.putBoolean("Shakeweight ", extender.shakeWeight.get());
        SmartDashboard.putNumber("FixLeft", grav.leftFix);
        SmartDashboard.putNumber("FixRight", grav.rightFix);
        SmartDashboard.putBoolean("Hopper", extender.extender.get());
        SmartDashboard.putNumber("max L", grav.maxLeft);
        SmartDashboard.putNumber("max R", grav.maxRight);
        SmartDashboard.putNumber("off L", grav.offsetLeft);
        SmartDashboard.putNumber("off R", grav.offsetRight);
        SmartDashboard.putNumber("msgOne", grav.magOne);
        SmartDashboard.putNumber("multOne", shoot.speedmultOne);
        SmartDashboard.putNumber("multTwo", shoot.speedmultTwo);

        drive.MecaDrive();
        compressor.start();
        shoot.voltageChange();
        shoot.drive();

        if (grav.climb || grav.climb2) {
            grav.testRange();
        } else {
            grav.positionDriveAlign();
        }
        grav.resetJag();
        grav.tipper();
        grav.DontFall();
        //grav.testJag();
        if (oi.getLJoystick().getRawButton(3)) {
            grav.testRangeAuto();
        }
        extender.extendHopper();
        extender.extendShooter();
        extender.shootADisc();
        grav.holdMe();
        grav.switchDrive();


        try {
            SmartDashboard.putNumber("Shoot1Speed", shoot.tester1.getX());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            SmartDashboard.putNumber("Shoot2Speed", shoot.tester2.getX());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        SmartDashboard.putBoolean("LBSens", grav.leftAtBottom());
        SmartDashboard.putBoolean("RBSens", grav.rightAtBottom());
        SmartDashboard.putBoolean("LTSens", grav.leftAtTop());
        SmartDashboard.putBoolean("RTSens", grav.rightAtTop());

    }
}
