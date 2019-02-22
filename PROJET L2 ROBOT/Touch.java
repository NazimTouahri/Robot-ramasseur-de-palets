package pack;


	import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
	import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
	import lejos.robotics.SampleProvider;

	public class Touch  {
		// Attributs
		private Port touchSensorPort = SensorPort.S1;
		private EV3TouchSensor touchSensor;
		private SampleProvider sampleProvider;
		private float[] touchSample;
		private int sampleSize;
		Algo ev3 = new Algo();

		// Constructeur
		public Touch() {
			// Initialiser le capteur de contact
			Port s1 = LocalEV3.get().getPort("S1");
			touchSensor = new EV3TouchSensor(s1);
		
			sampleProvider = touchSensor.getTouchMode();
			touchSample = new float[sampleProvider.sampleSize()];
			sampleSize = sampleProvider.sampleSize();
			sampleProvider.fetchSample(touchSample, 0);
		}

		
		
	
		public boolean isPressed() {
			sampleProvider.fetchSample(touchSample, 0);
			boolean palet = false;
			if (touchSample[0] == 0.0f) {
				palet = true;
			} else
				palet = false;
			return palet;
		
			
		}
	}
	
	