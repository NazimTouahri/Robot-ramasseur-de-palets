
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;


/**
 * Classe pour que le robot , placé n'importe où , puisse aller chercher un palet placé n'importe où
 * @author simon
 *
 */
public class P6 {

	static Mouvement ev3 = new Mouvement();
	static Touchh touch = new Touchh();
	//Color color = new Color();
	static Color2 color2 = new Color2();
	static UltrasonicDistance ultrasonic = new UltrasonicDistance();
	 static DifferentialPilot pilot = new DifferentialPilot(1.968f,4.35f,Motor.A,Motor.B);
	 static int cmpt=0;
	 static int button;
	 static int cmp , cmp2 , cmpV , cmpB =0;
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// les quelques lignes suivantes servent a empecher le robot de faire des mouvement hasardeux au lancement du code

		ev3.avancer1();
		Delay.msDelay(0);
		ev3.arreter1(); 
		ev3.avancer1();
		Delay.msDelay(0);
		ev3.arreter1();
		
		P6();

	}
	
	/**
	 * Permet au robot d'aller chercher un palet placé n'importe où
	 */
	public static void P6() {
		color2.readColor();
		System.out.println("PRET");
		button=Button.waitForAnyPress(); // la variable button va prendre la valeur qui serra renvoyé par le prochain bouton qui sera pressé
		
		/*
		 * il y a deux if 
		 * le premier sert a lancer le code pour le coté Ouest si on appuie sur le bouton gauche
		 * le second sert a lancer le code pour le coté Est si on appuie sur le bouton droit
		 */
		if(button==16) {
			
			///////////////////PREMIER TEST N'IMPORTE OU SUR LE TERRAIN//////////////
		ev3.ouverture1Boost();
		ultrasonic.UltrasonicClosest(360, 2.54f);  // le robot va detecter l'element le plus proche autour de lui sur un axe de 360 degré et avec une porté de 2m54
		ev3.avancer1();								// il avance
		
		//Si le robot a detecté un palet
		if(ultrasonic.hasPalet()) {
			ev3.arreter1();
			ev3.fermeture1Boost();		// il attrape le palet	
			ev3.arreter1();
			/*
			 *  il redetecter l'element le plus proche autour de lui sur un axe de 360 degré et avec une porté de 2m54
			 *  ce sera forcement un mur car le palet est dans ses pinces
			 *  c'est ce qui lui permettra de pouvoir l'orienté par la suite
			 */
			ultrasonic.UltrasonicClosest(360, 2.54f);	// il fait la detection
			ev3.arreter1();
			pilot.rotate(90);			// tourne a 90 degré
			ev3.avancer1();
			while(!color2.IsWhite()) {ev3.avancer1();}	//avance jusqu'a la ligne blanche
			ev3.arreter1();
			ev3.avancer1();
			Delay.msDelay(100);
			ev3.arreter1();			// la dépasse un peu
			
			/*
			 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blache
			 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
			 */

			Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
			Motor.B.forward();
			Motor.B.setSpeed(150);
			while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
			ev3.arreter1();

			Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
			Motor.B.forward();
			Motor.B.setSpeed(50);		
			while(!color2.IsGrey()) {Motor.B.forward();}		// Le robot tourne jusqu'au gris 
			ev3.arreter1();
			Motor.B.setSpeed(300);
			
			/*
			 * le robot avancera ensuite jusqu'au mur
			 * ensuite il pivotera a 90 degré 
			 */
			
			ev3.HorsLigne();
			while(!ultrasonic.TestUltrasonic2(0.135f))
			{
				// les deux if permettent le suivi de ligne
				if(color2.IsWhite()) ev3.SurLigne();
				if(color2.IsGrey()) ev3.HorsLigne();
			}
			
			// ici le robot tourne un peu plus que 90 degré, c'est pour s'assurer qu'il ne tappera pas dans le mur
			pilot.rotate(95);
			ev3.avancer1();
			
			/*
			 * Ceci est le test de camp
			 * le robot avncera dans la longueur du terrain 
			 * deux compteur tournent pendant l'execution de la boucle
			 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
			 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
			 * si c'est le bon coté le robot depose le palet
			 * sinon il fait demi tour et depose le palet de lautre coté
			 */
			while(!ultrasonic.TestUltrasonic2(0.15f))
			{	
				// les deux compteur qui augmentent a chaque tour de boucle
				cmp=cmp+1;
				cmp2=cmp+1;
		
				if(color2.IsGreen()) {cmpV=cmp;}		// validation de la variable pour le Vert
				if(color2.IsBlue()) {cmpB=cmp2;}		// validation de la variable pour le Bleu
				if(color2.IsWhite()) {break;}	
					
			}
			
			//Si la variable pour le vert est plus grande c'est qu'il est du mauvais coté
			if(cmpB<cmpV) { 
			pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
			ev3.avancer1();
			while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
			ev3.arreter1();
			ev3.ouverture1Boost();		// dépose le palet
			System.exit(0);		// fin du programme
				
			}
			
			//Si la variable pour le vert est plus petite c'est qu'il est du bon coté
			if(cmpB>cmpV) { 
			ev3.avancer1();
			while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
			ev3.arreter1();
			ev3.ouverture1Boost();		// dépose le palet
			System.exit(0);		// fin du programme
				
			}
			}
			
		
		//On rentre dans le sinon si la premiere detection était un mur
			else {
				
				
				ev3.arreter1();
				pilot.rotate(90);  // il tourne a 90 degré
				
		
				/*
				 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blanche
				 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
				 */
			
				ev3.avancer1();
				while(!color2.IsWhite()) {ev3.avancer1();}		//s'arrete au blanc
				ev3.arreter1();
				ev3.avancer1();
				Delay.msDelay(100);		
				ev3.arreter1();				//depasse un peu la ligne

				Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
				Motor.B.forward();
				Motor.B.setSpeed(150);
				while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
				ev3.arreter1();

				Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
				Motor.B.forward();
				Motor.B.setSpeed(50);
				while(!color2.IsGrey()) {Motor.B.forward();}		 // Le robot tourne jusqu'au gris 
				ev3.arreter1();
				Motor.B.setSpeed(300);
				ev3.arreter1();
				
				/*
				 * ceci sert a placer le palet devant le robot si il l'a attrapé dans ses pince durant le trajet
				 * car le palet peut de trouver dans la pince et ne pas actionner le capteur de pression
				 * donc on ferme les pince , puis on les ouvres , pour que le palet se retouve devant lui si il y en avait un
				 */
				ev3.fermeture1Boost();
				ev3.ouverture1Boost();
				
				
			//////AVANCE SUR LA LIGNE BLANCHE/////
				
				/*
				 * Le robot avancera sur la ligne blanche tant qu'il ne rencontrera ni un palet ni un mur
				 * si il rencontre un palet , il le ramenera derriere la ligne blanche
				 * si il rencontre un mur , il passe la suite et continu son chemin
				 */
				
					ev3.HorsLigne();
					while(!ultrasonic.TestUltrasonic2(0.19f))
					{
						// les deux if permettent le suivi de ligne
						if(color2.IsWhite()) ev3.SurLigne();
						if(color2.IsGrey()) ev3.HorsLigne();
						
					/*
					 * si il le capteur de pression est activé c'est qu'il avait un palet dans les pinces
					 * il commence alors a aller se placer pour detecter les camps et deposer ensuite le palet
					 */
						if(touch.isPressed()) {
						ev3.arreter1();
						ev3.fermeture1Boost();  // il attrape le palet
						
						
						ev3.arreter1();
						ev3.avancer1();
						Delay.msDelay(1000);
						ev3.arreter1();		// il avance un peu pour se decaler du mur
						
						pilot.rotate(90);		// tourne a 90 degré
						ev3.avancer1();
						
						/*
						 * Ceci est le test de camp
						 * le robot avncera dans la longueur du terrain 
						 * deux compteur tournent pendant l'execution de la boucle
						 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
						 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
						 * si c'est le bon coté le robot depose le palet
						 * sinon il fait demi tour et depose le palet de lautre coté
						 */
						
						while(!ultrasonic.TestUltrasonic2(0.15f))
						{
							// les deux compteur qui augmentent a chaque tour de boucle
							cmp=cmp+1;
							cmp2=cmp+1;
					
							if(color2.IsGreen()) {cmpV=cmp;}		//validation du vert
							if(color2.IsBlue()) {cmpB=cmp2;}		//validation du bleu
							if(color2.IsWhite()) {break;}	
								
						}
						
						//Si la variable pour le vert est plus grande c'est qu'il est du mauvais coté
						if(cmpB<cmpV) { 
						pilot.rotate(180);		// il fait demi tour 
						ev3.avancer1();
						while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
						ev3.arreter1();
						ev3.ouverture1Boost();		// dépose le palet
						System.exit(0);		// fin du programme
							
						}
						
						//Si la variable pour le vert est plus petite c'est qu'il est du bon coté
						if(cmpB>cmpV) { 
						ev3.avancer1();
						while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
						ev3.arreter1();
						ev3.ouverture1Boost();		// dépose le palet
						System.exit(0);		// fin du programme
							
						}}
					}
					
			/*
			 * le robot est arrivé au mur
			 * il va tourné et avancer jusqu'a la ligne noir
			 * il effectura une detection a 180 degré et avec une porté d'1.1m pour evité de faire un scan qui depasserai les limites du terrain
			 */
					
					pilot.rotate(90);		// il tourne a 90 degré
					ev3.avancer1();
					while(!color2.IsBlack()) {ev3.avancer1();}		// avance jusqu'a noir
					ev3.arreter1();
					
					//////////////////////////////////////////
					///////////////////////////
					ultrasonic.UltrasonicClosest(180, 1.1f);		// effectue une detection sur un axe de 180 degré et avec une portée d'1.1m
					if(ultrasonic.hasPalet()) {
						ev3.arreter1();
						ev3.fermeture1Boost();		// il attrape le palet	
						ev3.arreter1();
						/*
						 *  il redetecter l'element le plus proche autour de lui sur un axe de 360 degré et avec une porté de 2m54
						 *  ce sera forcement un mur car le palet est dans ses pinces
						 *  c'est ce qui lui permettra de pouvoir l'orienté par la suite
						 */
						ultrasonic.UltrasonicClosest(360, 2.54f);	// il fait la detection
						ev3.arreter1();
						pilot.rotate(90);			// tourne a 90 degré
						ev3.avancer1();
						while(!color2.IsWhite()) {ev3.avancer1();}	//avance jusqu'a la ligne blanche
						ev3.arreter1();
						ev3.avancer1();
						Delay.msDelay(100);
						ev3.arreter1();			// la dépasse un peu
 

						/*
						 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blache
						 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
						 */

						Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
						Motor.B.forward();
						Motor.B.setSpeed(150);
						while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
						ev3.arreter1();

						Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
						Motor.B.forward();
						Motor.B.setSpeed(50);		
						while(!color2.IsGrey()) {Motor.B.forward();}		// Le robot tourne jusqu'au gris 
						ev3.arreter1();
						Motor.B.setSpeed(300);
						
						/*
						 * le robot avancera ensuite jusqu'au mur
						 * ensuite il pivotera a 90 degré 
						 */
						
						ev3.HorsLigne();
						while(!ultrasonic.TestUltrasonic2(0.135f))
						{
							// les deux if permettent le suivi de ligne
							if(color2.IsWhite()) ev3.SurLigne();
							if(color2.IsGrey()) ev3.HorsLigne();
						}
						
						// ici le robot tourne un peu plus que 90 degré, c'est pour s'assurer qu'il ne tappera pas dans le mur
						pilot.rotate(95);
						ev3.avancer1();
						
						/*
						 * Ceci est le test de camp
						 * le robot avncera dans la longueur du terrain 
						 * deux compteur tournent pendant l'execution de la boucle
						 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
						 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
						 * si c'est le bon coté le robot depose le palet
						 * sinon il fait demi tour et depose le palet de lautre coté
						 */
						while(!ultrasonic.TestUltrasonic2(0.15f))
						{	
							// les deux compteur qui augmentent a chaque tour de boucle
							cmp=cmp+1;
							cmp2=cmp+1;
					
							if(color2.IsGreen()) {cmpV=cmp;}		// validation de la variable pour le Vert
							if(color2.IsBlue()) {cmpB=cmp2;}		// validation de la variable pour le Bleu
							if(color2.IsWhite()) {break;}	
								
						}
						
						//Si la variable pour le vert est plus grande c'est qu'il est du mauvais coté
						if(cmpB<cmpV) { 
						pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
						ev3.avancer1();
						while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
						ev3.arreter1();
						ev3.ouverture1Boost();		// dépose le palet
						System.exit(0);		// fin du programme
							
						}
						
						//Si la variable pour le vert est plus petite c'est qu'il est du bon coté
						if(cmpB>cmpV) { 
						ev3.avancer1();
						while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
						ev3.arreter1();
						ev3.ouverture1Boost();		// dépose le palet
						System.exit(0);		// fin du programme
							
						}
						}
						
					
					/*
					 * le robot n'a rien detecté
					 * il va alors continuer son chemin pour aller jusqu'a la ligne blanche
					 */
						else {	
							pilot.rotate(2);		// on le fait devier un petit peu pour ne pas qu'il ne rentre dans le mur
							ev3.arreter1();
							

							/*
							 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blanche
							 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
							 */
						
							ev3.avancer1();
							while(!color2.IsWhite()) {ev3.avancer1();}		//s'arrete au blanc
							ev3.arreter1();
							ev3.avancer1();
							Delay.msDelay(100);		
							ev3.arreter1();				//depasse un peu la ligne

							Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
							Motor.B.forward();
							Motor.B.setSpeed(150);
							while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
							ev3.arreter1();

							Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
							Motor.B.forward();
							Motor.B.setSpeed(50);
							while(!color2.IsGrey()) {Motor.B.forward();}		 // Le robot tourne jusqu'au gris 
							ev3.arreter1();
							Motor.B.setSpeed(300);
							ev3.arreter1();
							
							/*
							 * ceci sert a placer le palet devant le robot si il l'a attrapé dans ses pince durant le trajet
							 * car le palet peut de trouver dans la pince et ne pas actionner le capteur de pression
							 * donc on ferme les pince , puis on les ouvres , pour que le palet se retouve devant lui si il y en avait un
							 */
							ev3.fermeture1Boost();
							ev3.ouverture1Boost();
							

							/*
							 * Le robot avancera sur la ligne blanche tant qu'il ne rencontrera ni un palet ni un mur
							 * si il rencontre un palet , il le ramenera derriere la ligne blanche
							 * si il rencontre un mur , il passe la suite et continu son chemin
							 */
							
								ev3.HorsLigne();
								while(!ultrasonic.TestUltrasonic2(0.19f))
								{
									// les deux if permettent le suivi de ligne
									if(color2.IsWhite()) ev3.SurLigne();
									if(color2.IsGrey()) ev3.HorsLigne();
									
								/*
								 * si il le capteur de pression est activé c'est qu'il avait un palet dans les pinces
								 * il commence alors a aller se placer pour detecter les camps et deposer ensuite le palet
								 */
									if(touch.isPressed()) {
									ev3.arreter1();
									ev3.fermeture1Boost();  // il attrape le palet
									
									
									ev3.arreter1();
									ev3.avancer1();
									Delay.msDelay(1000);
									ev3.arreter1();		// il avance un peu pour se decaler du mur
									
									pilot.rotate(90);		// tourne a 90 degré
									ev3.avancer1();
									
									/*
									 * Ceci est le test de camp
									 * le robot avncera dans la longueur du terrain 
									 * deux compteur tournent pendant l'execution de la boucle
									 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
									 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
									 * si c'est le bon coté le robot depose le palet
									 * sinon il fait demi tour et depose le palet de lautre coté
									 */
									
									while(!ultrasonic.TestUltrasonic2(0.15f))
									{
										// les deux compteur qui augmentent a chaque tour de boucle
										cmp=cmp+1;
										cmp2=cmp+1;
								
										if(color2.IsGreen()) {cmpV=cmp;}		//validation du vert
										if(color2.IsBlue()) {cmpB=cmp2;}		//validation du bleu
										if(color2.IsWhite()) {break;}	
											
									}
									
									//Si la variable pour le vert est plus grande c'est qu'il est du mauvais coté
									if(cmpB<cmpV) { 
									pilot.rotate(180);		// il fait demi tour 
									ev3.avancer1();
									while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
									ev3.arreter1();
									ev3.ouverture1Boost();		// dépose le palet
									System.exit(0);		// fin du programme
										
									}
									
									//Si la variable pour le vert est plus petite c'est qu'il est du bon coté
									if(cmpB>cmpV) { 
									ev3.avancer1();
									while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
									ev3.arreter1();
									ev3.ouverture1Boost();		// dépose le palet
									System.exit(0);		// fin du programme
										
									}}
								}
								
								/*
								 * le robot est arrivé au mur
								 * il va tourné et avancer jusqu'a la ligne noir
								 * il effectura une detection a 180 degré et avec une porté d'1.1m pour evité de faire un scan qui depasserai les limites du terrain
								 */
										
										pilot.rotate(90);		// il tourne a 90 degré
										ev3.avancer1();
										while(!color2.IsBlack()) {ev3.avancer1();}		// avance jusqu'a noir
										ev3.arreter1();
										
										//////////////////////////////////////////
										///////////////////////////
										ultrasonic.UltrasonicClosest(180, 1.1f);		// effectue une detection sur un axe de 180 degré et avec une portée d'1.1m
										if(ultrasonic.hasPalet()) {
											ev3.arreter1();
											ev3.fermeture1Boost();		// il attrape le palet	
											ev3.arreter1();
											/*
											 *  il redetecter l'element le plus proche autour de lui sur un axe de 360 degré et avec une porté de 2m54
											 *  ce sera forcement un mur car le palet est dans ses pinces
											 *  c'est ce qui lui permettra de pouvoir l'orienté par la suite
											 */
											ultrasonic.UltrasonicClosest(360, 2.54f);	// il fait la detection
											ev3.arreter1();
											pilot.rotate(90);			// tourne a 90 degré
											ev3.avancer1();
											while(!color2.IsWhite()) {ev3.avancer1();}	//avance jusqu'a la ligne blanche
											ev3.arreter1();
											ev3.avancer1();
											Delay.msDelay(100);
											ev3.arreter1();			// la dépasse un peu
					 

											/*
											 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blache
											 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
											 */

											Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
											Motor.B.forward();
											Motor.B.setSpeed(150);
											while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
											ev3.arreter1();

											Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
											Motor.B.forward();
											Motor.B.setSpeed(50);		
											while(!color2.IsGrey()) {Motor.B.forward();}		// Le robot tourne jusqu'au gris 
											ev3.arreter1();
											Motor.B.setSpeed(300);
											
											/*
											 * le robot avancera ensuite jusqu'au mur
											 * ensuite il pivotera a 90 degré 
											 */
											
											ev3.HorsLigne();
											while(!ultrasonic.TestUltrasonic2(0.135f))
											{
												// les deux if permettent le suivi de ligne
												if(color2.IsWhite()) ev3.SurLigne();
												if(color2.IsGrey()) ev3.HorsLigne();
											}
											
											// ici le robot tourne un peu plus que 90 degré, c'est pour s'assurer qu'il ne tappera pas dans le mur
											pilot.rotate(95);
											ev3.avancer1();
											
											/*
											 * Ceci est le test de camp
											 * le robot avncera dans la longueur du terrain 
											 * deux compteur tournent pendant l'execution de la boucle
											 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
											 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
											 * si c'est le bon coté le robot depose le palet
											 * sinon il fait demi tour et depose le palet de lautre coté
											 */
											while(!ultrasonic.TestUltrasonic2(0.15f))
											{	
												// les deux compteur qui augmentent a chaque tour de boucle
												cmp=cmp+1;
												cmp2=cmp+1;
										
												if(color2.IsGreen()) {cmpV=cmp;}		// validation de la variable pour le Vert
												if(color2.IsBlue()) {cmpB=cmp2;}		// validation de la variable pour le Bleu
												if(color2.IsWhite()) {break;}	
													
											}
											
											//Si la variable pour le vert est plus grande c'est qu'il est du mauvais coté
											if(cmpB<cmpV) { 
											pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
											ev3.avancer1();
											while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
											ev3.arreter1();
											ev3.ouverture1Boost();		// dépose le palet
											System.exit(0);		// fin du programme
												
											}
											
											//Si la variable pour le vert est plus petite c'est qu'il est du bon coté
											if(cmpB>cmpV) { 
											ev3.avancer1();
											while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
											ev3.arreter1();
											ev3.ouverture1Boost();		// dépose le palet
											System.exit(0);		// fin du programme
												
											}
											}
											
										
										/*
										 * le robot n'a rien detecté
										 * il va alors continuer son chemin pour aller jusqu'a la ligne blanche
										 */
											else {	
												pilot.rotate(2);		// on le fait devier un petit peu pour ne pas qu'il ne rentre dans le mur
												ev3.arreter1();
										
								/////TOURNE SUR LA LIGNE BLANCHE////////////
									
									
												/*
												 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blanche
												 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
												 */
											
												ev3.avancer1();
												while(!color2.IsWhite()) {ev3.avancer1();}		//s'arrete au blanc
												ev3.arreter1();
												ev3.avancer1();
												Delay.msDelay(100);		
												ev3.arreter1();				//depasse un peu la ligne

												Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
												Motor.B.forward();
												Motor.B.setSpeed(150);
												while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
												ev3.arreter1();

												Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
												Motor.B.forward();
												Motor.B.setSpeed(50);
												while(!color2.IsGrey()) {Motor.B.forward();}		 // Le robot tourne jusqu'au gris 
												ev3.arreter1();
												Motor.B.setSpeed(300);
												ev3.arreter1();
												
												
									//////AVANCE SUR LA LIGNE BLANCHE/////
										// le robot va avancer le long de la ligne blanche jusqu'au mur
											ev3.HorsLigne();
											while(!ultrasonic.TestUltrasonic2(0.135f))
											{
												// les deux if permettent le suivi de ligne
												if(color2.IsWhite()) ev3.SurLigne();
												if(color2.IsGrey()) ev3.HorsLigne();
											}
											
									/*
									 * le robot va aller se placer le long le la ligne noir 
									 * pour cela il fera une rotation a 90 degré
									 * ensuite il avncera un petit peu 
									 * il fera ensuite une nouvelle rotation pour ensuite avancer jusqu'a la ligne noire
									 */
											
											pilot.rotate(90);		// tourne a 90 degré
											ev3.avancer1();
											Delay.msDelay(500);		// avance un petit peu
											ev3.arreter1();
											pilot.rotate(90);		// tourne a 90 degré
											ev3.avancer1();
											while(!color2.IsBlack()) {ev3.avancer1();}		// avance jusqu'au noir
											ev3.arreter1();
											ev3.avancer1();
											Delay.msDelay(50);
											ev3.arreter1();		// depasse un peu la ligne noire
											ev3.avancer1();
											Delay.msDelay(100);
											ev3.arreter1();

											Motor.A.setSpeed(150);		// on met la vitesse du moteur A a 150
											Motor.A.forward();
											Motor.A.setSpeed(150);	
											while(!color2.IsBlack()) {Motor.A.forward();}		// Le robot tourne jusqu'au noir
											ev3.arreter1();

											Motor.A.setSpeed(50);		/// on met la vitesse du moteur B a 50
											Motor.A.forward();
											Motor.A.setSpeed(50);
											while(!color2.IsGrey()) {Motor.A.forward();}		// Le robot tourne jusqu'au gris
											ev3.arreter1();
											Motor.B.setSpeed(300);
											
											/*
											 * le robot suivra la ligne noir jusqu'a la ligne bleu
											 */
											while(!ultrasonic.TestUltrasonic2(0.135f))
											{
												// les deux if permettent le suivi de ligne
												if(color2.IsBlack()) ev3.HorsLigne();
												if(color2.IsGrey()) ev3.SurLigne();
												if(color2.IsBlue()) {ev3.arreter1(); break;}		// quand il voit du bleu , il s'arrete et sort de la boucle
												
											}
											
											//////////////////////////////PETITE DETECTION NUMERO 1 ///////////////////
											
											// le robot fait une detection sur un angle de 360 degré et avec une porté de 0.6m
											ultrasonic.UltrasonicClosest(360, 0.6f);
											
											// si le robot a detecter un palet
											if(ultrasonic.hasPalet()) {
												ev3.arreter1();
												ev3.fermeture1Boost();		// il l'attrape
												ev3.arreter1();
												ultrasonic.UltrasonicClosest(360, 2.54f);		// refait une detection pour se situer
												ev3.arreter1();
												pilot.rotate(90);		// tourne a 90 degré 
												ev3.avancer1();
												while(!color2.IsWhite()) {ev3.avancer1();}		// avance jusqu'au blanc
												ev3.arreter1();
												ev3.avancer1();
												Delay.msDelay(100);
												ev3.arreter1();		// il depasse un peu le blanc
												
												/*
												 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blanche
												 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
												 */

												Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
												Motor.B.forward();
												Motor.B.setSpeed(150);
												while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
												ev3.arreter1();

												Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 100
												Motor.B.forward();
												Motor.B.setSpeed(50);
												while(!color2.IsGrey()) {Motor.B.forward();}	 // Le robot tourne jusqu'au gris 
												ev3.arreter1();
												Motor.B.setSpeed(300);
												
												
												/*
												 * le robot va suivre la ligne blanche jusqu'au mur
												 * ensuite il pourra faire la detetcion de camp
												 */
												ev3.HorsLigne();
												while(!ultrasonic.TestUltrasonic2(0.135f))
												{	
													// les deux if permettent le suivi de ligne
													if(color2.IsWhite()) ev3.SurLigne();
													if(color2.IsGrey()) ev3.HorsLigne();
												}
												// ici le robot tourne un peu plus que 90 degré, c'est pour s'assurer qu'il ne tappera pas dans le mur
												pilot.rotate(95);
												ev3.avancer1();
												
												/*
												 * Ceci est le test de camp
												 * le robot avncera dans la longueur du terrain 
												 * deux compteur tournent pendant l'execution de la boucle
												 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
												 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
												 * si c'est le bon coté le robot depose le palet
												 * sinon il fait demi tour et depose le palet de lautre coté
												 */
												while(!ultrasonic.TestUltrasonic2(0.15f))
												{	
													// les deux compteur qui augmentent a chaque tour de boucle
													cmp=cmp+1;
													cmp2=cmp+1;
											
													if(color2.IsGreen()) {cmpV=cmp;}		// validation de la variable pour le Vert
													if(color2.IsBlue()) {cmpB=cmp2;}		// validation de la variable pour le Bleu
													if(color2.IsWhite()) {break;}	
														
												}
												
												//Si la variable pour le vert est plus grande c'est qu'il est du mauvais coté
												if(cmpB<cmpV) { 
												pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
												ev3.avancer1();
												while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
												ev3.arreter1();
												ev3.ouverture1Boost();		// dépose le palet
												System.exit(0);		// fin du programme
													
												}
												
												//Si la variable pour le vert est plus petite c'est qu'il est du bon coté
												if(cmpB>cmpV) { 
												ev3.avancer1();
												while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
												ev3.arreter1();
												ev3.ouverture1Boost();		// dépose le palet
												System.exit(0);		// fin du programme
													
												}
												}
												
											
											/*
											 * le robot n'a rien detecté 
											 * il va alors faire une derniere detection
											 */
												else {
													ev3.avancer1();
													// le robot avance jusqu'a la ligne blanche
													while(!ultrasonic.TestUltrasonic2(0.135f))
													{
														
														// les trois if permettent le suivi de ligne
														if(color2.IsBlack()) ev3.HorsLigne();
														if(color2.IsGrey()) ev3.SurLigne();
														if(color2.IsBlue()) ev3.SurLigne();
														
														//ce if permet de sortir de la boucle quand le robot rencontre du blanc
														if(color2.IsWhite()) {ev3.arreter1(); break;}
														
													}
													
													
													pilot.rotate(170);		// le robot fait moins de 180 degré pour terminer sa rotation sur la ligne noir avec la rotation en deux temps

													Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
													Motor.B.forward();
													Motor.B.setSpeed(150);
													while(!color2.IsBlack()) {Motor.B.forward();}		// Le robot tourne jusqu'au noir
													ev3.arreter1();

													Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
													Motor.B.forward();
													Motor.B.setSpeed(50);
													while(!color2.IsGrey()) {Motor.B.forward();}		// Le robot tourne jusqu'au gris 
													ev3.arreter1();
													Motor.B.setSpeed(300);
													
													/*
													 * le robot va suivre la ligne noire
													 * il s'arretera a la ligne verte
													 */
													ev3.HorsLigne();
													while(!ultrasonic.TestUltrasonic2(0.135f))
													{	
														// les deux ifs permettent le suivi de ligne
														if(color2.IsBlack()) ev3.SurLigne();
														if(color2.IsGrey()) ev3.HorsLigne();
														
														//ce if permet de sortir de la boucle quand le robot va rencontrer du vert
														if(color2.IsGreen()) {ev3.arreter1(); break;}
													}
													
													
						//////////////////////////////PETITE DETECTION NUMERO 2 ///////////////////
													// le robot fait une detection sur un angle de 360 degré et avec une porté de 0.6m
													ultrasonic.UltrasonicClosest(360, 0.6f);
													
													// si le robot a detecter un palet
													if(ultrasonic.hasPalet()) {
														ev3.arreter1();
														ev3.fermeture1Boost();		// il l'attrape
														ev3.arreter1();
														ultrasonic.UltrasonicClosest(360, 2.54f);		// refait une detection pour se situer
														ev3.arreter1();
														pilot.rotate(90);		// tourne a 90 degré 
														ev3.avancer1();
														while(!color2.IsWhite()) {ev3.avancer1();}		// avance jusqu'au blanc
														ev3.arreter1();
														ev3.avancer1();
														Delay.msDelay(100);
														ev3.arreter1();		// il depasse un peu le blanc
														
														/*
														 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blanche
														 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
														 */

														Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
														Motor.B.forward();
														Motor.B.setSpeed(150);
														while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
														ev3.arreter1();

														Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 100
														Motor.B.forward();
														Motor.B.setSpeed(50);
														while(!color2.IsGrey()) {Motor.B.forward();}	 // Le robot tourne jusqu'au gris 
														ev3.arreter1();
														Motor.B.setSpeed(300);
														
														/*
														 * le robot va suivre la ligne blanche jusqu'au mur
														 * ensuite il pourra faire la detetcion de camp
														 */
														
														ev3.HorsLigne();
														while(!ultrasonic.TestUltrasonic2(0.135f))
														{
															if(color2.IsWhite()) ev3.SurLigne();
															if(color2.IsGrey()) ev3.HorsLigne();
														}
														
														
														// ici le robot tourne un peu plus que 90 degré, c'est pour s'assurer qu'il ne tappera pas dans le mur
														pilot.rotate(95);
														ev3.avancer1();
														
														/*
														 * Ceci est le test de camp
														 * le robot avncera dans la longueur du terrain 
														 * deux compteur tournent pendant l'execution de la boucle
														 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
														 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
														 * si c'est le bon coté le robot depose le palet
														 * sinon il fait demi tour et depose le palet de lautre coté
														 */
														while(!ultrasonic.TestUltrasonic2(0.15f))
														{	
															// les deux compteur qui augmentent a chaque tour de boucle
															cmp=cmp+1;
															cmp2=cmp+1;
													
															if(color2.IsGreen()) {cmpV=cmp;}		// validation de la variable pour le Vert
															if(color2.IsBlue()) {cmpB=cmp2;}		// validation de la variable pour le Bleu
															if(color2.IsWhite()) {break;}	
																
														}
														
														//Si la variable pour le vert est plus grande c'est qu'il est du mauvais coté
														if(cmpB<cmpV) { 
														pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
														ev3.avancer1();
														while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
														ev3.arreter1();
														ev3.ouverture1Boost();		// dépose le palet
														System.exit(0);		// fin du programme
															
														}
														
														//Si la variable pour le vert est plus petite c'est qu'il est du bon coté
														if(cmpB>cmpV) { 
														ev3.avancer1();
														while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
														ev3.arreter1();
														ev3.ouverture1Boost();		// dépose le palet
														System.exit(0);		// fin du programme
															
														}
													}
													
													
											}
											
								
									}
				
							}
					}
			}
						
								
									
		
	
		else if(button==8) {
///////////////////PREMIER TEST N'IMPORTE OU SUR LE TERRAIN//////////////
	ev3.ouverture1Boost();
	ultrasonic.UltrasonicClosest(360, 2.54f);  // le robot va detecter l'element le plus proche autour de lui sur un axe de 360 degré et avec une porté de 2m54
	ev3.avancer1();								// il avance
	
	//Si le robot a detecté un palet
	if(ultrasonic.hasPalet()) {
		ev3.arreter1();
		ev3.fermeture1Boost();		// il attrape le palet	
		ev3.arreter1();
		/*
		 *  il redetecter l'element le plus proche autour de lui sur un axe de 360 degré et avec une porté de 2m54
		 *  ce sera forcement un mur car le palet est dans ses pinces
		 *  c'est ce qui lui permettra de pouvoir l'orienté par la suite
		 */
		ultrasonic.UltrasonicClosest(360, 2.54f);	// il fait la detection
		ev3.arreter1();
		pilot.rotate(90);			// tourne a 90 degré
		ev3.avancer1();
		while(!color2.IsWhite()) {ev3.avancer1();}	//avance jusqu'a la ligne blanche
		ev3.arreter1();
		ev3.avancer1();
		Delay.msDelay(100);
		ev3.arreter1();			// la dépasse un peu
		
		/*
		 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blache
		 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
		 */

		Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
		Motor.B.forward();
		Motor.B.setSpeed(150);
		while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
		ev3.arreter1();

		Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
		Motor.B.forward();
		Motor.B.setSpeed(50);		
		while(!color2.IsGrey()) {Motor.B.forward();}		// Le robot tourne jusqu'au gris 
		ev3.arreter1();
		Motor.B.setSpeed(300);
		
		/*
		 * le robot avancera ensuite jusqu'au mur
		 * ensuite il pivotera a 90 degré 
		 */
		
		ev3.HorsLigne();
		while(!ultrasonic.TestUltrasonic2(0.135f))
		{
			// les deux if permettent le suivi de ligne
			if(color2.IsWhite()) ev3.SurLigne();
			if(color2.IsGrey()) ev3.HorsLigne();
		}
		
		// ici le robot tourne un peu plus que 90 degré, c'est pour s'assurer qu'il ne tappera pas dans le mur
		pilot.rotate(95);
		ev3.avancer1();
		
		/*
		 * Ceci est le test de camp
		 * le robot avncera dans la longueur du terrain 
		 * deux compteur tournent pendant l'execution de la boucle
		 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
		 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
		 * si c'est le bon coté le robot depose le palet
		 * sinon il fait demi tour et depose le palet de lautre coté
		 */
		while(!ultrasonic.TestUltrasonic2(0.15f))
		{	
			// les deux compteur qui augmentent a chaque tour de boucle
			cmp=cmp+1;
			cmp2=cmp+1;
	
			if(color2.IsGreen()) {cmpV=cmp;}		// validation de la variable pour le Vert
			if(color2.IsBlue()) {cmpB=cmp2;}		// validation de la variable pour le Bleu
			if(color2.IsWhite()) {break;}	
				
		}
		
		//Si la variable pour le vert est plus petite c'est qu'il est du mauvais coté
		if(cmpB>cmpV) { 
		pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
		ev3.avancer1();
		while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
		ev3.arreter1();
		ev3.ouverture1Boost();		// dépose le palet
		System.exit(0);		// fin du programme
			
		}
		
		//Si la variable pour le vert est plus grande c'est qu'il est du bon coté
		if(cmpB<cmpV) { 
		ev3.avancer1();
		while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
		ev3.arreter1();
		ev3.ouverture1Boost();		// dépose le palet
		System.exit(0);		// fin du programme
			
		}
		}
		
	
	//On rentre dans le sinon si la premiere detection était un mur
		else {
			
			
			ev3.arreter1();
			pilot.rotate(90);  // il tourne a 90 degré
			
	
			/*
			 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blanche
			 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
			 */
		
			ev3.avancer1();
			while(!color2.IsWhite()) {ev3.avancer1();}		//s'arrete au blanc
			ev3.arreter1();
			ev3.avancer1();
			Delay.msDelay(100);		
			ev3.arreter1();				//depasse un peu la ligne

			Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
			Motor.B.forward();
			Motor.B.setSpeed(150);
			while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
			ev3.arreter1();

			Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
			Motor.B.forward();
			Motor.B.setSpeed(50);
			while(!color2.IsGrey()) {Motor.B.forward();}		 // Le robot tourne jusqu'au gris 
			ev3.arreter1();
			Motor.B.setSpeed(300);
			ev3.arreter1();
			
			/*
			 * ceci sert a placer le palet devant le robot si il l'a attrapé dans ses pince durant le trajet
			 * car le palet peut de trouver dans la pince et ne pas actionner le capteur de pression
			 * donc on ferme les pince , puis on les ouvres , pour que le palet se retouve devant lui si il y en avait un
			 */
			ev3.fermeture1Boost();
			ev3.ouverture1Boost();
			
			
		//////AVANCE SUR LA LIGNE BLANCHE/////
			
			/*
			 * Le robot avancera sur la ligne blanche tant qu'il ne rencontrera ni un palet ni un mur
			 * si il rencontre un palet , il le ramenera derriere la ligne blanche
			 * si il rencontre un mur , il passe la suite et continu son chemin
			 */
			
				ev3.HorsLigne();
				while(!ultrasonic.TestUltrasonic2(0.19f))
				{
					// les deux if permettent le suivi de ligne
					if(color2.IsWhite()) ev3.SurLigne();
					if(color2.IsGrey()) ev3.HorsLigne();
					
				/*
				 * si il le capteur de pression est activé c'est qu'il avait un palet dans les pinces
				 * il commence alors a aller se placer pour detecter les camps et deposer ensuite le palet
				 */
					if(touch.isPressed()) {
					ev3.arreter1();
					ev3.fermeture1Boost();  // il attrape le palet
					
					
					ev3.arreter1();
					ev3.avancer1();
					Delay.msDelay(1000);
					ev3.arreter1();		// il avance un peu pour se decaler du mur
					
					pilot.rotate(90);		// tourne a 90 degré
					ev3.avancer1();
					
					/*
					 * Ceci est le test de camp
					 * le robot avncera dans la longueur du terrain 
					 * deux compteur tournent pendant l'execution de la boucle
					 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
					 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
					 * si c'est le bon coté le robot depose le palet
					 * sinon il fait demi tour et depose le palet de lautre coté
					 */
					
					while(!ultrasonic.TestUltrasonic2(0.15f))
					{
						// les deux compteur qui augmentent a chaque tour de boucle
						cmp=cmp+1;
						cmp2=cmp+1;
				
						if(color2.IsGreen()) {cmpV=cmp;}		//validation du vert
						if(color2.IsBlue()) {cmpB=cmp2;}		//validation du bleu
						if(color2.IsWhite()) {break;}	
							
					}
					
					//Si la variable pour le vert est plus petite c'est qu'il est du mauvais coté
					if(cmpB>cmpV) { 
					pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
					ev3.avancer1();
					while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
					ev3.arreter1();
					ev3.ouverture1Boost();		// dépose le palet
					System.exit(0);		// fin du programme
						
					}
					
					//Si la variable pour le vert est plus grande c'est qu'il est du bon coté
					if(cmpB<cmpV) { 
					ev3.avancer1();
					while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
					ev3.arreter1();
					ev3.ouverture1Boost();		// dépose le palet
					System.exit(0);		// fin du programme
					}}
				}
				
		/*
		 * le robot est arrivé au mur
		 * il va tourné et avancer jusqu'a la ligne noir
		 * il effectura une detection a 180 degré et avec une porté d'1.1m pour evité de faire un scan qui depasserai les limites du terrain
		 */
				
				pilot.rotate(90);		// il tourne a 90 degré
				ev3.avancer1();
				while(!color2.IsBlack()) {ev3.avancer1();}		// avance jusqu'a noir
				ev3.arreter1();
				
				//////////////////////////////////////////
				///////////////////////////
				ultrasonic.UltrasonicClosest(180, 1.1f);		// effectue une detection sur un axe de 180 degré et avec une portée d'1.1m
				if(ultrasonic.hasPalet()) {
					ev3.arreter1();
					ev3.fermeture1Boost();		// il attrape le palet	
					ev3.arreter1();
					/*
					 *  il redetecter l'element le plus proche autour de lui sur un axe de 360 degré et avec une porté de 2m54
					 *  ce sera forcement un mur car le palet est dans ses pinces
					 *  c'est ce qui lui permettra de pouvoir l'orienté par la suite
					 */
					ultrasonic.UltrasonicClosest(360, 2.54f);	// il fait la detection
					ev3.arreter1();
					pilot.rotate(90);			// tourne a 90 degré
					ev3.avancer1();
					while(!color2.IsWhite()) {ev3.avancer1();}	//avance jusqu'a la ligne blanche
					ev3.arreter1();
					ev3.avancer1();
					Delay.msDelay(100);
					ev3.arreter1();			// la dépasse un peu


					/*
					 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blache
					 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
					 */

					Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
					Motor.B.forward();
					Motor.B.setSpeed(150);
					while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
					ev3.arreter1();

					Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
					Motor.B.forward();
					Motor.B.setSpeed(50);		
					while(!color2.IsGrey()) {Motor.B.forward();}		// Le robot tourne jusqu'au gris 
					ev3.arreter1();
					Motor.B.setSpeed(300);
					
					/*
					 * le robot avancera ensuite jusqu'au mur
					 * ensuite il pivotera a 90 degré 
					 */
					
					ev3.HorsLigne();
					while(!ultrasonic.TestUltrasonic2(0.135f))
					{
						// les deux if permettent le suivi de ligne
						if(color2.IsWhite()) ev3.SurLigne();
						if(color2.IsGrey()) ev3.HorsLigne();
					}
					
					// ici le robot tourne un peu plus que 90 degré, c'est pour s'assurer qu'il ne tappera pas dans le mur
					pilot.rotate(95);
					ev3.avancer1();
					
					/*
					 * Ceci est le test de camp
					 * le robot avncera dans la longueur du terrain 
					 * deux compteur tournent pendant l'execution de la boucle
					 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
					 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
					 * si c'est le bon coté le robot depose le palet
					 * sinon il fait demi tour et depose le palet de lautre coté
					 */
					while(!ultrasonic.TestUltrasonic2(0.15f))
					{	
						// les deux compteur qui augmentent a chaque tour de boucle
						cmp=cmp+1;
						cmp2=cmp+1;
				
						if(color2.IsGreen()) {cmpV=cmp;}		// validation de la variable pour le Vert
						if(color2.IsBlue()) {cmpB=cmp2;}		// validation de la variable pour le Bleu
						if(color2.IsWhite()) {break;}	
							
					}
					
					//Si la variable pour le vert est plus petite c'est qu'il est du mauvais coté
					if(cmpB>cmpV) { 
					pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
					ev3.avancer1();
					while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
					ev3.arreter1();
					ev3.ouverture1Boost();		// dépose le palet
					System.exit(0);		// fin du programme
						
					}
					
					//Si la variable pour le vert est plus grande c'est qu'il est du bon coté
					if(cmpB<cmpV) { 
					ev3.avancer1();
					while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
					ev3.arreter1();
					ev3.ouverture1Boost();		// dépose le palet
					System.exit(0);		// fin du programme
						
					}
					}
					
				
				/*
				 * le robot n'a rien detecté
				 * il va alors continuer son chemin pour aller jusqu'a la ligne blanche
				 */
					else {	
						pilot.rotate(2);		// on le fait devier un petit peu pour ne pas qu'il ne rentre dans le mur
						ev3.arreter1();
						

						/*
						 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blanche
						 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
						 */
					
						ev3.avancer1();
						while(!color2.IsWhite()) {ev3.avancer1();}		//s'arrete au blanc
						ev3.arreter1();
						ev3.avancer1();
						Delay.msDelay(100);		
						ev3.arreter1();				//depasse un peu la ligne

						Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
						Motor.B.forward();
						Motor.B.setSpeed(150);
						while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
						ev3.arreter1();

						Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
						Motor.B.forward();
						Motor.B.setSpeed(50);
						while(!color2.IsGrey()) {Motor.B.forward();}		 // Le robot tourne jusqu'au gris 
						ev3.arreter1();
						Motor.B.setSpeed(300);
						ev3.arreter1();
						
						/*
						 * ceci sert a placer le palet devant le robot si il l'a attrapé dans ses pince durant le trajet
						 * car le palet peut de trouver dans la pince et ne pas actionner le capteur de pression
						 * donc on ferme les pince , puis on les ouvres , pour que le palet se retouve devant lui si il y en avait un
						 */
						ev3.fermeture1Boost();
						ev3.ouverture1Boost();
						

						/*
						 * Le robot avancera sur la ligne blanche tant qu'il ne rencontrera ni un palet ni un mur
						 * si il rencontre un palet , il le ramenera derriere la ligne blanche
						 * si il rencontre un mur , il passe la suite et continu son chemin
						 */
						
							ev3.HorsLigne();
							while(!ultrasonic.TestUltrasonic2(0.19f))
							{
								// les deux if permettent le suivi de ligne
								if(color2.IsWhite()) ev3.SurLigne();
								if(color2.IsGrey()) ev3.HorsLigne();
								
							/*
							 * si il le capteur de pression est activé c'est qu'il avait un palet dans les pinces
							 * il commence alors a aller se placer pour detecter les camps et deposer ensuite le palet
							 */
								if(touch.isPressed()) {
								ev3.arreter1();
								ev3.fermeture1Boost();  // il attrape le palet
								
								
								ev3.arreter1();
								ev3.avancer1();
								Delay.msDelay(1000);
								ev3.arreter1();		// il avance un peu pour se decaler du mur
								
								pilot.rotate(90);		// tourne a 90 degré
								ev3.avancer1();
								
								/*
								 * Ceci est le test de camp
								 * le robot avncera dans la longueur du terrain 
								 * deux compteur tournent pendant l'execution de la boucle
								 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
								 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
								 * si c'est le bon coté le robot depose le palet
								 * sinon il fait demi tour et depose le palet de lautre coté
								 */
								
								while(!ultrasonic.TestUltrasonic2(0.15f))
								{
									// les deux compteur qui augmentent a chaque tour de boucle
									cmp=cmp+1;
									cmp2=cmp+1;
							
									if(color2.IsGreen()) {cmpV=cmp;}		//validation du vert
									if(color2.IsBlue()) {cmpB=cmp2;}		//validation du bleu
									if(color2.IsWhite()) {break;}	
										
								}
								//Si la variable pour le vert est plus petite c'est qu'il est du mauvais coté
								if(cmpB>cmpV) { 
								pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
								ev3.avancer1();
								while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
								ev3.arreter1();
								ev3.ouverture1Boost();		// dépose le palet
								System.exit(0);		// fin du programme
									
								}
								
								//Si la variable pour le vert est plus grande c'est qu'il est du bon coté
								if(cmpB<cmpV) { 
								ev3.avancer1();
								while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
								ev3.arreter1();
								ev3.ouverture1Boost();		// dépose le palet
								System.exit(0);		// fin du programme
								}}
							}
							
							/*
							 * le robot est arrivé au mur
							 * il va tourné et avancer jusqu'a la ligne noir
							 * il effectura une detection a 180 degré et avec une porté d'1.1m pour evité de faire un scan qui depasserai les limites du terrain
							 */
									
									pilot.rotate(90);		// il tourne a 90 degré
									ev3.avancer1();
									while(!color2.IsBlack()) {ev3.avancer1();}		// avance jusqu'a noir
									ev3.arreter1();
									
									//////////////////////////////////////////
									///////////////////////////
									ultrasonic.UltrasonicClosest(180, 1.1f);		// effectue une detection sur un axe de 180 degré et avec une portée d'1.1m
									if(ultrasonic.hasPalet()) {
										ev3.arreter1();
										ev3.fermeture1Boost();		// il attrape le palet	
										ev3.arreter1();
										/*
										 *  il redetecter l'element le plus proche autour de lui sur un axe de 360 degré et avec une porté de 2m54
										 *  ce sera forcement un mur car le palet est dans ses pinces
										 *  c'est ce qui lui permettra de pouvoir l'orienté par la suite
										 */
										ultrasonic.UltrasonicClosest(360, 2.54f);	// il fait la detection
										ev3.arreter1();
										pilot.rotate(90);			// tourne a 90 degré
										ev3.avancer1();
										while(!color2.IsWhite()) {ev3.avancer1();}	//avance jusqu'a la ligne blanche
										ev3.arreter1();
										ev3.avancer1();
										Delay.msDelay(100);
										ev3.arreter1();			// la dépasse un peu
				 

										/*
										 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blache
										 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
										 */

										Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
										Motor.B.forward();
										Motor.B.setSpeed(150);
										while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
										ev3.arreter1();

										Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
										Motor.B.forward();
										Motor.B.setSpeed(50);		
										while(!color2.IsGrey()) {Motor.B.forward();}		// Le robot tourne jusqu'au gris 
										ev3.arreter1();
										Motor.B.setSpeed(300);
										
										/*
										 * le robot avancera ensuite jusqu'au mur
										 * ensuite il pivotera a 90 degré 
										 */
										
										ev3.HorsLigne();
										while(!ultrasonic.TestUltrasonic2(0.135f))
										{
											// les deux if permettent le suivi de ligne
											if(color2.IsWhite()) ev3.SurLigne();
											if(color2.IsGrey()) ev3.HorsLigne();
										}
										
										// ici le robot tourne un peu plus que 90 degré, c'est pour s'assurer qu'il ne tappera pas dans le mur
										pilot.rotate(95);
										ev3.avancer1();
										
										/*
										 * Ceci est le test de camp
										 * le robot avncera dans la longueur du terrain 
										 * deux compteur tournent pendant l'execution de la boucle
										 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
										 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
										 * si c'est le bon coté le robot depose le palet
										 * sinon il fait demi tour et depose le palet de lautre coté
										 */
										while(!ultrasonic.TestUltrasonic2(0.15f))
										{	
											// les deux compteur qui augmentent a chaque tour de boucle
											cmp=cmp+1;
											cmp2=cmp+1;
									
											if(color2.IsGreen()) {cmpV=cmp;}		// validation de la variable pour le Vert
											if(color2.IsBlue()) {cmpB=cmp2;}		// validation de la variable pour le Bleu
											if(color2.IsWhite()) {break;}	
												
										}
										
										//Si la variable pour le vert est plus petite c'est qu'il est du mauvais coté
										if(cmpB>cmpV) { 
										pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
										ev3.avancer1();
										while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
										ev3.arreter1();
										ev3.ouverture1Boost();		// dépose le palet
										System.exit(0);		// fin du programme
											
										}
										
										//Si la variable pour le vert est plus grande c'est qu'il est du bon coté
										if(cmpB<cmpV) { 
										ev3.avancer1();
										while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
										ev3.arreter1();
										ev3.ouverture1Boost();		// dépose le palet
										System.exit(0);		// fin du programme
											
										}
										}
										
									
									/*
									 * le robot n'a rien detecté
									 * il va alors continuer son chemin pour aller jusqu'a la ligne blanche
									 */
										else {	
											pilot.rotate(2);		// on le fait devier un petit peu pour ne pas qu'il ne rentre dans le mur
											ev3.arreter1();
									
							/////TOURNE SUR LA LIGNE BLANCHE////////////
								
								
											/*
											 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blanche
											 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
											 */
										
											ev3.avancer1();
											while(!color2.IsWhite()) {ev3.avancer1();}		//s'arrete au blanc
											ev3.arreter1();
											ev3.avancer1();
											Delay.msDelay(100);		
											ev3.arreter1();				//depasse un peu la ligne

											Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
											Motor.B.forward();
											Motor.B.setSpeed(150);
											while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
											ev3.arreter1();

											Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
											Motor.B.forward();
											Motor.B.setSpeed(50);
											while(!color2.IsGrey()) {Motor.B.forward();}		 // Le robot tourne jusqu'au gris 
											ev3.arreter1();
											Motor.B.setSpeed(300);
											ev3.arreter1();
											
											
								//////AVANCE SUR LA LIGNE BLANCHE/////
									// le robot va avancer le long de la ligne blanche jusqu'au mur
										ev3.HorsLigne();
										while(!ultrasonic.TestUltrasonic2(0.135f))
										{
											// les deux if permettent le suivi de ligne
											if(color2.IsWhite()) ev3.SurLigne();
											if(color2.IsGrey()) ev3.HorsLigne();
										}
										
								/*
								 * le robot va aller se placer le long le la ligne noir 
								 * pour cela il fera une rotation a 90 degré
								 * ensuite il avncera un petit peu 
								 * il fera ensuite une nouvelle rotation pour ensuite avancer jusqu'a la ligne noire
								 */
										
										pilot.rotate(90);		// tourne a 90 degré
										ev3.avancer1();
										Delay.msDelay(500);		// avance un petit peu
										ev3.arreter1();
										pilot.rotate(90);		// tourne a 90 degré
										ev3.avancer1();
										while(!color2.IsBlack()) {ev3.avancer1();}		// avance jusqu'au noir
										ev3.arreter1();
										ev3.avancer1();
										Delay.msDelay(50);
										ev3.arreter1();		// depasse un peu la ligne noire
										ev3.avancer1();
										Delay.msDelay(100);
										ev3.arreter1();

										Motor.A.setSpeed(150);		// on met la vitesse du moteur A a 150
										Motor.A.forward();
										Motor.A.setSpeed(150);	
										while(!color2.IsBlack()) {Motor.A.forward();}		// Le robot tourne jusqu'au noir
										ev3.arreter1();

										Motor.A.setSpeed(50);		/// on met la vitesse du moteur B a 50
										Motor.A.forward();
										Motor.A.setSpeed(50);
										while(!color2.IsGrey()) {Motor.A.forward();}		// Le robot tourne jusqu'au gris
										ev3.arreter1();
										Motor.B.setSpeed(300);
										
										/*
										 * le robot suivra la ligne noir jusqu'a la ligne bleu
										 */
										while(!ultrasonic.TestUltrasonic2(0.135f))
										{
											// les deux if permettent le suivi de ligne
											if(color2.IsBlack()) ev3.HorsLigne();
											if(color2.IsGrey()) ev3.SurLigne();
											if(color2.IsBlue()) {ev3.arreter1(); break;}		// quand il voit du bleu , il s'arrete et sort de la boucle
											
										}
										
										//////////////////////////////PETITE DETECTION NUMERO 1 ///////////////////
										
										// le robot fait une detection sur un angle de 360 degré et avec une porté de 0.6m
										ultrasonic.UltrasonicClosest(360, 0.6f);
										
										// si le robot a detecter un palet
										if(ultrasonic.hasPalet()) {
											ev3.arreter1();
											ev3.fermeture1Boost();		// il l'attrape
											ev3.arreter1();
											ultrasonic.UltrasonicClosest(360, 2.54f);		// refait une detection pour se situer
											ev3.arreter1();
											pilot.rotate(90);		// tourne a 90 degré 
											ev3.avancer1();
											while(!color2.IsWhite()) {ev3.avancer1();}		// avance jusqu'au blanc
											ev3.arreter1();
											ev3.avancer1();
											Delay.msDelay(100);
											ev3.arreter1();		// il depasse un peu le blanc
											
											/*
											 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blanche
											 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
											 */

											Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
											Motor.B.forward();
											Motor.B.setSpeed(150);
											while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
											ev3.arreter1();

											Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 100
											Motor.B.forward();
											Motor.B.setSpeed(50);
											while(!color2.IsGrey()) {Motor.B.forward();}	 // Le robot tourne jusqu'au gris 
											ev3.arreter1();
											Motor.B.setSpeed(300);
											
											
											/*
											 * le robot va suivre la ligne blanche jusqu'au mur
											 * ensuite il pourra faire la detetcion de camp
											 */
											ev3.HorsLigne();
											while(!ultrasonic.TestUltrasonic2(0.135f))
											{	
												// les deux if permettent le suivi de ligne
												if(color2.IsWhite()) ev3.SurLigne();
												if(color2.IsGrey()) ev3.HorsLigne();
											}
											// ici le robot tourne un peu plus que 90 degré, c'est pour s'assurer qu'il ne tappera pas dans le mur
											pilot.rotate(95);
											ev3.avancer1();
											
											/*
											 * Ceci est le test de camp
											 * le robot avncera dans la longueur du terrain 
											 * deux compteur tournent pendant l'execution de la boucle
											 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
											 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
											 * si c'est le bon coté le robot depose le palet
											 * sinon il fait demi tour et depose le palet de lautre coté
											 */
											while(!ultrasonic.TestUltrasonic2(0.15f))
											{	
												// les deux compteur qui augmentent a chaque tour de boucle
												cmp=cmp+1;
												cmp2=cmp+1;
										
												if(color2.IsGreen()) {cmpV=cmp;}		// validation de la variable pour le Vert
												if(color2.IsBlue()) {cmpB=cmp2;}		// validation de la variable pour le Bleu
												if(color2.IsWhite()) {break;}	
													
											}
											//Si la variable pour le vert est plus petite c'est qu'il est du mauvais coté
											if(cmpB>cmpV) { 
											pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
											ev3.avancer1();
											while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
											ev3.arreter1();
											ev3.ouverture1Boost();		// dépose le palet
											System.exit(0);		// fin du programme
												
											}
											
											//Si la variable pour le vert est plus grande c'est qu'il est du bon coté
											if(cmpB<cmpV) { 
											ev3.avancer1();
											while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
											ev3.arreter1();
											ev3.ouverture1Boost();		// dépose le palet
											System.exit(0);		// fin du programme
												
											}
											}
											
										
										/*
										 * le robot n'a rien detecté 
										 * il va alors faire une derniere detection
										 */
											else {
												ev3.avancer1();
												// le robot avance jusqu'a la ligne blanche
												while(!ultrasonic.TestUltrasonic2(0.135f))
												{
													
													// les trois if permettent le suivi de ligne
													if(color2.IsBlack()) ev3.HorsLigne();
													if(color2.IsGrey()) ev3.SurLigne();
													if(color2.IsBlue()) ev3.SurLigne();
													
													//ce if permet de sortir de la boucle quand le robot rencontre du blanc
													if(color2.IsWhite()) {ev3.arreter1(); break;}
													
												}
												
												
												pilot.rotate(170);		// le robot fait moins de 180 degré pour terminer sa rotation sur la ligne noir avec la rotation en deux temps

												Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
												Motor.B.forward();
												Motor.B.setSpeed(150);
												while(!color2.IsBlack()) {Motor.B.forward();}		// Le robot tourne jusqu'au noir
												ev3.arreter1();

												Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 50
												Motor.B.forward();
												Motor.B.setSpeed(50);
												while(!color2.IsGrey()) {Motor.B.forward();}		// Le robot tourne jusqu'au gris 
												ev3.arreter1();
												Motor.B.setSpeed(300);
												
												/*
												 * le robot va suivre la ligne noire
												 * il s'arretera a la ligne verte
												 */
												ev3.HorsLigne();
												while(!ultrasonic.TestUltrasonic2(0.135f))
												{	
													// les deux ifs permettent le suivi de ligne
													if(color2.IsBlack()) ev3.SurLigne();
													if(color2.IsGrey()) ev3.HorsLigne();
													
													//ce if permet de sortir de la boucle quand le robot va rencontrer du vert
													if(color2.IsGreen()) {ev3.arreter1(); break;}
												}
												
												
					//////////////////////////////PETITE DETECTION NUMERO 2 ///////////////////
												// le robot fait une detection sur un angle de 360 degré et avec une porté de 0.6m
												ultrasonic.UltrasonicClosest(360, 0.6f);
												
												// si le robot a detecter un palet
												if(ultrasonic.hasPalet()) {
													ev3.arreter1();
													ev3.fermeture1Boost();		// il l'attrape
													ev3.arreter1();
													ultrasonic.UltrasonicClosest(360, 2.54f);		// refait une detection pour se situer
													ev3.arreter1();
													pilot.rotate(90);		// tourne a 90 degré 
													ev3.avancer1();
													while(!color2.IsWhite()) {ev3.avancer1();}		// avance jusqu'au blanc
													ev3.arreter1();
													ev3.avancer1();
													Delay.msDelay(100);
													ev3.arreter1();		// il depasse un peu le blanc
													
													/*
													 *  Les prochaines lignes servent a faire tourner le robot sur la ligne blanche
													 *  puis ensuite jusqu'au gris , ce qui permet au robot de se mettre dans un meilleur axe pour la suite
													 */

													Motor.B.setSpeed(150);		// on met la vitesse du moteur B a 150
													Motor.B.forward();
													Motor.B.setSpeed(150);
													while(!color2.IsWhite()) {Motor.B.forward();}		// Le robot tourne jusqu'au blanc
													ev3.arreter1();

													Motor.B.setSpeed(50);		// on met la vitesse du moteur B a 100
													Motor.B.forward();
													Motor.B.setSpeed(50);
													while(!color2.IsGrey()) {Motor.B.forward();}	 // Le robot tourne jusqu'au gris 
													ev3.arreter1();
													Motor.B.setSpeed(300);
													
													/*
													 * le robot va suivre la ligne blanche jusqu'au mur
													 * ensuite il pourra faire la detetcion de camp
													 */
													
													ev3.HorsLigne();
													while(!ultrasonic.TestUltrasonic2(0.135f))
													{
														if(color2.IsWhite()) ev3.SurLigne();
														if(color2.IsGrey()) ev3.HorsLigne();
													}
													
													
													// ici le robot tourne un peu plus que 90 degré, c'est pour s'assurer qu'il ne tappera pas dans le mur
													pilot.rotate(95);
													ev3.avancer1();
													
													/*
													 * Ceci est le test de camp
													 * le robot avncera dans la longueur du terrain 
													 * deux compteur tournent pendant l'execution de la boucle
													 * des qu'il rencontrera la ligne vert et la ligne bleu une variable sera "validée"
													 * il y aura donc une valeur plus grande que l'autre selon le coté ou le robot vient , c'est ce qui permettra de savoir ou deposer le plaet
													 * si c'est le bon coté le robot depose le palet
													 * sinon il fait demi tour et depose le palet de lautre coté
													 */
													while(!ultrasonic.TestUltrasonic2(0.15f))
													{	
														// les deux compteur qui augmentent a chaque tour de boucle
														cmp=cmp+1;
														cmp2=cmp+1;
												
														if(color2.IsGreen()) {cmpV=cmp;}		// validation de la variable pour le Vert
														if(color2.IsBlue()) {cmpB=cmp2;}		// validation de la variable pour le Bleu
														if(color2.IsWhite()) {break;}	
															
													}
													
													//Si la variable pour le vert est plus petite c'est qu'il est du mauvais coté
													if(cmpB>cmpV) { 
													pilot.rotate(173);		// il fait demi tour , ici moins que 180 pour etre sur que le robot ne rencontrera pas un mur
													ev3.avancer1();
													while(!color2.IsWhite()) {ev3.avancer1();}		// il avance jusqu'au blanc
													ev3.arreter1();
													ev3.ouverture1Boost();		// dépose le palet
													System.exit(0);		// fin du programme
														
													}
													
													//Si la variable pour le vert est plus grande c'est qu'il est du bon coté
													if(cmpB<cmpV) { 
													ev3.avancer1();
													while(!color2.IsWhite()) {ev3.avancer1();}	// il avance jusqu'au blanc
													ev3.arreter1();
													ev3.ouverture1Boost();		// dépose le palet
													System.exit(0);		// fin du programme
														
													}
												}
												
												
										}
										
							
								}
			
						}
				}
		}
	}									
		

}
