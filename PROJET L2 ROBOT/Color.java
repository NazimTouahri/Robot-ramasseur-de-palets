package pack;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Color {
	private SampleProvider colorProvider;
	private EV3ColorSensor colorSensor;
	private float[] colorSample;
	Algo ev3 = new Algo();
	String color = "nul";
	Boolean bool = false;

//_______MET EN ROUTE LE CAPTEUR DE COULEUR_______//
	
	public Color() {
		Port s2 = LocalEV3.get().getPort("S2");
		colorSensor = new EV3ColorSensor(s2);
		
		colorProvider = colorSensor.getRGBMode();
		
		colorSample = new float[colorProvider.sampleSize()];
	
		
		
		colorProvider.fetchSample(colorSample, 0);
	
	}
	

//_____________FONCTION RETOURNANT VRAI SI LA COULEUR DETECTEE EST LE BLANC_____________//

	public Boolean IsWhite() {
		colorProvider.fetchSample(colorSample, 0);
		bool = false;
		while(!bool) {
			colorProvider.fetchSample(colorSample, 0);
		Delay.msDelay(0);
		
		if(((colorSample[0]<0.6f) && (colorSample[0]>0.1f ))
				&& ((colorSample[1]>0.1f) && (colorSample[1]<0.4f ))
				&& ((colorSample[2]>0) && (colorSample[2]<0.25f))) {
		
		bool=true;}
		}
		return bool;
		}
	

//_____________FONCTION RETOURNANT VRAI SI LA COULEUR DETECTEE EST LE GRIS_____________//


	public Boolean IsGrey() {
		colorProvider.fetchSample(colorSample, 0);
		bool = false;
		while(!bool) {
			colorProvider.fetchSample(colorSample, 0);
		Delay.msDelay(0);
		
		if(((colorSample[0]>0.05 ) && (colorSample[0]<0.1))
				&& ((colorSample[1]>0) && (colorSample[1]<0.3f ))
				&& ((colorSample[2]>0) && (colorSample[2]<0.1))) {
		
		bool=true;}
		}
		return bool;
		}
	

//_____________FONCTION RETOURNANT VRAI SI LA COULEUR DETECTEE EST LE NOIR_____________//


	public Boolean IsBlack() {
		colorProvider.fetchSample(colorSample, 0);
		bool = false;
		while(!bool) {
			colorProvider.fetchSample(colorSample, 0);
		Delay.msDelay(0);
		
		if(((colorSample[0]>0 ) && (colorSample[0]<0.03f))
				&& ((colorSample[1]>0) && (colorSample[1]<0.035f ))
				&& ((colorSample[2]>0) && (colorSample[2]<0.03f))) {
		
		bool=true;}
		}
		return bool;
		}

//_____________FONCTION RETOURNANT VRAI SI LA COULEUR DETECTEE EST LE BLEU_____________//


		
	public Boolean IsBlue() {
		colorProvider.fetchSample(colorSample, 0);
		bool = false;
		while(!bool) {
			colorProvider.fetchSample(colorSample, 0);
		Delay.msDelay(0);
		
		if(((colorSample[0]>0.01f ) && (colorSample[0]<0.04f))
				&& ((colorSample[1]>0.07f) && (colorSample[1]<0.2f ))
				&& ((colorSample[2]>0) && (colorSample[2]<0.11f))) {
		
		bool=true;}
		}
		return bool;
		}
	

//_____________FONCTION RETOURNANT VRAI SI LA COULEUR DETECTEE EST LE ROUGE_____________//


	public Boolean IsRed() {
		colorProvider.fetchSample(colorSample, 0);
		bool = false;
		while(!bool) {
			colorProvider.fetchSample(colorSample, 0);
		Delay.msDelay(0);
		
		if(((colorSample[0]>0.05f ) && (colorSample[0]<0.14f))
				&& ((colorSample[1]>0) && (colorSample[1]<0.07f ))
				&& ((colorSample[2]>0) && (colorSample[2]<0.03))) {
		
		bool=true;}
		}
		return bool;
		}


//_____________FONCTION RETOURNANT VRAI SI LA COULEUR DETECTEE EST LE VERT_____________//



	public Boolean IsGreen() {
		colorProvider.fetchSample(colorSample, 0);
		bool = false;
		while(!bool) {
			colorProvider.fetchSample(colorSample, 0);
		Delay.msDelay(0);
		
		if(((colorSample[0]>0 ) && (colorSample[0]<0.09f))
				&& ((colorSample[1]>0.11f) && (colorSample[1]<0.2f ))
				&& ((colorSample[2]>0) && (colorSample[2]<0.07f))) {
		bool=true;}
		}
		return bool;
		}


//_____________FONCTION RETOURNANT VRAI SI LA COULEUR DETECTEE EST LE JAUNE_____________//


	public Boolean IsYellow() {
		colorProvider.fetchSample(colorSample, 0);
		bool = false;
		while(!bool) {
			colorProvider.fetchSample(colorSample, 0);
		Delay.msDelay(0);
	
		if(((colorSample[0]>0.1f ) && (colorSample[0]<0.2f))
				&& ((colorSample[1]>0.1f) && (colorSample[1]<0.3f ))
				&& ((colorSample[2]>0) && (colorSample[2]<0.05f))) {
		bool=true;}
		}
		return bool;
		}
	

	
}
	