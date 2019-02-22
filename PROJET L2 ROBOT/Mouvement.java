



import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.Touch;
import lejos.utility.Delay;

/**
 * Classe ou sont tous les mouvements pour le robot
 * @author simon
 *
 */

public class Mouvement {

	
	/**
	 * permet au robot de dériver sur la gauche
	 */
	
	//Cette classe permettra de faire dériver le robot vers la gauche 

	public void SurLigne() {
		Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A }); // on synchronise les deux moteurs ensemble
		Motor.B.startSynchronization();
		Motor.A.setSpeed(360);   // le moteur de droite ira moins vite que le gauche 
		Motor.B.setSpeed(400);	// Ce qui permet au robot de dériver sur la gauche
		Motor.B.forward();		// le robot avance
		Motor.A.forward();
		Motor.B.endSynchronization(); // on termine la synchronisation 
	} 
	
	 /**
	  * permet au robot de deriver sur la droite
	  */
	
	//Cette classe permettra de faire dériver le robot vers la droite 
	public void HorsLigne() {
		Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A }); // on synchronise les deux moteurs ensemble
		Motor.B.startSynchronization();
		  Motor.A.setSpeed(400); 	// le moteur de droite ira plus vite que le gauche 
		  Motor.B.setSpeed(360); 	// Ce qui permet au robot de dériver sur la droite
		Motor.B.forward();			//  le robot avance
		Motor.A.forward();
		Motor.B.endSynchronization(); // on termine la synchronisation 
	} 
	
	
	/**
	 * Ouvre les pinces
	 */
	
	// Cette classe va permettre de faire ouvrir les pinces 
	public void ouverture1() {
		
		Motor.C.setSpeed(1000); // on met la vitesse de 1000 au moteur
		Motor.C.forward();		// on ouvre les pinces
		Delay.msDelay(700);		// pendant un delai de 700 ms
		Motor.C.stop();			// on arrete le moteur 
		
	}
	
	/**
	 * Ouvre les pinces avec un axe plus grand
	 */
	
	// Cette classe va permettre de faire ouvrir les pinces 
	public void ouverture1Boost() {
		
		Motor.C.setSpeed(1500); // on met la vitesse de 1500 au moteur
		Motor.C.forward();		// on ouvre les pinces
		Delay.msDelay(1000);		// pendant un delai de 1000 ms
		Motor.C.stop();			// on arrete le moteur 
		
	}
	
	/**
	 * Ferme les pinces
	 */
	
	// Cette classe va permettre de faire fermer les pinces 
	public void fermeture1() {
		Motor.C.setSpeed(1000); // on met la vitesse de 1000 au moteur
		Motor.C.backward();		// on ferme les pinces
		Delay.msDelay(700);		// pendant un delai de 700 ms
	    Motor.C.stop();			// on arrete le moteur 
	}
	
	/**
	 * Ferme les pinces avec un axe plus grand
	 */
	
	// Cette classe va permettre de faire fermer les pinces 
	public void fermeture1Boost() {
		Motor.C.setSpeed(1500); // on met la vitesse de 1500 au moteur
		Motor.C.backward();		// on ferme les pinces
		Delay.msDelay(1000);		// pendant un delai de 1000 ms
	    Motor.C.stop();			// on arrete le moteur 
	}
	
	/**
	 * Fait avancer le robot
	 */
	
	//Cette classe permet de faire avancer le robot
	public void avancer1() {
		Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A }); // on synchronise les deux moteurs ensemble
		Motor.B.startSynchronization();
		  Motor.A.setSpeed(400);  // on met les deux moteurs a la meme vitesse
		  Motor.B.setSpeed(400);
		Motor.B.forward();		// on le fait avancer
		Motor.A.forward();
		Motor.B.endSynchronization();		// on arrete la synchronisation
	}
	
	/**
	 * Fair reculer le robot
	 */
	
	//Cette classe permet de faire reculer le robot
	public  void Reculer() {
		Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A }); // on synchronise les deux moteurs ensemble
		Motor.B.startSynchronization();
		  Motor.A.setSpeed(400);		// on met les deux moteurs a la meme vitesse
		  Motor.B.setSpeed(400);
		Motor.B.backward(); 		// on le fait reculer
		Motor.A.backward();
		Motor.B.endSynchronization();		// on arrete la synchronisation
	}
	
	/**
	 * Arrete le robot
	 */
	
	//Cette classe permet de faire arreter le robot
	public void arreter1() {
		Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A }); // on synchronise les deux moteurs ensemble
		Motor.B.startSynchronization();
		Motor.B.stop();			// on arrete les deux moteurs
		Motor.A.stop();
		Motor.B.endSynchronization();		// on arrete la synchronisation
		Delay.msDelay(100);				// on laisse un petit delai de 100 ms pour un peu plus de fluidité
	}
	
	/**
	 * Fait avancer le robot plus vite
	 */
	//Cette classe permet de faire avancer le robot plus vite 
	public void avancer1Boost() {
		Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A }); // on synchronise les deux moteurs ensemble
		Motor.B.startSynchronization();
		  Motor.A.setSpeed(800);		// on met les deux moteurs a la meme vitesse
		  Motor.B.setSpeed(800);
		Motor.B.forward();		// on le fait avancer
		Motor.A.forward();
		Motor.B.endSynchronization(); // on arrete la synchronisation
	}
	
}

