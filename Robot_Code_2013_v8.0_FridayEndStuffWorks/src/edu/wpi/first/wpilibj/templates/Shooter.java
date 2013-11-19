 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTableKeyNotDefined;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * Author: Brad Remington (BR, hue?)
 */
// Whoopystick
//dudepiston is pneumatic pusher
public class Shooter {

    public int shootingMotor = 6;
    public int shootingMotor2 = 2;
    public Victor sucksButtOne = new Victor(6);
    public Victor sucksButtTwo = new Victor(2);
    double maxspeed;
    double angle;
    double speeddif;
    double speed = 0;
    double testValueOne = SmartDashboard.getNumber("testValueOne");
    double testValueTwo = SmartDashboard.getNumber("testValueTwo");
    OI oi;
    CANJaguar Shrek;
    double speedmultOne = 0; //goal side wheel
    double speedmultTwo = 0;  //feeder side wheel
    public CANJaguar tester1;
    public CANJaguar tester2;
    public CANJaguar tester3;
    public CANJaguar tester4;
    public double magOne;
    public double magTwo;
    public double speedOne;
    public double speedTwo;
    public double tspeedTwo;
    public double speedThree;
    public double speedFour;
    public double deadZone = .1;
    CANJaguar.ControlMode controlMode = CANJaguar.ControlMode.kVoltage; //Voltage drive
    CANJaguar.NeutralMode neutralMode2 = CANJaguar.NeutralMode.kBrake;
    CANJaguar.NeutralMode neutralMode = CANJaguar.NeutralMode.kCoast;
    CANJaguar.ControlMode controlModew = CANJaguar.ControlMode.kSpeed;
//NetworkTable table = NetworkTable.getTable("camera");
    public double distance;

    public Shooter(OI oi) {
        this.oi = oi;
        initializeOne();
        initializeTwo();
        //NetworkTable.setTeam(1259);
        //NetworkTable.setIPAddress("10.12.59.2");
        //       distance = table.getDouble("distance");

    }

    public void initializeOne() {
        try {
            tester1 = new CANJaguar(shootingMotor, controlMode);
            tester1.configNeutralMode(neutralMode);
            //FrontLeftJJ.setPID(kP, kI, kD);
//            FrontLeftJJ.setVoltageRampRate(rampRate);
//            BackLeftJJ.setVoltageRampRate(rampRate);
//            FrontRightJJ.setVoltageRampRate(rampRate);
//            BackRightJJ.setVoltageRampRate(rampRate);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void initializeTwo() {
        try {
            tester2 = new CANJaguar(shootingMotor2, controlMode);
            tester2.configNeutralMode(neutralMode);
            //FrontLeftJJ.setPID(kP, kI, kD);
//            FrontLeftJJ.setVoltageRampRate(rampRate);
//            BackLeftJJ.setVoltageRampRate(rampRate);
//            FrontRightJJ.setVoltageRampRate(rampRate);
//            BackRightJJ.setVoltageRampRate(rampRate);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void voltageChange() {

        //3 point goal
        if (this.oi.getY()) { //shoot mid
            speedmultOne = 10.6; //10.1 8.7
            speedmultTwo = 8.2; //2.16
        } //2 point goal
        else if (this.oi.getX()) {
            speedmultOne = 10.4; //10.7 8.2
            speedmultTwo = 7.9;
        } //pooter speed
        else if (this.oi.getB()) {
            speedmultOne = 4.0;
            speedmultTwo = 7.5;
        } else if (this.oi.getLJoystick().getRawButton(10)) {
            speedmultOne = testValueOne;
            speedmultTwo = testValueTwo;
        }

    }

    public void autoShootThree() {
        try {
            tester1.setX(10.8); //9.5 8.2 2 pt
            tester2.setX(8.3);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    public void autoShoot() {
        try {
            tester1.setX(10.3); //9.5 8.2 2 pt
            tester2.setX(7.8);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void autoShootMid() { //shoot middle
        try {
            tester1.setX(10.5);
            tester2.setX(8.0); //9.2

        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void autoShootThreeSide() { //shoot middle
        try {
            tester1.setX(10.6);
            tester2.setX(8.5);

        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void drive() {
        try {
            magOne = this.oi.getXJoystick().getY();
            if (Math.abs(magOne) < deadZone) {
                magOne = 0;
            }
            if (Math.abs(magTwo) < deadZone) {
                magTwo = 0;
            }
            speedOne = magOne * speedmultOne; //4500
            speedTwo = magTwo * speedmultTwo; //14000
            tspeedTwo = magOne * speedmultTwo;
            tester1.setX(-speedOne);
            tester2.setX(-tspeedTwo);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void testValue() {
        if (oi.getLJoystick().getTrigger()) {

            boolean distcheck = NetworkTable.getTable("camera").containsKey("distance");
            NetworkTable.getTable("rob").putNumber("x", 8008135);
            System.out.println(distcheck);
            if (distcheck) {
                distance = NetworkTable.getTable("camera").getNumber("distance");
            }
            System.out.println("Distance:" + " " + distance);
        }

    }
}
