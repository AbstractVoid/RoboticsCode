/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * Author: Nicholas
 */
public class Drive {

    double[] Speed = new double[4];
    double[] speedTemp = new double[4];
    double[] speedRamp = new double[4];
    final double frontPower = 3;
    final double backPower = 2;
    public double largeSide = (.6308 * .5);  //10 is the distance between parallel sides
    public double smallSide = 0.6308; //10 is the distance between parallel sides
    double rampRate = 0.01;
    double tol = 0;
    double strafe = 0;
    //float heightSide = 10;
    public final int[] MC = {5, 9, 4/*4*/, 8/*5*/};
    /**
     * Front left motor channel
     */
    public final int FL = 5;
    /**
     * Front right motor channel
     */
    public final int FR = 9;
    /**
     * Back left motor channel
     */
    public final int BL = 4;
    /**
     * 
     */
    public final int BR = 8;
    double speedMult = 9.0;
    /**
     * Front left motor controller
     */
    public CANJaguar[] JJ = new CANJaguar[4];
    /**
     * Front right motor controller
     */
    //public CANJaguar FrontRightJJ;
    /**
     * Back left motor controller
     */
    //public CANJaguar BackLeftJJ;
    /**
     * Back right motor
     */
    final CANJaguar.ControlMode controlMode = CANJaguar.ControlMode.kVoltage; //Voltage drive
    final CANJaguar.NeutralMode neutralMode = CANJaguar.NeutralMode.kBrake;
    OI oi; //create controls
    final double Tangle = MathUtils.atan(1.0);
    final double kP = 0.003; //0
    final double kI = 0.000; //0.0000006
    final double kD = 0; //0.01
    //PID 0.003, 0, 0
    final double deadzone = .05; //deadzone to prevent joystick 

    /**
     * Enables the motors we don't have to use this because we are using a
     * voltage mode not position
     */
    public void enableControl() {
        try {
            JJ[0].enableControl();
            JJ[1].enableControl();
            JJ[2].enableControl();
            JJ[3].enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initialize motors
     */
    public void initialize() {
        try {
            for (int i = 0; i < 4; i++) {
                JJ[i] = new CANJaguar(MC[i], controlMode);
                JJ[i].configNeutralMode(neutralMode);
                // JJ[i].setVoltageRampRate(rampRate);
                JJ[i].setPID(kP, kI, kD);
            }
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * creates a drive system and initialize the motors
     *
     * @param control
     */
    public Drive(OI control) {
        this.oi = control;
        initialize();
    }

    public void setSpeeds(double[] speedArr) {
        try {
            for (int i = 0; i < speedArr.length; i++) {
                JJ[i].setX(speedMult * speedArr[i]);
            }//set speeds to motors and scales appropriately
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
   

    public void setSpeedsRamp(double[] speedArr) {
        try {
            for (int i = 0; i < speedArr.length; i++) {
                speedTemp[i] = JJ[i].getX() / speedMult;
            }
            for (int i = 0; i < speedArr.length; i++) {
                if (Math.abs(speedTemp[i] - speedArr[i]) < .1) {
                    speedRamp[i] = 0;
                } else if (speedTemp[i] < speedArr[i]) {
                    speedRamp[i] = speedTemp[i] + .1;
                } else if (speedTemp[i] > speedArr[i]) {
                    speedRamp[i] = speedTemp[i] - .1;
                }
            }
            for (int i = 0; i < speedArr.length; i++) {
                JJ[i].setX(speedMult * speedRamp[i]);
            }//set speeds to motors and scales appropriately
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Drive system computes values normalizes and sets speeds to motors
     */
    public void MecaDrive() {
        largeSide = SmartDashboard.getNumber("trapezoid");
        smallSide = 1 / largeSide;
        tol = SmartDashboard.getNumber("tol");
        strafe = SmartDashboard.getNumber("strafe");
        System.out.println(tol + "tolerance");

        double magTest = Math.sqrt((-this.oi.getLJoystick().getX() * -this.oi.getLJoystick().getX()) + (this.oi.getRJoystick().getY() * this.oi.getRJoystick().getY()));
        double dirTest = MathUtils.atan((-this.oi.getLJoystick().getX()) / (this.oi.getRJoystick().getY())) + (Math.PI / 4);
        double magnitude = -this.oi.getLJoystick().getY() - this.oi.getRJoystick().getY(); // magnitude for front and back motion
        double direction = this.oi.getLJoystick().getX(); //); //direction x direction left and right
        double rotation = this.oi.getRJoystick().getX(); //turning 
//        double magnitude = -this.oi.getXJoystick().getX();
//        double direction = this.oi.getXJoystick().getRawAxis(4);
//        double rotation = this.oi.getXJoystick().getY();
        if (Math.abs(magnitude) <= deadzone) { //makes sure the joystick values are out of deadzone or else it is 0
            magnitude = 0;
        }
        if (Math.abs(rotation) <= deadzone) {
            rotation = 0;
        }

        //   double cosD = Math.cos(dirInRad);
        //   double sinD = Math.sin(dirInRad);
        //magnitude = (magnitude) * Math.sqrt(2.0);
        /*double posmagnitude = (magnitude >0) ? magnitude : 0;
         double negmagnitude = (magnitude <0) ? magnitude : 0;*/
        double posdirection = (direction != 0) ? strafe : 0;
        //double negdirection = (direction <0) ? direction : 0;
        //Test angle method
//        Speed[0] = Math.sin(dirTest)*magTest + rotation;
//        Speed[1] = Math.cos(dirTest)*magTest - rotation;
//        Speed[2] = Math.cos(dirTest)*magTest + rotation;
//        Speed[3] = Math.sin(dirTest)*magTest - rotation;


        Speed[0] = smallSide * ((largeSide * tol * magnitude) + (strafe * direction) + (largeSide * tol * rotation)); // calculates speeds
        Speed[1] = smallSide * -((largeSide * tol * magnitude) - (strafe * direction) - (largeSide * tol * rotation));
        Speed[2] = largeSide * ((smallSide * tol * magnitude) - (strafe * direction) + (smallSide * tol * rotation));
        Speed[3] = largeSide * -((smallSide * tol * magnitude) + (strafe * direction) - (smallSide * tol * rotation));
        double maxSpeed = Math.max(
                Math.max(Math.abs(Speed[0]), Math.abs(Speed[1])),
                Math.max(Math.abs(Speed[2]), Math.abs(Speed[3])));
        if (maxSpeed > 1.0) { // normalize speeds
            Speed[0] *= posdirection /= maxSpeed;
            Speed[1] *= posdirection /= maxSpeed;
            Speed[2] *= posdirection /= maxSpeed;
            Speed[3] *= posdirection /= maxSpeed;
        }

//        
//                Speed[0] = smallSide * (largeSide * tol * magnitude + strafe*(direction) + largeSide * tol * rotation); // calculates speeds
//        Speed[1] = smallSide * -(largeSide * tol * magnitude - strafe*(direction) - largeSide * tol * rotation);
//        Speed[2] = largeSide * (smallSide * tol * magnitude - strafe*(direction) + smallSide * tol * rotation);
//        Speed[3] = largeSide * -(smallSide * tol * magnitude + strafe*(direction) - smallSide * tol * rotation);
        ///////////////////////////////////////////////////////////
        //recover from overvoltage/////////////////////////////////
        if (oi.getRJoystick().getRawButton(11)) {
            initialize();
        }
        ////////////////////////////////////////////////////////////
        //setspeeds/////////////////////////////////////////////////
        setSpeeds(Speed);
        /////////////////////////////////////////////////////////////
    }

    /**
     * Drive system computes values normalizes and sets speeds to motors
     */
    public void MecaDriveTest() {
        largeSide = SmartDashboard.getNumber("trapezoid");
        smallSide = 1 / largeSide;
        tol = SmartDashboard.getNumber("tol");
        strafe = SmartDashboard.getNumber("strafe");
        System.out.println(tol + "tolerance");

        double magTest = Math.sqrt((-this.oi.getLJoystick().getX() * -this.oi.getLJoystick().getX()) + (this.oi.getRJoystick().getY() * this.oi.getRJoystick().getY()));
        double dirTest = MathUtils.atan((-this.oi.getLJoystick().getX()) / (this.oi.getRJoystick().getY())) + (Math.PI / 4);
        double magnitude = -this.oi.getLJoystick().getY() - this.oi.getRJoystick().getY(); // magnitude for front and back motion
        double direction = this.oi.getLJoystick().getX(); //); //direction x direction left and right
        double rotation = this.oi.getRJoystick().getX(); //turning 
//        double magnitude = -this.oi.getXJoystick().getX();
//        double direction = this.oi.getXJoystick().getRawAxis(4);
//        double rotation = this.oi.getXJoystick().getY();
        if (Math.abs(magnitude) <= deadzone) { //makes sure the joystick values are out of deadzone or else it is 0
            magnitude = 0;
        }
        if (Math.abs(rotation) <= deadzone) {
            rotation = 0;
        }

        //   double cosD = Math.cos(dirInRad);
        //   double sinD = Math.sin(dirInRad);
        //magnitude = (magnitude) * Math.sqrt(2.0);
        /*double posmagnitude = (magnitude >0) ? magnitude : 0;
         double negmagnitude = (magnitude <0) ? magnitude : 0;*/
        double posdirection = (direction != 0) ? strafe : 0;
        //double negdirection = (direction <0) ? direction : 0;
        //Test angle method
        Speed[0] = Math.sin(dirTest)*magTest + rotation;
        Speed[1] = Math.cos(dirTest)*magTest - rotation;
        Speed[2] = Math.cos(dirTest)*magTest + rotation;
       Speed[3] = Math.sin(dirTest)*magTest - rotation;


     //   Speed[0] = smallSide * (largeSide * tol * magnitude + (direction) + largeSide * tol * rotation); // calculates speeds
     //   Speed[1] = smallSide * -(largeSide * tol * magnitude - (direction) - largeSide * tol * rotation);
     //   Speed[2] = largeSide * (smallSide * tol * magnitude - (direction) + smallSide * tol * rotation);
     //   Speed[3] = largeSide * -(smallSide * tol * magnitude + (direction) - smallSide * tol * rotation);
//        double maxSpeed = Math.max(
//                Math.max(Math.abs(Speed[0]), Math.abs(Speed[1])),
//                Math.max(Math.abs(Speed[2]), Math.abs(Speed[3])));
//        if (maxSpeed > 1.0) { // normalize speeds
//            Speed[0] *= posdirection /= maxSpeed;
//            Speed[1] *= posdirection /= maxSpeed;
//            Speed[2] *= posdirection /= maxSpeed;
//            Speed[3] *= posdirection /= maxSpeed;
//        }

//        
//                Speed[0] = smallSide * (largeSide * tol * magnitude + strafe*(direction) + largeSide * tol * rotation); // calculates speeds
//        Speed[1] = smallSide * -(largeSide * tol * magnitude - strafe*(direction) - largeSide * tol * rotation);
//        Speed[2] = largeSide * (smallSide * tol * magnitude - strafe*(direction) + smallSide * tol * rotation);
//        Speed[3] = largeSide * -(smallSide * tol * magnitude + strafe*(direction) - smallSide * tol * rotation);
        ///////////////////////////////////////////////////////////
        //recover from overvoltage/////////////////////////////////
        if (oi.getRJoystick().getRawButton(11)) {
            initialize();
        }
        ////////////////////////////////////////////////////////////
        //setspeeds/////////////////////////////////////////////////
        setSpeeds(Speed);
        /////////////////////////////////////////////////////////////
    }
}