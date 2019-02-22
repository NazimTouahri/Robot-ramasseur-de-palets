
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.internal.io.SystemSettings;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

/**
 * Classe pour le second scénario , le robot est placé sur une des positions de départ et va chercher un palet situé à une des intersections
 * Le robot est situé du coté le plus proche de la ligne verte
 * @author simon
 *
 */
public class P2DroitePuisVert {

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
		ev3.ouverture1();
		P2();	

	}
	
	
	//Ceci sert a faire avancer le robot jusqu'a la ligne blanche , deposer le palet , et ouvrir ses pinces
	public static void ramenePalet() {
		// le robot avance jusqu'a la ligne blanche
		ev3.avancer1();
		while(!color2.IsWhite())		
		{ev3.avancer1();}
		// s'arrete
		ev3.arreter1();   
		// ouvre ses pinces
		ev3.ouverture1();
	}	
	
	/**
	 * Permet au robot d'aller chercher un palet sur une des intersections
	 */
	public static void P2() {
		
		
		
		/*
		 * le robot va avancer un petit peu , pivoter a 90 degré , il ira ensuite jusqu'au mur 
		 * ensuite fera une rotation a 90 degré dans lautre sens pour se mettre sur la ligne verte 
		 */
		ev3.avancer1();
		Delay.msDelay(400);			// on fait un petit peu avancer le robot
		ev3.arreter1();
		pilot.rotate(-90);			// il tourne a 90 degré vers la droite
		ev3.avancer1();
		while(!ultrasonic.TestUltrasonic2(0.13f)) {ev3.avancer1();}		// il avance jusqu'au mur
		ev3.arreter1();
		pilot.rotate(90);			// il tourne a 90 degré vers la gauche
		ev3.avancer1();
		while(!color2.IsGreen()) {ev3.avancer1();}		// avance jusqu'a la ligne verte
		ev3.arreter1();
			
		
		/*
		 *  Les prochaines lignes servent a faire tourner le robot sur la ligne verte
		 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
		 */
		
			ev3.avancer1();
			Delay.msDelay(38);			// on depasse legerement la ligne verte
			ev3.arreter1();

			Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
			Motor.B.forward();			
			Motor.B.setSpeed(150);	
			while(!color2.IsGreen()) {Motor.B.forward();}	// Le robot tourne jusqu'au vert
			ev3.arreter1();

			Motor.B.setSpeed(100);		// on met la vitesse du moteur B a 100
			Motor.B.forward();
			Motor.B.setSpeed(100);
			while(!color2.IsGrey()) {Motor.B.forward();} // Le robot tourne jusqu'au gris 
			ev3.arreter1();
			Motor.B.setSpeed(300);
			
			/*
			 * Le robot avancera sur la ligne verte tant qu'il ne rencontrera ni un palet ni un mur
			 * si il rencontre un palet , il le ramenera derriere la ligne blanche
			 * si il rencontre un mur , il passe la suite et continu son chemin
			 */
			ev3.HorsLigne();
			while(!ultrasonic.TestUltrasonic2(0.15f))
			{
				// les deux if permettent le suivi de ligne 
				if(color2.IsGreen()) ev3.SurLigne();
				if(color2.IsGrey()) ev3.HorsLigne();
				
				// si il rencontre un palet le robot va le ramener 
				if(touch.isPressed()) {
				ev3.arreter1();
				ev3.fermeture1();
				pilot.rotate(-90);
				ramenePalet();
				System.exit(0);}
			}
			
		/*
		 * le robot va pivoter a 90 degré , il ira ensuite jusqu'a la ligne noir 
		 * ensuite fera une rotation a 90 degré dans lautre sens pour se mettre sur la ligne noir
		 */
			ev3.arreter1();
			pilot.rotate(-90);    // il tourne a 90 degré
			ev3.avancer1();
			while(!color2.IsBlack()) {ev3.avancer1();} 	// avance jusqu'a la ligne noir
			ev3.arreter1();
			ev3.avancer1();
			Delay.msDelay(70);		// la depasse un peu
			ev3.arreter1();
			
			/*
			 *  Les prochaines lignes servent a faire tourner le robot sur la ligne noir
			 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
			 */
			
			Motor.A.setSpeed(150);  // on met la vitesse du moteur a 150 
			Motor.A.forward();
			Motor.A.setSpeed(150);
			while(!color2.IsBlack()) {Motor.A.forward();}	// le robot tourne jusqu'au noir
			ev3.arreter1();

			Motor.A.setSpeed(50);			// on ralentis le moteur A a 50
			Motor.A.forward();
			Motor.A.setSpeed(50);
			while(!color2.IsGrey()) {Motor.A.forward();}		// le robot tourne jusqu'au gris
			ev3.arreter1();
			Motor.A.setSpeed(300);
			
			/*
			 * Le robot avancera sur la ligne noir tant qu'il ne rencontrera ni un palet ni un mur
			 * si il rencontre un palet , il le ramenera derriere la ligne blanche
			 * si il rencontre un mur , il passe la suite et continu son chemin
			 */

			ev3.SurLigne();
			while(!ultrasonic.TestUltrasonic2(0.15f))
			{
				// les deux if permettent de suivre la ligne noire
				if(color2.IsBlack()) ev3.HorsLigne();
				if(color2.IsGrey()) ev3.SurLigne();
				
				//si il touche un palet , il le ramene derniere la ligne blanche
				if(touch.isPressed()) {
					ev3.arreter1();
					ev3.fermeture1();
					pilot.rotate(90);
					ramenePalet();
					System.exit(0);} }
				
			/*
			 * le robot va pivoter a 90 degré , il ira ensuite jusqu'a la ligne bleue 
			 * ensuite fera une rotation a 90 degré dans lautre sens pour se mettre sur la ligne bleue
			 */
				ev3.arreter1();
				pilot.rotate(90);		// il tourne a 90 degré vers la gauche 
				ev3.avancer1();
				while(!color2.IsBlue()) {ev3.avancer1();}  // il avance jusqu'au bleu
				ev3.arreter1();
				ev3.avancer1();
				Delay.msDelay(38);		// il depasse un peu la ligne bleue
				ev3.arreter1();
				
				/*
				 *  Les prochaines lignes servent a faire tourner le robot sur la ligne bleue
				 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
				 */
				
				Motor.B.setSpeed(150);		// on met la vitesse du moteur a 150 
				Motor.B.setSpeed(150);
				while(!color2.IsBlue()) {Motor.B.forward();}  // il tourne jusq'au bleu
				ev3.arreter1();

				Motor.B.setSpeed(50);		// on met la vitesse du moteur a 50 
				Motor.B.forward();
				Motor.B.setSpeed(50);
				while(!color2.IsGrey()) {Motor.B.forward();}	// il tourne jusq'au gris
				ev3.arreter1();
				Motor.B.setSpeed(300);
				
				/*
				 * Le robot avancera sur la ligne bleue tant qu'il ne rencontrera ni un palet ni un mur
				 * si il rencontre un palet , il le ramenera derriere la ligne blanche
				 * si il rencontre un mur , il passe la suite et continu son chemin
				 */

				ev3.HorsLigne();
				while(!ultrasonic.TestUltrasonic2(0.15f))
				{
					// les deux if permettent de suivre la ligne bleue
					if(color2.IsBlue()) ev3.SurLigne();
					if(color2.IsGrey()) ev3.HorsLigne();
					
					//si il touche un palet , il le ramene derniere la ligne blanche
					if(touch.isPressed()) {
						ev3.arreter1();
						ev3.fermeture1();
						pilot.rotate(-90);
						ramenePalet();
						System.exit(0);}
					
				}
				
	}
}
				
			

