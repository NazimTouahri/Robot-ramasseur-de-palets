
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;


public class TestDesMouvements {

	static Mouvement ev3 = new Mouvement();
	static Touchh touch = new Touchh();
	static Color2 color2 = new Color2();
	static UltrasonicDistance ultrasonic = new UltrasonicDistance();
	 static DifferentialPilot pilot = new DifferentialPilot(1.968f,4.35f,Motor.A,Motor.B);
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		color2.readColor();  // permet de lire les mesures pour les couleurs qui on été precedement faite
		
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
	 * Permet de test les mouvement 
	 */
	public static void Test() {
		ev3.avancer1();		//avance
		Delay.msDelay(800);
		ev3.arreter1();		//avance boost
		ev3.avancer1Boost();
		Delay.msDelay(500);
		ev3.arreter1();
		ev3.ouverture1();		//ouvre pince
		ev3.fermeture1();		//ferme pince
		ev3.ouverture1Boost();	//ouvre pince plus grand
		ev3.fermeture1Boost();	//ferme pince plus grand
		ev3.Reculer();			//recule
		Delay.msDelay(500);
		ev3.arreter1();
	}
	}