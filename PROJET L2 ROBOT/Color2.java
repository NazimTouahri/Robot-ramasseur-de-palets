package pack;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.Button;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
//Packages à importer afin d'utiliser l'objet File
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Classe qui permet de stockées les valeurs enregistrées par le capteur de couleur dans des tableaux et creer un fichier pour les enregistrer
 * @author ASUS
 *
 */

public class Color2 {
	// Déclaration des attributs 
	   float[] Red=new float[3];
	   float[] Green=new float[3];
	   float[] Blue=new float[3];
	   float[] Black=new float[3];
	   float[] Yellow=new float[3];
	   float[] White=new float[3];
	   float[] Grey=new float[3];

		
	private Port colorSensorPort = SensorPort.S2;
	private EV3ColorSensor colorSensor;
	private SampleProvider sampleProvider;
	private int sampleSize;

	// Constructeur
	public Color2() {
		// Initialiser le capteur de couleurs
		colorSensor = new EV3ColorSensor(colorSensorPort);
		sampleProvider = colorSensor.getRGBMode();
		sampleSize = sampleProvider.sampleSize();

	}
/**
 * Cette methode permet de renvoyer l'echantillon recuperé 
 * @return
 */
	public float[] getSampleColor() {
		// Initialise le tableau pour stocker les échantillons
		float[] sample = new float[sampleSize];

		// Récupère l'échantillon et le renvoie
		sampleProvider.fetchSample(sample, 0);
		return sample;
	}
	
	/**
	 * setter la couleur blanche	
	 */
	
	public void setWhite() {
			System.out.println("Couleur Blanche");
			Button.ENTER.waitForPressAndRelease();
			White = new float[sampleSize];
			sampleProvider.fetchSample(White, 0);
			System.out.println(White[0] + " " + White[1] + " " + White[2]);
			Delay.msDelay(1000);
		}
	
	/**
	 * setter la couleur noir
	 */
	
	public void setBlack() {
		System.out.println("Couleur Noir");
		Button.ENTER.waitForPressAndRelease();
		Black = new float[sampleSize];
		sampleProvider.fetchSample(Black, 0);
		System.out.println(Black[0] + " " + Black[1] + " " + Black[2]);
		Delay.msDelay(1000);
	}
	
	/**
	 * setter la couleur grise
	 */
	
	public void setGrey() {
		System.out.println("Couleur Gris");
		Button.ENTER.waitForPressAndRelease();
		Grey = new float[sampleSize];
		sampleProvider.fetchSample(Grey, 0);
		System.out.println(Grey[0] + " " + Grey[1] + " " + Grey[2]);
		Delay.msDelay(1000);
	}

	
	/**
	 * setter la couleur jaune
	 */
	public void setYellow() {
		System.out.println("Couleur Jaune");
		Button.ENTER.waitForPressAndRelease();
		Yellow = new float[sampleSize];
		sampleProvider.fetchSample(Yellow, 0);
		System.out.println(Yellow[0] + " " + Yellow[1] + " " + Yellow[2]);
		Delay.msDelay(1000);
	}

	
	/**
	 * setter la couleur verte
	 */
	public void setGreen() {
		System.out.println("Couleur Verte");
		Button.ENTER.waitForPressAndRelease();
		Green = new float[sampleSize];
		sampleProvider.fetchSample(Green, 0);
		System.out.println(Green[0] + " " + Green[1] + " " + Green[2]);
		Delay.msDelay(1000);
	}

	/**
	 * 	setter la couleur rouge
	 */

	public void setRed() {
		System.out.println("Couleur Rouge");
		Button.ENTER.waitForPressAndRelease();
		Red = new float[sampleSize];
		sampleProvider.fetchSample(Red, 0);
		System.out.println(Red[0] + " " + Red[1] + " " + Red[2]);
		Delay.msDelay(1000);
	}

	/**
	 * setter la couleur bleue
	 */
	public void setBlue() {

		System.out.println("Couleur Bleue");
		Button.ENTER.waitForPressAndRelease();
		Blue = new float[sampleSize];
		sampleProvider.fetchSample(Blue, 0);
		System.out.println(Blue[0] + " " + Blue[1] + " " + Blue[2]);
		Delay.msDelay(1000);
	}

	/**
	 * Methode qui permet d'ecrire les valeurs dans un fichier
	 */
	public void ecrire() {
		 //Nous déclarons nos objets en dehors du bloc
		
		float[][] tab=new float[7][3];
		
	    tab[0][0]= Green[0];
		tab[0][1]= Green[1];
		tab[0][2]= Green[2];
		tab[1][0]= Blue[0];
		tab[1][1]= Blue[1];
		tab[1][2]= Blue[2];
		tab[2][0]= Black[0];
		tab[2][1]= Black[1];
		tab[2][2]= Black[2];
		tab[3][0]= Yellow[0];
		tab[3][1]= Yellow[1];
		tab[3][2]= Yellow[2];
		tab[4][0]= White[0];
		tab[4][1]= White[1];
		tab[4][2]= White[2];
		tab[5][0]= Red[0];
		tab[5][1]= Red[1];
		tab[5][2]= Red[2];
		tab[6][0]= Grey[0];
		tab[6][1]= Grey[1];
		tab[6][2]= Grey[2];
		
	
	    DataOutputStream dos;
	    
	    try {
	      dos = new DataOutputStream(
	              new BufferedOutputStream(
	                new FileOutputStream(
	                  new File("couleurs.txt"))));

	      //Nous allons écrire chaque type primitif
	      for(int i=0;i<=6;i++) {
	    	  for(int j=0;j<3;j++) {
	    		  dos.writeFloat(tab[i][j]);
	    		  dos.writeChar(' ');
	    	  }
	     
	      }
	      dos.close();
	    }catch (FileNotFoundException e) {
	          e.printStackTrace();
	        } catch (IOException e) {
	          e.printStackTrace();
	        } 
	}
	
	/**
	 * Methode qui permet de lire les valeurs déjà écrites dans le fichier et les stockées dans des tableaux 
	 */
	public void lire() {
		DataInputStream dis;
	   
	
	    float[][] tab=new float[7][3];
		 //On récupère maintenant les données !
	    try {
	      dis = new DataInputStream(
	              new BufferedInputStream(
	                new FileInputStream(
	                  new File("couleurs.txt"))));
	        //Lecture des données à partir du fichier "couleur" et enregistrer chaque trois premiers floats dans dès les variables r, g, b et les mettre dans la matrice "tab"  
	      
	      for(int i=0;i<7;i++) {
	    	 	 float r= dis.readFloat();
	       //lecture du caractere qui sépare les deux float
	 	    	 dis.readChar();
	 	    	 float g= dis.readFloat();
	 	    	 dis.readChar();
	 	    	 float b= dis.readFloat();
	 	    	 dis.readChar();
	 	    	 
	 	    	 tab[i][0]=r;
	 	    	 tab[i][1]=g;
	 	    	 tab[i][2]=b;
	      }
	      //mettre les valeurs de la matrice "tab" dans les differents tableaux des differentes couleurs 
	      	Green[0]=tab[0][0];
			Green[1]=tab[0][1];
			Green[2]=tab[0][2];
			Blue[0]=tab[1][0];
			Blue[1]=tab[1][1];
			Blue[2]=tab[1][2];
			Black[0]=tab[2][0];
			Black[1]=tab[2][1];
			Black[2]=tab[2][2];
			Yellow[0]=tab[3][0];
			Yellow[1]=tab[3][1];
			Yellow[2]=tab[3][2];
			White[0]=tab[4][0];
			White[1]=tab[4][1];
			White[2]=tab[4][2];
			Red[0]=tab[5][0];
			Red[1]=tab[5][1];
			Red[2]=tab[5][2];
			Grey[0]=tab[6][0];
			Grey[1]=tab[6][1];
			Grey[2]=tab[6][2]; 
	    	
	    	
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } 
		
	}
	
	
	/**
	 * La méthode renvoie vrai si la couleur détectée est rouge
	 * @return retourne vrai si la couleur detectée est rouge si non elle retourne faux
	 */
	public boolean IsRed() {
		boolean couleur = false;
		if (((getSampleColor()[0] - 0.04) <= Red[0] && (getSampleColor()[0] + 0.04) >= Red[0])
				&& ((getSampleColor()[1] - 0.04) <= Red[1] && (getSampleColor()[1] + 0.04) >= Red[1])
				&& ((getSampleColor()[2] - 0.04) <= Red[2] && (getSampleColor()[2] + 0.04) >= Red[2])) {
			couleur = true;
		}
		return couleur;

	}
	
	
	/**
	 * La méthode renvoie vrai si la couleur détectée est grise
	 * @return retourne vrai si la couleur detectée est grise si non elle retourne faux
	 */
	public boolean IsGrey() {
		boolean couleur = false;
		if (((getSampleColor()[0] - 0.02) <= Grey[0] && (getSampleColor()[0] + 0.02) >= Grey[0])
				&& ((getSampleColor()[1] - 0.04) <= Grey[1] && (getSampleColor()[1] + 0.04) >= Grey[1])
				&& ((getSampleColor()[2] - 0.04) <= Grey[2] && (getSampleColor()[2] + 0.04) >= Grey[2])) {
			couleur = true;
		}
		return couleur;

	}
	
	/**
	 * La méthode renvoie vrai si la couleur détectée est bleue
	 * @return retourne vrai si la couleur detectée est bleu si non elle retourne faux
	 */
	public boolean IsBlue() {
		boolean couleur = false;
		if (((getSampleColor()[0] - 0.02) <= Blue[0] && (getSampleColor()[0] + 0.02) >= Blue[0])
				&& ((getSampleColor()[1] - 0.02) <= Blue[1] && (getSampleColor()[1] + 0.02) >= Blue[1])
				&& ((getSampleColor()[2] - 0.02) <= Blue[2] && (getSampleColor()[2] + 0.02) >= Blue[2])) {
			couleur = true;
		}
		return couleur;

	}

	/**
	 *  La méthode renvoie vrai si la couleur détectée est jaune
	 * @return retourne vrai si la couleur detectée est jaune si non elle retourne faux
	 */
	public boolean IsYellow() {
		boolean couleur = false;
		if (((getSampleColor()[0] - 0.04) <= Yellow[0] && (getSampleColor()[0] + 0.04) >= Yellow[0])
				&& ((getSampleColor()[1] - 0.04) <= Yellow[1] && (getSampleColor()[1] + 0.04) >= Yellow[1])
				&& ((getSampleColor()[2] - 0.04) <= Yellow[2] && (getSampleColor()[2] + 0.04) >= Yellow[2])) {
			couleur = true;
		}
		return couleur;
	}

	
	/**
	 * La méthode renvoie vrai si la couleur détectée est verte
	 * @return retourne vrai si la couleur detectée est verte si non elle retourne faux
	 */
	public boolean IsGreen() {

		boolean couleur = false;
		if (((getSampleColor()[0] - 0.02) <= Green[0] && (getSampleColor()[0] + 0.02) >= Green[0])
				&& ((getSampleColor()[1] - 0.02) <= Green[1] && (getSampleColor()[1] + 0.02) >= Green[1])
				&& ((getSampleColor()[2] - 0.02) <= Green[2] && (getSampleColor()[2] + 0.02) >= Green[2])) {
			couleur = true;
		}
		return couleur;
	}

	
	/**
	 *  La méthode renvoie vrai si la couleur détectée est noire
	 * @return retourne vrai si la couleur detectée est noire si non elle retourne faux
	 */
	public boolean IsBlack() {

		boolean couleur = false;
		if (((getSampleColor()[0] - 0.04) <= Black[0] && (getSampleColor()[0] + 0.04) >= Black[0])
				&& ((getSampleColor()[1] - 0.04) <= Black[1] && (getSampleColor()[1] + 0.04) >= Black[1])
				&& ((getSampleColor()[2] - 0.04) <= Black[2] && (getSampleColor()[2] + 0.04) >= Black[2])) {
			couleur = true;
		}
		return couleur;
	}

	/**
	 * Si la couleur détectée est blanche la méthode return true
	 * @return retourne vrai si la couleur detectée est rouge si non elle retourne faux
	 */
	public boolean IsWhite() {
		boolean couleur = false;
		if (((getSampleColor()[0] - 0.05) <= White[0] && (getSampleColor()[0] + 0.05) >= White[0])
				&& ((getSampleColor()[1] - 0.05) <= White[1] && (getSampleColor()[1] + 0.05) >= White[1])
				&& ((getSampleColor()[2] - 0.05) <= White[2] && (getSampleColor()[2] + 0.05) >= White[2])) {
			couleur = true;
		}
		return couleur;
	}

	


	/**
	 * Méthode pour setter toutes les couleurs
	 */
	public void calibrerCouleur() {
		
		setWhite();
		setRed();
		setBlack();
		setYellow();
		setBlue();
		setGreen();
		setGrey();
		getSampleColor();
		//fait appel a la methode pour ecrire dans le fichier "couleur"
		ecrire();
	}
	/**
	 * Méthode pour lire les données du fichier "couleur" deja stockées
	 */
	public void readColor() {
		//fait appel a la methode lire()
		lire();
		
	}
}
