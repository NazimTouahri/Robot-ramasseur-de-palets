package pack;

import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

public class Ultrasonic extends AbstractFilter {
 
	float[] sample;
	
	//_____________PERMET LE BON FONCTIONNEMENT DU CAPTEUR A ULTRASON_____________//
	
	public Ultrasonic(SampleProvider source) {
		super(source);
		// TODO Auto-generated constructor stub
		
		sample = new float[sampleSize];
	}

	//_____________MET A JOUR LE TABLEAU DE VALEUR_____________//
	public float distance() {
		super.fetchSample(sample, 0);
		return sample[0];
	}
	
}

