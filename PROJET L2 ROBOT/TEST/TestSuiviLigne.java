
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;


public class TestSuiviLigne {

	static Mouvement ev3 = new Mouvement();
	static Touchh touch = new Touchh();
	static Color2 color2 = new Color2();
	static UltrasonicDistance ultrasonic = new UltrasonicDistance();
	 static DifferentialPilot pilot = new DifferentialPilot(1.968f,4.35f,Motor.A,Motor.B);
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		color2.readColor();  // permet de lire les mesures pour les couleurs qui on �t� precedement faite
		
		// les quelques lignes suivantes servent a empecher le robot de faire des mouvement hasardeux au lancement du code
		ev3.avancer1();
		Delay.msDelay(0);
		ev3.arreter1(); 
		
		ev3.avancer1();
		Delay.msDelay(0);
		ev3.arreter1();
		System.out.println("PRET");
		Button.ENTER.waitForPressAndRelease();
		Test();
	}
	
	/**
	 * Permet de test le suivi de ligne
	 */
	public static void Test() {
		color2.readColor();
		ev3.avancer1();
		ev3.SurLigne();
		while(!ultrasonic.TestUltrasonic2(0.135f))
		{
			// les deux if permettent le suivi de ligne 
		if(color2.IsBlack()) ev3.HorsLigne();
		if(color2.IsGrey()) ev3.SurLigne();
		if(color2.IsWhite()) {ev3.arreter1(); break; }
		
	}
		ev3.arreter1();
	}
	
}