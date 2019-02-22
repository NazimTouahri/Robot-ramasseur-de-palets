

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
       /**
        * Classe pour le capteur de pression
        * @author simon
        *
        */
        public class Touchh  {
                // Attributs
                private Port touchSensorPort = SensorPort.S1;
                private EV3TouchSensor touchSensor;
                private SampleProvider sampleProvider;
                private float[] touchSample;
                private int sampleSize;
                boolean palet;
                Mouvement ev3 = new Mouvement();
                // Constructeur
                
                /**
                 * constructeur
                 */
                
                // Constructeur  
                public Touchh() {
                        // Initialiser le capteur de contact
                        Port s1 = LocalEV3.get().getPort("S1");		// permet de dire que le capteur de pression est connecté au port numéro 1
                        touchSensor = new EV3TouchSensor(s1);
                
                        sampleProvider = touchSensor.getTouchMode(); 
                        touchSample = new float[sampleProvider.sampleSize()];
                        sampleSize = sampleProvider.sampleSize();
                        sampleProvider.fetchSample(touchSample, 0);	// permet de mettre a jour les mesures
                }
                
                
        /**
         * Permet de savoir si il y a une pression et donc si le robot touche un palet
         * @return vrai(true) si il y a une pression ou faux(false) sinon
         */
                public boolean isPressed() {
                        sampleProvider.fetchSample(touchSample, 0);
                       
                        if (touchSample[0] == 0.0f) {
                                palet = false;		//	si le robot ne touche rien il renvoie faux
                        } else
                                palet = true;		// si il touche quelque chose il renvoie vrai
                        return palet;
                
                        
                }
        }
			
