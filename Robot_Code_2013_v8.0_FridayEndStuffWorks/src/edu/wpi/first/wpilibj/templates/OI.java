/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import java.lang.Math;

/**
 *
 * @author Gaylord Steambath
 */
public final class OI {

    private Joystick lJoystick = new Joystick(1);
    private Joystick rJoystick = new Joystick(2);
    private Joystick xJoystick = new Joystick(3);

    //curently using default methods instead of OI class methods...not sure why ill look into restructuring it probably not though
    public Joystick getLJoystick() {
        return lJoystick;
    }

    public Joystick getRJoystick() {
        return rJoystick;
    }

    public Joystick getXJoystick() {
        return xJoystick;
    }

    public boolean getA() {
        return xJoystick.getRawButton(1);
    }

    public boolean getRightTrig() {
        if (xJoystick.getZ() > .5) {
            return true;
        } else {
            return false;
        } 
    }

    public boolean getLeftTrig() {
        if (xJoystick.getZ() < -.5) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getB() {
        return xJoystick.getRawButton(2);
    }

    public boolean getX() {
        return xJoystick.getRawButton(3);
    }

    public boolean getY() {
        return xJoystick.getRawButton(4);
    }

    public boolean getLB() {
        return xJoystick.getRawButton(5);
    }

    public boolean getRB() {
        return xJoystick.getRawButton(6);
    }

    public boolean getBack() {
        return xJoystick.getRawButton(7);
    }

    public boolean getStart() {
        return xJoystick.getRawButton(8);
    }
}
