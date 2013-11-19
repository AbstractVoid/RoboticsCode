/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author Programming
 */
public class GravitySucks {

    boolean gotToTop = false;
    public CANJaguar Beavis;
    public CANJaguar Butthead;
    public int climbmotor1 = 3;
    public int climbmotor2 = 7;
    public int climbAction;
    public double magOne;
    public double magTwo;
    public double speedOne;
    boolean gotToBottom = false;
    public double speedTwo;
    public double deadZone = .25;
    public double speedMult = 10;
    final int codesPerRev = 200;
    final private double maxRPM = 360; //we dont know until testing
    final private double maxRPL = 20; //we dont know until testing
    private double leftPos = 0;
    private double rightPos = 0;
    private double stopPos = 0; //test to stop here
    public double rightPosition = 0.0;
    public double leftPosition = 0.0;
    public double count = 0;
    public double offsetLeft;
    public double offsetRight;
    private double topsetLeft;
    private double topsetRight;
    boolean toggleTipper = false;
    boolean switchTipper = false;
    boolean switchFGI = false;
    boolean toggleFGI = false;
    boolean climb = false;
    boolean climb2 = false;
    Solenoid Tipper = new Solenoid(1);
    Solenoid FirmlyGraspIt = new Solenoid(2);
    CANJaguar.ControlMode controlMode = CANJaguar.ControlMode.kVoltage; //Voltage drive   
    CANJaguar.NeutralMode neutralMode = CANJaguar.NeutralMode.kBrake;
    final CANJaguar.PositionReference positionReference = CANJaguar.PositionReference.kQuadEncoder;
    final CANJaguar.SpeedReference speedReference = CANJaguar.SpeedReference.kQuadEncoder;
    OI oi;
    double go = 10.0;
    double stop = 0.0;
    double max = 4.9;
    double min = -3.4;
    double volt1 = 0;
    double volt2 = 0;
    private DigitalInput bottomleftlaser = new DigitalInput(1);
    private DigitalInput topleftlaser = new DigitalInput(2);
    private DigitalInput bottomrightlaser = new DigitalInput(3);
    private DigitalInput toprightlaser = new DigitalInput(4);
    public double leftFix;
    public double rightFix;
    public double maxLeft;
    public double maxRight;
    public double toleranceRange = .023; //tolerance for alignment

    public GravitySucks(OI oi) {
        this.oi = oi;
        initBeavis();
        initButthead();
    }

    public boolean testValue() {
        return !bottomleftlaser.get();

    }

    public boolean atBottom() {
        if (!bottomleftlaser.get() && !bottomrightlaser.get()) {
            return true;
        } else {
            return false;
        }
    }

    public void testPos() {
        try {
            System.out.println(-Beavis.getPosition());
            System.out.println(Butthead.getPosition());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public boolean leftAtBottom() {
        return (!bottomleftlaser.get());
    }

    public boolean rightAtBottom() {
        return (!bottomrightlaser.get());
    }

    public boolean atTop() {
        if (!topleftlaser.get() && !toprightlaser.get()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean leftAtTop() {
        if (!topleftlaser.get()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean rightAtTop() {
        if (!toprightlaser.get()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean alignmentCheck() {
        if (!topleftlaser.get() && !toprightlaser.get()) {
            return true;
        } else {
            return false;
        }
    }

    public void initBeavis() {
        try {
            Beavis = new CANJaguar(climbmotor1, controlMode);
            Beavis.configNeutralMode(neutralMode);
            Beavis.setSpeedReference(speedReference);
            Beavis.setPositionReference(positionReference);
            Beavis.configEncoderCodesPerRev(codesPerRev);
            //FrontRightJJ.setPID(kP, kI, kD);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void initButthead() {
        try {
            Butthead = new CANJaguar(climbmotor2, controlMode);
            Butthead.configNeutralMode(neutralMode);
            Butthead.setSpeedReference(speedReference);
            Butthead.setPositionReference(positionReference);
            Butthead.configEncoderCodesPerRev(codesPerRev);
            //FrontRightJJ.setPID(kP, kI, kD);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void enableBeavis() {
        try {
            Beavis.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

    }

    public void enableButthead() {
        try {
            Butthead.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void testRangeAuto() {

        if (leftAtBottom()) {
            try {
                offsetLeft = -Beavis.getPosition();

            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        if (leftAtTop()) {
            try {
                maxLeft = -Beavis.getPosition();

            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        if (rightAtTop()) {
            try {
                maxRight = Butthead.getPosition();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        if (rightAtBottom()) {
            try {
                offsetRight = Butthead.getPosition();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        if (!gotToTop) {
            if (!gotToBottom && !atBottom()) {
                setMotors(-3, 3);
            }
            else if (!gotToBottom && atBottom()) {
                setMotors(stop, stop);
                gotToBottom = true;
            }
            else if (gotToBottom && !atTop()) {
                setMotors(3, -3);
            }
            else if (gotToBottom && atTop()) {
                setMotors(stop, stop);
                gotToTop = true;
            }
            
        } else {
            setMotors(stop, stop);
        }
    }

    public void testRange() {

        if (leftAtBottom()) {
            try {
                offsetLeft = -Beavis.getPosition();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        if (leftAtTop()) {
            try {
                maxLeft = -Beavis.getPosition();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        if (rightAtTop()) {
            try {
                maxRight = Butthead.getPosition();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        if (rightAtBottom()) {
            try {
                offsetRight = Butthead.getPosition();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        magOne = this.oi.getXJoystick().getX();
        magTwo = this.oi.getXJoystick().getRawAxis(4);
        System.out.println("a "+magOne+" "+magTwo+" ");
        setMotors(magOne * 7, magTwo * 7);

    }

    
        public void testRangeElectronicSucks() {

        if (leftAtBottom() && magOne > .5) {
            try {
                offsetLeft = -Beavis.getPosition();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        if (leftAtTop() && magOne < -.5) {
            try {
                maxLeft = -Beavis.getPosition();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        if (rightAtTop() && magTwo > .5) {
            try {
                maxRight = Butthead.getPosition();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        if (rightAtBottom() && magTwo < -.5) {
            try {
                offsetRight = Butthead.getPosition();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        magOne = this.oi.getXJoystick().getX();
        magTwo = this.oi.getXJoystick().getRawAxis(4);
        System.out.println("a "+magOne+" "+magTwo+" ");
        setMotors(magOne * 7, magTwo * 7);

    }

    public void switchDrive() {
        if (this.oi.getLeftTrig()) {
            climb = true;
        } else {
            climb = false;
        }
        if (this.oi.getRightTrig()){
           climb2 = true;
        } else {
           climb2 = false;
        }

    }

    public void DontFall() {
        //add holder thingies with distance values or something
    }

    private double Fixleft() {
        try {
            leftFix = ((-Beavis.getPosition() - offsetLeft)/(maxLeft-offsetLeft))-0;
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        return leftFix;
    }

    private double Fixright() {
        try {
            rightFix = (Butthead.getPosition() - offsetRight)/(maxRight-offsetRight);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        return rightFix;
    }

    public void positionDriveAlign() {
        //try {
            magOne = this.oi.getXJoystick().getRawAxis(5);
          //  System.out.println("volt 1 " + volt1 + " " + "volt 2 " + volt2);
            if (Math.abs(magOne) < deadZone) {
                magOne = 0;
            }
            if (Fixleft() >= 1) {
                if (magOne <= 0) {
                    System.out.println("magOne A" + magOne);
                    magOne = 0;
                } else {
                    //magOne = this.oi.getXJoystick().getRawAxis(5);
                }
            }
            if (Fixleft() <= 0) {
                if (magOne >= 0) {
                    magOne = 0;
                } else {
                   // magOne = this.oi.getXJoystick().getRawAxis(5);
                }
            }
            if (Fixright() >= 1) {
                if (magOne <= 0) {
                    System.out.println("magOne B" + magOne);
                    magOne = 0;
                } else {
                   // magOne = this.oi.getXJoystick().getRawAxis(5);
                }
            }
            if (Fixright() <= 0) {
                if (magOne >= 0) {
                    magOne = 0;
                } else {
                   // magOne = this.oi.getXJoystick().getRawAxis(5);
                }
            }
            if (magOne < 0) {
                if (Math.abs(Fixleft() - Fixright()) <= toleranceRange) {
                    volt1 = magOne * 9;
                    volt2 = magOne*7;
                } else if (Fixleft() - Fixright() < -toleranceRange) {
                    volt1 = magOne * 9;
                    volt2 = stop;
                } else if (Fixleft() - Fixright() > toleranceRange) {
                    volt1 = stop;
                    volt2 = magOne * 9;
                }
            } else if (magOne > 0) {
                if (Math.abs(Fixleft() - Fixright()) <= toleranceRange) {
                    volt1 = magOne * 9; //these values are weird.
                    volt2 = magOne * 9;
                } else if (Fixleft() - Fixright() < -toleranceRange) {
                    volt1 = stop;
                    volt2 = magOne * 9;
                } else if (Fixleft() - Fixright() > toleranceRange) {
                    volt1 = magOne * 9;
                    volt2 = stop;
                }
            } else {
                volt1 = stop;
                volt2 = stop;
            }
            setMotors(-volt1, volt2);
//        } catch (CANTimeoutException ex) {
//            ex.printStackTrace();
//        }
    }

    public void setMotors(double left, double right) {
        try {
            Beavis.setX(left);
            Butthead.setX(right);
            //System.out.println("im trying" + left + " " + right);
                   
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void setMotor(double speed, CANJaguar jag) {
        try {
            jag.setX(speed);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void holdMe() {
        boolean button2 = oi.getLJoystick().getRawButton(4);
        if (button2 && !toggleFGI) {
            solenoidSwitch(switchFGI, FirmlyGraspIt);
        }
        toggleFGI = button2;
        switchFGI = FirmlyGraspIt.get();
    }

    public void tipSimpA() {
        boolean button1 = oi.getRB();
        boolean button2 = oi.getLB();
        if (button1) {
            Tipper.set(true);
        } else if (button2) {
            Tipper.set(false);
        }
    }

    public void tipper() {
        boolean button2 = oi.getLJoystick().getRawButton(2);
        if (button2 && !toggleTipper) {
            solenoidSwitch(switchTipper, Tipper);
        }
        toggleTipper = button2;
        switchTipper = Tipper.get();
    }

    public void solenoidSwitch(boolean GFU, Solenoid sol) {
        GFU = !GFU;
        sol.set(GFU);

    }

    public void testJag(){
        try {
            System.out.println(" Beavis " + Beavis.getPosition());
            System.out.println("Butthead " + Butthead.getPosition());
           
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    // NOTE REDO AUTO TO DO ONE LEVEL AT A TIME AND MANUAL ADJUST FOR TOP AND START
    public void climbLevel() {
        if (count == 10) {
            if (atTop()) {
                if (count < 20) {
                    count++;
                    setMotors(-go, -go);
                }
                if (atBottom()) {
                    setMotors(stop, stop);
                } else if (leftAtBottom()) {
                    setMotors(stop, -go);
                } else if (rightAtBottom()) {
                    setMotors(-go, stop);
                } else {
                    setMotors(-go, -go);
                }
            } else if (leftAtTop()) {
                setMotors(stop, go);
            } else if (rightAtTop()) {
                setMotors(go, stop);
            }


        } else {
            count++;
            setMotors(go, go);
        }
    }
    public void resetCount() {
        count = 0;
    }

    public void resetJag() {
        if (oi.getRJoystick().getRawButton(12)) {
            initBeavis();
            initButthead();

        }
    }

}
