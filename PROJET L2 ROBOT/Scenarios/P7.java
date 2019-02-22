package pack; 
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

import lejos.utility.Delay;
/**
 * Classe qui renvoie la couleur de la ligne detectée
 * @author ASUS
 *
 */


public class P7 {
        static Mouvement ev3 = new Mouvement();
        static Touchh touch = new Touchh();
        static Color2 color2 = new Color2();
        static UltrasonicDistance ultrasonic = new UltrasonicDistance();
         static DifferentialPilot pilot = new DifferentialPilot(1.968f,4.35f,Motor.A,Motor.B);
         
        public static void main(String[] args) {
                // TODO Auto-generated method stub
                color2.readColor();
                P7();        }
        
        /**
         * Permet de donner la couleur en dessous du robot
         */
        public static void P7() {
                
                /*
                 * Le robot ecrira la couleur sur laquelle il est pos?
                 */
                
                do {
                        if(color2.IsBlack()) {System.out.println("Noir"); Delay.msDelay(1000); LCD.clearDisplay();} // Il ecrira Noir si il est sur du noir
                        if(color2.IsBlue()) {System.out.println("Bleu"); Delay.msDelay(1000); LCD.clearDisplay();} // Il ecrira Bleu si il est sur du Bleu
                        if(color2.IsGreen()) {System.out.println("Vert"); Delay.msDelay(1000); LCD.clearDisplay();} // Il ecrira Vert si il est sur du vert
                        if(color2.IsGrey()) {System.out.println("Gris"); Delay.msDelay(1000); LCD.clearDisplay();} // Il ecrira Gris si il est sur du Gris
                        if(color2.IsRed()) {System.out.println("Rouge"); Delay.msDelay(1000); LCD.clearDisplay();} // Il ecrira Rouge si il est sur du rouge
                        if(color2.IsWhite()) {System.out.println("Blanc"); Delay.msDelay(1000); LCD.clearDisplay();} // Il ecrira Blanc si il est sur du blanc
                        if(color2.IsYellow()) {System.out.println("Jaune"); Delay.msDelay(1000); LCD.clearDisplay();} // Il ecrira Jaune si il est sur du jaune
                }        while(true);
                }
}