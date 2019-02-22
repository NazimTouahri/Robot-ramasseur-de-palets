import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

import lejos.utility.Delay;

/**
 * Classe pour aller chercher les palets en partant de la ligne Rouge avec le premier virage a droite
 * @author simon
 *
 */
public class NRougePuisDroite {
static Mouvement ev3 = new Mouvement();
static Touchh touch = new Touchh();
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
ProjetJaunePuisDroite();

}
/*
 * Cela va servir au depart , le robot utilisera ce code , pour faire un arc de cercle vers la droite
 * la vitesse pour deposer le palet est donc accru
 */
/**
 * Permet de faire deriver le robot vers la droite
 */
public static void BonusDroite() {
Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A }); // on synchronise les moteurs
Motor.B.startSynchronization();
  Motor.A.setSpeed(600);  // on met le moteur A plus lent 
  Motor.B.setSpeed(635);  // et le moteur B plus rapide , pour faire un arc de cercle
Motor.B.forward();
Motor.A.forward();			// on les fait avancer
Motor.B.endSynchronization(); 
}

/*
 * Cela va servir au depart , le robot utilisera ce code , pour faire un arc de cercle vers la gauche
 * la vitesse pour deposer le palet est donc accru
 */
/**
 * Permet de faire deriver le robot vers la gauche
 */
public static void BonusGauche() {
Motor.B.synchronizeWith(new RegulatedMotor[] { Motor.A }); // on synchronise les moteurs
Motor.B.startSynchronization();		
  Motor.A.setSpeed(635);		// on met le moteur A plus rapide
  Motor.B.setSpeed(600);		// et le moteur B plus lent , pour faire un arc de cercle
Motor.B.forward();
Motor.A.forward();		// on les fait avancer
Motor.B.endSynchronization();
}



/**
 * Permet d'aller chercher les palets en partant de la ligne Rouge avec le premier virage a droite
 * Prend fin quand il n'y a plus de palets sur les intersections
 */
public static void ProjetJaunePuisDroite() {
//color2.calibrerCouleur();
System.out.println("ReadColor");
Button.ENTER.waitForPressAndRelease(); //permet de se preparer avancer de lancer le code 
color2.readColor();
ev3.ouverture1();
Button.ENTER.waitForPressAndRelease();	 //permet de se preparer avancer de lancer le code

/////////////////LE PREMIER PALET LE BONUS ///////////////////

/*
 * Le robot va aller chercher le premier palet qui est situé devant lui 
 * il effectuera ensuite une legere rotation 
 * ensuite il efectura un arc de cercle
 * il s'arretera a la ligne blanche où il deposera le palet
 */

ev3.avancer1Boost();			//on utilise le boost pour gagner du temps
while(!touch.isPressed()) {ev3.avancer1Boost();}	//il avance jusqu'au palet
ev3.arreter1();		
ev3.fermeture1();				// il attrape le palet
pilot.rotate(-32); 				//il fait sa rotation
BonusDroite();				
while(!color2.IsWhite()) {BonusDroite();}	// le robot faire l'arc de cercle jusqu'a la ligne blanche 
ev3.arreter1();
ev3.ouverture1();		// il depose le palet


/*
 * le robot va se reculer faire un demi tour
 * tourner vers le mur et avancer vers
 * tournera ensuite vers la ligne bleu , sur laquelle il tournera
 * et ira ensuite chercher les palets qui sont dessus
 */

//le robot recule
Motor.A.backward();
Motor.B.backward();
Delay.msDelay(500);
ev3.arreter1();
pilot.rotate(75);		// il tourne pour aller vers le mur
ev3.avancer1();
while(!ultrasonic.TestUltrasonic2(0.135f)) {ev3.avancer1();}	// il avance jusqu'au mur
pilot.rotate(90); // tourne vers la ligne verte


/////////////RAMASSAGE DES PALETS SUR LA LIGNE VERTE/////////////////////


ev3.avancer1();
while(!color2.IsGreen()) {ev3.avancer1();}
ev3.arreter1();


ev3.avancer1();
Delay.msDelay(50); //dépasse un peu la ligne verte
ev3.arreter1();

/*
 *  Les prochaines lignes servent a faire tourner le robot sur la ligne bleue
 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
 */

Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
Motor.B.forward();
Motor.B.setSpeed(150);
while(!color2.IsGreen()) {Motor.B.forward();}		// Le robot tourne jusqu'au vert
ev3.arreter1();

Motor.B.setSpeed(50);	/// on met la vitesse du moteur B a 50
Motor.B.forward();
Motor.B.setSpeed(50);
while(!color2.IsGrey()) {Motor.B.forward();}	// Le robot tourne jusqu'au gris
ev3.arreter1();
Motor.B.setSpeed(300);

/*
 * Le robot avancera sur la ligne bleue tant qu'il ne rencontrera ni un palet ni un mur
 * si il rencontre un palet , il le ramenera derriere la ligne blanche
 * si il rencontre un mur , il passe la suite et continu son chemin
 */

ev3.HorsLigne();
while(!ultrasonic.TestUltrasonic2(0.135f))
{
	// les deux if permettent le suivi de ligne
if(color2.IsGreen())ev3.SurLigne();
if(color2.IsGrey()) ev3.HorsLigne();
/*
 * si le robot rencontre un palet
 * il tourne a 90 degré , va deposer le palet derriere la ligne blanche
 * et reviendra sur la ligne verte , sur laquelle il pivotera pour se mettre dans le bon axe 
 */
if(touch.isPressed()) 
{
ev3.arreter1();
ev3.fermeture1();		// il prend le palet
pilot.rotate(90);		// tourne a 90 degres
ev3.avancer1Boost();	//on met le boost pour gagner du temps
while(!color2.IsWhite()) {ev3.avancer1Boost();}	// il s'arrete a la ligne blanche
ev3.arreter1();		
ev3.ouverture1();		// il depose le palet
Motor.A.backward();
Motor.B.backward();		// recule
Delay.msDelay(500);
ev3.arreter1();
pilot.rotate(180);		// fait demi tour
ev3.avancer1();
while(!color2.IsGreen()) {ev3.avancer1();} 	// avance jusqu'au bleu
ev3.arreter1();

//va depasser un petit peu la ligne verte
ev3.avancer1();
Delay.msDelay(50);
ev3.arreter1();

//Fait la rotation pour se mettre dans l'axe 
Motor.B.setSpeed(150);
Motor.B.forward();
Motor.B.setSpeed(150);
while(!color2.IsGreen()) {Motor.B.forward();}
ev3.arreter1();

Motor.B.setSpeed(50);
Motor.B.forward();
Motor.B.setSpeed(50);
while(!color2.IsGrey()) {Motor.B.forward();}
ev3.arreter1();
Motor.B.setSpeed(300);
}
}

/*
 * Les prochaines lignes peuvent etre considerees comme un "sauvetage"
 * cela permet de s'arreter et eviter si un robot adverse est rencontrer
 */
ev3.arreter1();		// il s'arrete
ev3.Reculer();		//	il recule
Delay.msDelay(800);
ev3.arreter1();		// il attend un petit peu
Delay.msDelay(3000);

/*
 * si quelque chose est encore detecté c'est donc que c'était un mur et le robot passera a la suite
 * Sinon c'est que c'était un obot advserve et donc reprendra la prise des palets sur la ligne 
 */

if(!ultrasonic.TestUltrasonic2(0.135f)) {
while(!ultrasonic.TestUltrasonic2(0.15f))
{
	// les deux if permettent le suivi de ligne 
if(color2.IsGreen()) ev3.SurLigne();
if(color2.IsGrey()) ev3.HorsLigne();
/*
 * si le robot rencontre un palet
 * il tourne a 90 degré , va deposer le palet derriere la ligne blanche
 * et reviendra sur la ligne bleue , sur laquelle il pivotera pour se mettre dans le bon axe 
 */

if(touch.isPressed()) 
{
ev3.arreter1();		// s'arrete
ev3.fermeture1();	// prend le palet
pilot.rotate(90);	// tourne a 90 degré
ev3.avancer1Boost();	// ici le Boost pour gagner du temps
while(!color2.IsWhite()) {ev3.avancer1Boost();}	// il avance jusq'au blanc
ev3.arreter1();
ev3.ouverture1();		// depose le palet
Motor.A.backward();
Motor.B.backward();	
Delay.msDelay(500);		// recule un peu
ev3.arreter1();
pilot.rotate(180);		// fait un demi tour
ev3.avancer1();
while(!color2.IsGreen()) {ev3.avancer1();}	// avance jusqu'au vert
ev3.avancer1();	
Delay.msDelay(50);
ev3.arreter1();		// depasse un petit peu le vert

//effectue la rotation en deux temps pour se remettre dans l'axe
Motor.B.setSpeed(150);
Motor.B.forward();
Motor.B.setSpeed(150);
while(!color2.IsGreen()) {Motor.B.forward();}
ev3.arreter1();

Motor.B.setSpeed(50);
Motor.B.forward();
Motor.B.setSpeed(50);
while(!color2.IsGrey()) {Motor.B.forward();}
ev3.arreter1();
Motor.B.setSpeed(300);
}
}
}

/*
 * le robot va passer ensuite au ramssage des palets sur la ligne noir
 * il devra d'abors tourner a 90 degré
 * et ensuite avancer jusqu'au noir , et effetcué la rotation en deux temps pour se mettre dans l'axe
 */

pilot.rotate(-90);		// tourne a 90 degré
ev3.avancer1();
while(!color2.IsBlack()) {ev3.avancer1();}		// avance jusqu'au noir
ev3.avancer1();
Delay.msDelay(100);
ev3.arreter1();		// depasse un petit peu

/*
 *  Les prochaines lignes servent a faire tourner le robot sur la ligne noir
 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
 */




Motor.A.setSpeed(150);		// on met la vitesse du moteur A a 150
Motor.A.forward();
Motor.A.setSpeed(150);
while(!color2.IsBlack()) {Motor.A.forward();} 	// Le robot tourne jusqu'au noir
ev3.arreter1();

Motor.A.setSpeed(50);		// on met la vitesse du moteur A a 50
Motor.A.forward();
Motor.A.setSpeed(50);
while(!color2.IsGrey()) {Motor.A.forward();}	// Le robot tourne jusqu'au gris
ev3.arreter1();
Motor.A.setSpeed(300);

//////////RAMASSAGE DES PALETS DE LA LIGNE NOIR//////////////

/*
 * Le robot avancera sur la ligne noir tant qu'il ne rencontrera ni un palet ni un mur
 * si il rencontre un palet , il le ramenera derriere la ligne blanche
 * si il rencontre un mur , il passe a la suite et continu son chemin
 */


ev3.SurLigne();
while(!ultrasonic.TestUltrasonic2(0.15f))
{
	// les deux if permettent le suivi de ligne 
if(color2.IsBlack()) ev3.HorsLigne();
if(color2.IsGrey()) ev3.SurLigne();
/*
 * si le robot rencontre un palet
 * il tourne a 90 degré , va deposer le palet derriere la ligne blanche
 * et reviendra sur la ligne noir , sur laquelle il pivotera pour se mettre dans le bon axe 
 */

if(touch.isPressed()) 
{
ev3.arreter1();		// il s'arrete 	
ev3.fermeture1();	//attrape la palet
pilot.rotate(-90);	// tourne a 90 degré
ev3.avancer1Boost();	//ici le Boost pour aggner du temps
while(!color2.IsWhite()) {ev3.avancer1Boost();}		// avancer jusqu'au blanc
ev3.arreter1();
ev3.ouverture1();		// dépose le palet
Motor.A.backward();
Motor.B.backward();		// il recule
Delay.msDelay(500);
ev3.arreter1();
pilot.rotate(-178);	// fait un demi tour , ici moins de 180 degré pour evité de rencontrer la ligne noir perpandiculaire
ev3.avancer1();
while(!color2.IsBlack()) {ev3.avancer1();}	// avance jusqu'au noir
ev3.avancer1();
Delay.msDelay(100);
ev3.arreter1();			// depasse un peu le noir

/*
 *  Les prochaines lignes servent a faire tourner le robot sur la ligne noir
 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
 */


Motor.A.setSpeed(150);		// on met la vitesse du moteur a 150
Motor.A.forward();
Motor.A.setSpeed(150);
while(!color2.IsBlack()) {Motor.A.forward();}	// le robot tourne jusqu'au noir
ev3.arreter1();

Motor.A.setSpeed(50);		// on ralentis le moteur A a 50
Motor.A.forward();
Motor.A.setSpeed(50);
while(!color2.IsGrey()) {Motor.A.forward();}		// le robot tourne jusqu'au gris
ev3.arreter1();
Motor.A.setSpeed(300);


}
}

/*
 * Les prochaines lignes peuvent etre considerees comme un "sauvetage"
 * cela permet de s'arreter et eviter si un robot adverse est rencontrer
 */


ev3.arreter1();		// il s'arrete 
ev3.Reculer();		// recule
Delay.msDelay(800);	// attend un peu	
ev3.arreter1();		//s'arrete un petit peu
Delay.msDelay(3000);

/*
 * si quelque chose est encore detecté c'est donc que c'était un mur et le robot passera a la suite
 * Sinon c'est que c'était un robot advserve et donc reprendra la prise des palets sur la ligne 
 */

if(!ultrasonic.TestUltrasonic2(0.135f)) {
while(!ultrasonic.TestUltrasonic2(0.15f))
{
	// les deux if permettent le suivi de ligne 
if(color2.IsBlack()) ev3.HorsLigne();
if(color2.IsGrey()) ev3.SurLigne();
/*
 * si le robot rencontre un palet
 * il tourne a 90 degré , va deposer le palet derriere la ligne blanche
 * et reviendra sur la ligne noire , sur laquelle il pivotera pour se mettre dans le bon axe 
 */

if(touch.isPressed()) 
{
ev3.arreter1();		// il s'arrete
ev3.fermeture1();	// il prend le palet
pilot.rotate(-90);	// pivote a 90 degré
ev3.avancer1Boost();	//ici le boost pour gagner du temps
while(!color2.IsWhite()) {ev3.avancer1Boost();} // il avance jusqu'au blanc
ev3.arreter1();
ev3.ouverture1();		// dépose le palet
Motor.A.backward();
Motor.B.backward();		//recule
Delay.msDelay(500);
ev3.arreter1();
pilot.rotate(-178);			// fait un demi tour , ici moins de 180 degré pour evité de rencontrer la ligne noir perpandiculaire
ev3.avancer1();
while(!color2.IsBlack()) {ev3.avancer1();}	// avance jusqu'au noir
ev3.avancer1();
Delay.msDelay(100);
ev3.arreter1();		// dépasse un peu la ligne noire

/*
 *  Les prochaines lignes servent a faire tourner le robot sur la ligne noir
 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
 */



Motor.A.setSpeed(150);	// on met la vitesse du moteur a 150
Motor.A.forward();
Motor.A.setSpeed(150);
while(!color2.IsBlack()) {Motor.A.forward();}	// le robot tourne jusqu'au noir
ev3.arreter1();

Motor.A.setSpeed(50);	// on ralentis le moteur A a 50
Motor.A.forward();
Motor.A.setSpeed(50);
while(!color2.IsGrey()) {Motor.A.forward();}		// le robot tourne jusqu'au gris
ev3.arreter1();
Motor.A.setSpeed(300);

}
}
}

/*
 * le robot va passer ensuite au ramssage des palets sur la ligne verte
 * il devra d'abors tourner a 90 degré
 * et ensuite avancer jusqu'au verte , et effetcué la rotation en deux temps pour se mettre dans l'axe
 */


pilot.rotate(90);		//toune a 90 degré
ev3.avancer1();
while(!color2.IsBlue()) {ev3.avancer1();}		//avance jusqu'au bleu
ev3.arreter1();
ev3.avancer1();
Delay.msDelay(38);
ev3.arreter1();		// depasse un peu la ligne bleu


/*
 *  Les prochaines lignes servent a faire tourner le robot sur la ligne bleu
 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
 */
		
Motor.B.setSpeed(150);		// on met la vitesse du moteur a 150
Motor.B.forward();
Motor.B.setSpeed(150);
while(!color2.IsBlue()) {Motor.B.forward();}	// le robot tourne jusqu'au bleu
ev3.arreter1();

Motor.B.setSpeed(50);		// on ralentis le moteur B a 50
Motor.B.forward();
Motor.B.setSpeed(50);
while(!color2.IsGrey()) {Motor.B.forward();}		// le robot tourne jusqu'au gris
ev3.arreter1();
Motor.B.setSpeed(300);

/*
 * Le robot avancera sur la ligne verte tant qu'il ne rencontrera ni un palet ni un mur
 * si il rencontre un palet , il le ramenera derriere la ligne blanche
 * si il rencontre un mur , il passe la suite et continu son chemin
 */

ev3.HorsLigne();
while(!ultrasonic.TestUltrasonic2(0.15f))
{
	// les deux if permettent de suivre la ligne bleu
if(color2.IsBlue()) ev3.SurLigne();
if(color2.IsGrey()) ev3.HorsLigne();

/*
 * si le robot rencontre un palet
 * il tourne a 90 degré , va deposer le palet derriere la ligne blanche
 * et reviendra sur la ligne bleue, sur laquelle il pivotera pour se mettre dans le bon axe 
 */

if(touch.isPressed()) 
{
ev3.arreter1();		//il s'arrete 	
ev3.fermeture1();	//attrape le palet
pilot.rotate(90);	//tourne a 90 degré
ev3.avancer1Boost();	// ici le boost pour gagner du temps	
while(!color2.IsWhite()) {ev3.avancer1Boost();}	//avance jusqu'au blanc
ev3.arreter1();
ev3.ouverture1();		//dépose le palet
Motor.A.backward();
Motor.B.backward();		//recule
Delay.msDelay(500);
ev3.arreter1();
pilot.rotate(180);		//fait demi tour
ev3.avancer1();
while(!color2.IsBlue()) {ev3.avancer1();}		// avance jusqu'au bleu
ev3.avancer1();
Delay.msDelay(50);
ev3.arreter1();		// dépasse un petit peu la ligne bleu

/*
 *  Les prochaines lignes servent a faire tourner le robot sur la ligne bleu
 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
 */


Motor.B.setSpeed(150);		// on met la vitesse du moteur a 150
Motor.B.forward();
Motor.B.setSpeed(150);
while(!color2.IsBlue()) {Motor.B.forward();}		// le robot tourne jusqu'au bleu
ev3.arreter1();

Motor.B.setSpeed(50);		// on ralentis le moteur B a 50
Motor.B.forward();
Motor.B.setSpeed(50);
while(!color2.IsGrey()) {Motor.B.forward();}		// le robot tourne jusqu'au gris
ev3.arreter1();
Motor.B.setSpeed(300);
}
}

/*
 * Les prochaines lignes peuvent etre considerees comme un "sauvetage"
 * cela permet de s'arreter et eviter si un robot adverse est rencontrer
 */
ev3.arreter1(); // il s'arrete 	
ev3.Reculer(); // il reucle
Delay.msDelay(800); // attend un peu
ev3.arreter1();	// s'arrete 
Delay.msDelay(3000); // attend encore un peu


/*
 * si quelque chose est encore detecté c'est donc que c'était un mur et le robot passera a la suite
 * Sinon c'est que c'était un robot advserve et donc reprendra la prise des palets sur la ligne 
 */

if(!ultrasonic.TestUltrasonic2(0.135f)) {
while(!ultrasonic.TestUltrasonic2(0.15f))
{
	/// les deux if permettent de suivre la ligne bleu
if(color2.IsBlue()) ev3.SurLigne();
if(color2.IsGrey()) ev3.HorsLigne();

/*
 * si le robot rencontre un palet
 * il tourne a 90 degré , va deposer le palet derriere la ligne blanche
 * et reviendra sur la ligne verte , sur laquelle il pivotera pour se mettre dans le bon axe 
 */

if(touch.isPressed()) 
{
ev3.arreter1();		// il s'arrete 
ev3.fermeture1();	//atrape le palet
pilot.rotate(90);	//tourne a 90 degré
ev3.avancer1Boost();	// ici le boost pour gagner du temps
while(!color2.IsWhite()) {ev3.avancer1Boost();}	//avance jusqu'au blanc
ev3.arreter1();
ev3.ouverture1();		//depose le palet
Motor.A.backward();
Motor.B.backward();		// il recule
Delay.msDelay(500);
ev3.arreter1();
pilot.rotate(180);		//fait demi tour
ev3.avancer1();
while(!color2.IsBlue()) {ev3.avancer1();}		//avance jusqu'au bleu
ev3.avancer1();
Delay.msDelay(50);
ev3.arreter1();		//depasse un peu le bleu

//il effectue ensuite la rotation en deux temps pour bien se remettre dans l'axe de la ligne bleu

Motor.B.setSpeed(150);		// on met la vitesse du moteur a 150
Motor.B.forward();
Motor.B.setSpeed(150);
while(!color2.IsBlue()) {Motor.B.forward();}		// le robot tourne jusqu'au bleu
ev3.arreter1();	

Motor.B.setSpeed(50);		// on ralentis le moteur B a 50
Motor.B.forward();
Motor.B.setSpeed(50);
while(!color2.IsGrey()) {Motor.B.forward();}		// le robot tourne jusqu'au gris
ev3.arreter1();
Motor.B.setSpeed(300);
}
}
}
///////////////FIN DES 9 PALETS LE ROBOT VA VENIR ARRETER LE PROGRAMME EN SE METTANT SUR SA LIGNE BLANCHE ///////////
ev3.arreter1();
pilot.rotate(90);		// toune a 90 degré
ev3.avancer1();
while(!color2.IsWhite()) {ev3.avancer1();}	// avance jusqu'a la ligne blanche
ev3.arreter1();		// s'arrete 

}
/////////////FIN DU PRPGRAMME///////////////  

}
