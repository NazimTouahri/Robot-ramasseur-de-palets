package pack;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

public class UltrasonicDistance {
	

//______________FONCTION QUI PERMET AU ROBOT DE RECULER ET DE SE RETOURNER SI IL DETECTE UN OBSTACTE A MOINS DE 30 CM ______________//

public void TestUltrasonic() {
	

//_________PERMET D'UTILISER LE CAPTEUR A ULTRASON_________//

	Brick b = BrickFinder.getDefault();
	Port s3 = b.getPort("S3");
	EV3UltrasonicSensor us = new EV3UltrasonicSensor(s3);
	Ultrasonic ultrasonic = new Ultrasonic(us.getMode("Distance"));
	
	while(true) {
		Delay.msDelay(2);

		 
		float distance = ultrasonic.distance();


		if(distance < 0.3) {
//_______LE ROBOT RECULE_______//
			Motor.A.backward();
			Motor.B.backward();
			Delay.msDelay(1000);

			
//_______LE ROBOT SE TOURNE_______//					 			Motor.B.forward();
			Delay.msDelay(1375);
			
		} else if (distance > 0.4){
			
			Motor.A.forward();
			Motor.B.forward();
			
		
		} 

		//______________PROGRAMME SE FINI SI QUELQU'UN APPUIE SUR LE BUTTON ESCAPE______________//
		if( Button.ESCAPE.isDown()) {
			Motor.A.stop();
			Motor.B.stop();
			us.close();
			System.exit(0);
			
			
		}
		
	}
}

}
