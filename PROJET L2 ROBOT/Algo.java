package pack;




import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.Touch;
import lejos.utility.Delay;


public class Algo {

	//______________OUVRIR LES PINCES______________//
	public void ouverture1() {
		Motor.C.forward();
		Delay.msDelay(1350);
		Motor.C.stop();
	}
	
	//______________OUVRIR LES PINCES______________//
	public void fermeture1() {
		Motor.C.backward();
		Delay.msDelay(1350);
	    Motor.C.stop();
	}
	
	//______________FAIRE AVANCER LE ROBOT______________//
	public void avancer1() {
		Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A });
		Motor.B.startSynchronization();
		  Motor.A.setSpeed(300);
		  Motor.B.setSpeed(300);
		Motor.B.forward();
		Motor.A.forward();
		Motor.B.endSynchronization();
	}
	

	//______________FAIRE RECULER LE ROBOT______________//
	public  void Reculer() {
		Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A });
		Motor.B.startSynchronization();
		  Motor.A.setSpeed(200);
		  Motor.B.setSpeed(200);
		Motor.B.backward();
		Motor.A.backward();
		Motor.B.endSynchronization();
	}
	

//______________ARRETER LE ROBOT______________//
	public void arreter1() {
		Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A });
		Motor.B.startSynchronization();
		Motor.B.stop();
		Motor.A.stop();
		Motor.B.endSynchronization();
		Delay.msDelay(100);
	}
	
			
//______________FAIRE UN DEMI TOUR AU ROBOT______________//	
	public void demi() {
	Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A });
	Motor.B.startSynchronization();
	Motor.B.forward();
	Motor.A.backward();
	Delay.msDelay(1375);
	Motor.B.endSynchronization();
}
	
}

