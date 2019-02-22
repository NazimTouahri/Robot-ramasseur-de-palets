package pack;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

import lejos.utility.Delay;


/**
 * La classe permet de setter toutes les couleurs 
 * @author ASUS
 *
 */
public class SetColor {
	static Mouvement ev3 = new Mouvement();
	static Touchh touch = new Touchh();
	static Color2 color2 = new Color2();
	static UltrasonicDistance ultrasonic = new UltrasonicDistance();
	 static DifferentialPilot pilot = new DifferentialPilot(1.968f,4.35f,Motor.A,Motor.B);
	 

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("SetColor");
		Button.ENTER.waitForPressAndRelease();
		color2.calibrerCouleur();
	}

}
