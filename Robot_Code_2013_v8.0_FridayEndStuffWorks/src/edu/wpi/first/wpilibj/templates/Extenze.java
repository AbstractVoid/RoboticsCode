/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import java.util.Timer;

/**
 *
 * @author Programming
 */
public class Extenze {

    OI oi;
    Solenoid extender;
    Solenoid hopper;
    Solenoid shakeWeight;
    Solenoid pressure;
    boolean toggleHopper = false;
    boolean switchHopper = false;
    boolean toggleShooter = false;
    boolean switchShooter = false;
    boolean toggleShakeWeight = false;
    boolean switchShakeWeight = false;
    boolean togglePressure = false;
    boolean switchPressure = false;

    public Extenze(OI oi) {
        this.oi = oi;
        extender = new Solenoid(3);
        hopper = new Solenoid(4);
        shakeWeight = new Solenoid(5);
        pressure = new Solenoid(8); //unused
    }

    public void extendShooter() {
        boolean button = oi.getBack();
        if (button && !toggleShooter) {
            solenoidSwitch(switchShooter, extender);
        }
        toggleShooter = button;
        switchShooter = extender.get();
    }

    public void extendHopper() {
        boolean button3 = oi.getStart();
        if (button3 && !toggleHopper) {
            solenoidSwitch(switchHopper, hopper);
        }
        toggleHopper = button3;
        switchHopper = hopper.get();
    }


    public void shootADisc() {
        boolean button2 = oi.getA();
        if (button2 && !toggleShakeWeight) {
            solenoidSwitch(switchShakeWeight, shakeWeight);
        }
        toggleShakeWeight = button2;
        switchShakeWeight = shakeWeight.get();
    }
    


    public void solenoidSwitch(boolean GFU, Solenoid sol) {
        GFU = !GFU;
        sol.set(GFU);

    }
}
