package pack;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

/** 
 * Classe pour que le robot , plac? n'importe o? , puisse aller vers le mur le plus proche
 * @author ASUS
 *
 */
public class P8 {
        static Mouvement ev3 = new Mouvement();
        static Touchh touch = new Touchh();
        //Color color = new Color();
        static Color2 color2 = new Color2();
        static UltrasonicDistance ultrasonic = new UltrasonicDistance();
         static DifferentialPilot pilot = new DifferentialPilot(1.968f,4.35f,Motor.A,Motor.B);
       
   
        
         
        public static void main(String[] args) {
                // TODO Auto-generated method stub
                
                // les quelques lignes suivantes servent a empecher le robot de faire des mouvement hasardeux au lancement du code
                ev3.avancer1();
                Delay.msDelay(0);
                ev3.arreter1(); 
                ev3.avancer1();
                Delay.msDelay(0);
                ev3.arreter1();
                
                P8();
        }
        
        /**
         * Permet au robot d'aller au mur le plus proche 
         */
        public static void P8() {
                
                // Lecture de données stockées dans le fichier 
                color2.readColor();
                System.out.println("PRET");
                
               Button.waitForAnyPress(); 
                
               
                 
                        
                        
                ev3.ouverture1Boost();
                ultrasonic.UltrasonicClosest(360, 2.54f);  // le robot va detecter le mur le plus proche autour de lui sur un axe de 360 degr? et avec une port? de 2m54
             
                }}