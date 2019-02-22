


import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

public class UltrasonicDistance {
	// on initialise tout ce qui sera utile lors des methodes
	DifferentialPilot pilot;
	float closestAngle = -1.0f;
	float closestDistance = 1000.0f	;	
	private boolean bool;
	private Port ultrasonicSensorPort = SensorPort.S3;
	private EV3UltrasonicSensor ultrasonicSensor;
	private SampleProvider sampleProvider;
	private int sampleSize;
	float[] sample;
	boolean palet2 = false;
	int cmpt =0;
	/**
	 * Constructeur
	 */
	
	//Constructeur 
	
	public UltrasonicDistance() {
		ultrasonicSensor = new EV3UltrasonicSensor(ultrasonicSensorPort); 
		sampleProvider = ultrasonicSensor.getDistanceMode();		//On utilise le capteur en mode Distance
		sampleSize = sampleProvider.sampleSize();					
		sample = new float[sampleSize];								// On creer un tableau ou ranger les distances mesurées
		DifferentialPilot pilot = new DifferentialPilot(1.968f,4.35f,Motor.A,Motor.B);
	}

	/**
	 * Permet de creer un tableau qui sera mis  jour
	 * @return un tableau ou seront rangé les distances mesurées
	 */
	
	//cela sert a prendre des mesures
public float getSampleUltrasonic() {
		
		float[] sample = new float[sampleSize];		// On creer un tableau ou ranger les distances mesurées
		
	
		sampleProvider.fetchSample(sample, 0);		 // permet de renouveler les mesures
		return sample[0];
	}


	
	/**
	 * Permet de faire une rotation et detecter l'objet le plus proche ainsi que son angle pour ensuite s'y diriger. 
	 * Le robot parcourera ensuite 80% de cette distance , lancera un scan d'une portée de 20% de la distance
	 * si le robot detecte quelque chose c'est que c'était un mur , sinon c'est que c'est un palet
	 * @param degre axe de rotation du robot
	 * @param taille portée du scan
	 */


	public void UltrasonicClosest(float degre,float taille) {
		float closestAngle = -1.0f;										// On initialise l'angle minimum a -1.0 degré
		float closestDistance = 1000.0f	;								// On initialise la distane minimum a 1000.0
		DifferentialPilot pilot = new DifferentialPilot(1.968f,4.35f,Motor.A,Motor.B); 	// On initialise le differentialPilot qui sera utile pour les mouvements
		palet2= false;													// On initialise le boolean a faux
		pilot.reset();													
		pilot.setAngularSpeed(45);										// Regle la vitesse de rotation a 45
		pilot.rotate(degre,true);										// Le robot tournera d'autant de degré qu'il sera indiquer dans l'appel de la fonction
		
		while(pilot.isMoving()) {										// tant que le robot est ne mouvement
			Delay.msDelay(10);											// on laisse un petit delai de 10 ms
			
	    ///////////////////ON RENTRE DANS LE IF /////////////////////
			if(getSampleUltrasonic() <closestDistance && getSampleUltrasonic()<taille) { 
				/*
				 * Des qu'une valeur sera plus petite que la plus petite valeurs ou que la taille indiqué dans l'apelle de la fonction
				 * Cette valeur sera alors la nouvelle plus petite valeurs
				 * Et son angle sera sauvgardé
				 */
				closestDistance=getSampleUltrasonic();			//remplacement de l'ancienne plus petite valeurs par la nouvelle				
				closestAngle=pilot.getAngleIncrement();			//remplacement de l'ancienne plus petite valeurs par la nouvelle
																						}
								}

		pilot.setAngularSpeed(50);
		if(closestDistance>500) {
			
			/*
			 * Ceci sert a faire que si rien a été reperer , le robot reprendra son axe d'origine
			 */
			pilot.rotate(-degre);	// robot se remet dans son axe
		}
		//////////////ON PASSE AU ELSE///////////////
		else {
			/*
			 * on prend le cas 360 et 180 car on ne fera aucun autre appel pour la variable dregré
			 * Ce qui changera sera l'axe pour trouvé l'objet reperer
			 * qui devra etre ajuster selon la distance
			 */
				{if(degre==360) 
					{pilot.rotate(closestAngle-((degre*0.99)+(closestDistance*2.65))); // Le robot se met dans l'axe de ce qu'il a reperer
					pilot.travel((closestDistance*75)/2.54);						// le robot fait 75% de la distance qu'il a retenu 
					if(!TestUltrasonic2(closestDistance*0.25f)) {pilot.travel((closestDistance*25)/2.54); palet2 =true ;}  
					/* si il voit rien a moins de 25% de la distance qu'il a retenu , le robot fera les 25% restant sinon il reste sur place
					*  si il parcours les 25% restant c'est que c'est un palet , et donc la variable devient vraie
					*/
					}
		
				if(degre==180) 
					{pilot.rotate(closestAngle-((degre*0.99)+(closestDistance)));// Le robot se met dans l'axe de ce qu'il a reperer
					pilot.travel((closestDistance*75)/2.54);					// le robot fait 75% de la distance qu'il a retenu 
					if(!TestUltrasonic2(closestDistance*0.25f)) {pilot.travel((closestDistance*25)/2.54); palet2 =true ;}
					/* si il voit rien a moins de 25% de la distance qu'il a retenu , le robot fera les 25% restant sinon il reste sur place
					*  si il parcours les 25% restant c'est que c'est un palet , et donc la variable devient vraie
					*/
					}
				}	
			}
	}

	
	/**
	 * Permet de savoir si le robot detecte quelque chose jusqu'a une portée precise
	 * @param distancemini distance jusqu'à laquelle le robot cherchera si il detecte quelque chose ou non
	 * @return vrai(true)  si il detect quelque chose ou faux (false) sinon
	 */
			
	public boolean TestUltrasonic2(float distancemini) {
		boolean Detec = false;  // on initialise a faux 
		if (getSampleUltrasonic() <distancemini) {
			Detec = true;		// si le robot detecte quelque chose a moins de la distance indiqué dans l'apelle de la fonction il renvoie vrai
		} else	
			Detec = false;		// sinon il renvoi faux
		return Detec;			// on renvoie alors si le robot a detecter quelque chose ou non
	}
	
	
	
	/**
	 * Permet de savoir si il a un palet
	 * @return palet2 soit un boolean et donc vrai (true) si il a un palet ou faux (false) sinon
	 */

	public boolean hasPalet() {
		return palet2;  // permet de renvoyer si c'est un palet ou non qui a été reperer 
		
	}
		
}


