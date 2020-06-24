//Zone import librairie sound//
import ddf.minim.*;
Minim minimTuto,minimPlayer;
AudioPlayer tuto1,tuto2,tuto3,tuto4,tuto5,shoot,saut;
AudioInput input;
//////////////////////////////

//Zone Variables joueur//
Joueur monJoueur;                              
float posJoueurX,posJoueurY,tJoueurX,tJoueurY,vitesseCourse,vitesseSaut; //Variables contenant respectivement position,  taille, vitesse de course et saut du joueur
int mouvementD,mouvementG,mouvementH; // Variables contenant les inputs
float vitGrav; // Variable qui contiendra la force de la gravité
///////////////////////

//Zone Variables platformes//
ArrayList<Platforme> sol = new ArrayList<Platforme>(); //Liste qui contient tout les objets que l'on considère comme "sol"
////////////////////////////

//Zone Variables projectiles//
ArrayList<Projectiles> projectiles = new ArrayList<Projectiles>(); //Liste qui contient tout les objets que l'on considère comme "projectiles"
////////////////////////////

//Zone Variables Ennemis//
ArrayList<Ennemi> ennemis = new ArrayList<Ennemi>(); //Liste qui contient tout les objets que l'on considère comme "ennemis"
float vitGravE; // Variable qui contient la force de la gravité des ennemis
////////////////////////////

//Zone Variables Ecran//
int largEcran,hautEcran; //Variables qui contient la résolution de lécran : Largeur / Hauteur
///////////////////////

//Zone Variables Etat du jeu//
int etatJeu,etapeTuto; // Entier représentant un état du jeu (Tutoriel,Jeu,Ecran Défaite,Ecran Victoire), De même pour les étapes du tutoriel
PImage zone1,zoneTuto; // Images des backgrounds : Jeu / Tutoriel (resp.)
/////////////////////////////

//Zone Variables Vagues//
int numVague,ennemiParVague,numEnnemiTued; // Variables stockant : Numéro de vague actuel, Le nombre d'ennemis par vague et le nombre d'ennemis tués (resp.)
////////////////////////

//zone Variable Timer//
int tempTotal;                   //Variable qui contient ( en ms) le temps écoulé depuis le lancement du sketch
int animationDegatDelay = 1000; //Variable qui contient la durée de l'animation de dégât
/////////////////////

//Reset de la position du joueur après le tutoriel//
boolean reset = false;                              //... No comment
///////////////////////////////////////////////////

///Zone Ecran défaite/victoire//
Animation defaite,victoire;
///////////////////////////



void setup(){
     

     //Initialisation taille fenêtre//
      largEcran = width;             
      hautEcran = height;
     //size(800,600);
     fullScreen();
     tempTotal = millis();        
     minimTuto = new Minim(this); // L'objet Minim destiné aux samples du tutoriel
     
     tuto1 =  minimTuto.loadFile("son/tuto/tuto1.mp3");tuto1.setGain(-10.0); ///On Charge les différents samples du tutoriel et on réduit leurs volumes
     tuto2 = minimTuto.loadFile("son/tuto/tuto2.mp3");tuto2.setGain(-10.0);  //...
     tuto3 = minimTuto.loadFile("son/tuto/tuto3.mp3");tuto3.setGain(-10.0); //...
     tuto4 = minimTuto.loadFile("son/tuto/tuto4.mp3");tuto4.setGain(-10.0); //...
     tuto5 = minimTuto.loadFile("son/tuto/tuto5.mp3");tuto5.setGain(-10.0); //...
     
      minimPlayer = new Minim(this); // L'objet Minim destiné aux samples du joueur   
      
     ////////////////////////////////
     
     
     
     //Initialisation objets platformes// // Une règle de trois est faite avec les positions des platformes de l'image initiale pour pouvoir adapter la position des platformes à toute résolution.
     sol.add(new Platforme((largEcran*616)/1950,(hautEcran * 220)/1080 ,(largEcran*225)/1950,0));
     
     sol.add(new Platforme(0,(hautEcran * 386)/1080 ,(largEcran*495)/1950,0.001));
     sol.add(new Platforme((largEcran*876)/1950,(hautEcran * 380)/1080 ,(largEcran*510)/1950,0));
     
     sol.add(new Platforme((largEcran*526)/1950,(hautEcran * 517)/1080 ,(largEcran*225)/1950,0));
     
     sol.add(new Platforme((largEcran*1256)/1950,(hautEcran * 604)/1080 ,(largEcran*800)/1950,0));
     sol.add(new Platforme((largEcran*156)/1950,(hautEcran * 720)/1080 ,(largEcran*704)/1950,0));
     ///////////////////////////////////
     
     
       //Initialisation des animations écrans défaite/Victoire///
     defaite = new Animation("Defeattest", "Screen/Defeat/",19,".png");
      victoire = new Animation("Victory", "Screen/Victory/",20,".png");
     //////////////////////////////////////////////////////////
     
     frameRate(30); // Réduction du nombre de frame par ms
     resetJeu(4);   //  On Invoque la fonction resetJeu() pour continuer à initialiser le reste des variables qui se doivent d'être rénitialisés en cas de "re-play"
 
}

void resetPositionJoueur(){//Fonction qui repositionne le joueur après le tutoriel
  monJoueur.position.set(largEcran/2,0);// Utilisation du PVecteur.set() pour attribuer de nouvelles valeures à PVecteur.x et PVecteur.y
}


void resetJeu(int etapeJeu){ //Fonction qui initialise toutes les variables qui doivent-être rénitialisés en début de partie si l'on recommence une partie
  
     //Initialisation etat jeu///
     etatJeu = etapeJeu;        //Le nouvel etat du jeu en fonction du paramètre etapeJeu
     etapeTuto = 1;             //Etape numéro du tutoriel
     textSize(150);
     zone1 = loadImage("zone/zones/zone1.png"); //Chargement du background de jeu
     zone1.resize(largEcran,hautEcran);            ///.... Ajustement de la taille de ce dernier à l'écran 
     zoneTuto= loadImage("zone/zones/zonetuto.png"); //Chargement du background de jeu
     zoneTuto.resize(largEcran,hautEcran);           ///.... Ajustement de la taille de ce dernier à l'écran 
     //////////////////////////
     
     //Initialisation objet joueur//
     posJoueurX = largEcran/2;posJoueurY = 0f; tJoueurX = largEcran/32; tJoueurY = hautEcran/16;
     vitesseCourse = 10;
     vitesseSaut = 22;
     monJoueur = new Joueur(posJoueurX,posJoueurY,tJoueurX,tJoueurY,vitesseCourse,vitesseSaut); // Création de l'objet Joueur
     numEnnemiTued = 0;numVague=1; //Le nombre d'ennemis tués en début de partie est initialisé à 0 et le numéro de la vague à 1
     /////////////////////////////
     
     if(ennemis.size() > 0){ // Si la liste contenant les ennemis n'est pas nulle
     for(int i = 0; i < ennemis.size();i++){ // On supprime tous les ennemis de la liste pour reprendre une partie
        
        Ennemi e = ennemis.get(i); //i-ème ennemi de la liste ennemis
        ennemis.remove(e);//On enlève le i-ème ennemi de la liste ennemis
     }
     
     }
}


void draw(){
  
   switch(etatJeu){ // On invoque les fonctions qui correspondent à l'état du jeu
     case 1: //etat1:écran de défaite
           imageMode(CENTER);
           defaite.display(largEcran/2, hautEcran/2,largEcran, hautEcran/2); // On affiche l'animation de "l'écran" défaite           
           imageMode(CORNER);
           fill(0,0,255);  
           textSize(30);
           text("Appuies sur \"r\" pour rejouer !",largEcran/2, hautEcran - hautEcran/4);
     break;
     case 2: //etat2: Zone 1 de jeu
            if(!reset){ //Système pour invoquer une unique fois une fonction dans draw()
              resetPositionJoueur(); //Repositionnement du joueur en début de partie
              reset = true;
            }
            background(zone1); // On charge le background du jeu
            updateJoueur(); //On invoque la fonction qui mets à jour tout ce qui concerne le joueur(voir onglet Update_Joueur)
            updateProjectiles();//On invoque la fonction qui mets à jour tout ce qui concerne les projectiles(voir onglet Update_Projectiles)
            updateEnnemis();//On invoque la fonction qui mets à jour tout ce qui concerne les ennemis(voir onglet Update_Ennemis)
            afficheInfoVague();// Fonction qui affiche l'indication du numéro de vague actuel
             
      break;
      case 3:// etat3: écran de victoire 
             imageMode(CENTER);
             victoire.display(largEcran/2, hautEcran/2,largEcran, hautEcran/2); // On affiche l'animation de "l'écran" victoire          
             imageMode(CORNER);
            fill(0,0,255);  

            textSize(30);
           text("Appuies sur \"r\" pour rejouer !",largEcran/2, hautEcran - hautEcran/4);
      break;
      case 4: //etat 4: tutoriel
            textSize((largEcran*30)/1950);
            background(zoneTuto);// On charge le background du Tutoriel
            updateJoueur();//On invoque la fonction qui mets à jour tout ce qui concerne le joueur(voir onglet Update_Joueur)
            updateProjectiles();//On invoque la fonction qui mets à jour tout ce qui concerne les projectiles(voir onglet Update_Projectiles)
            fill(0,0,255);  
            text("Appuies sur \"t\" pour passer le tutoriel", largEcran/20, hautEcran - hautEcran/12); // Texte à afficher à l'écran
                                  
             switch(etapeTuto){ //Switch concernant les différentes étapes du tutoriel
                   
                   case 1:
                          tuto1.play(); //On lance le sample de l'étape 1 du tutoriel
                          text("Appuies sur \"d\" pour te déplacer vers la droite !",largEcran/2, hautEcran/2); // Texte à afficher à l'écran
                   break;
                   case 2:
                         if(tuto1.isPlaying())tuto1.close(); // Test pour que l'indication sonore 1 ne se superpose pas avec la 2
                         tuto2.play();   //On lance le sample de l'étape 2 du tutoriel
                           text("Super! Appuies sur \"q\" pour te déplacer vers la gauche !",largEcran/2, hautEcran/2); // Texte à afficher à l'écran
                   break;
                   case 3:
                     if(tuto2.isPlaying())tuto2.close(); //Test pour que l'indication sonore 2 ne se superpose pas avec la 3
                         tuto3.play();//On lance le sample de l'étape 3 du tutoriel
                           text("T'es super doué !Appuies sur \"ESPACE\" pour sauter !",largEcran/2, hautEcran/2); // Texte à afficher à l'écran
                   break;
                   case 4:
                   if(tuto3.isPlaying())tuto3.close(); //Test pour que l'indication sonore 3 ne se superpose pas avec la 4
                   tuto4.play();//On lance le sample de l'étape 4 du tutoriel
                           text("Excellent !Appuies sur \"f\" pour utiliser ton arme !",largEcran/2, hautEcran/2); // Texte à afficher à l'écran
                   break;
                   case 5:
                         text("Appuies sur \"t\" si tu es prêt à combattre !",largEcran/2, hautEcran/2); // Texte à afficher à l'écran
                         monJoueur.afficheFleche();
                         if(tuto4.isPlaying())tuto4.close();// Test pour que l'indication sonore 4 ne se superpose pas avec la 5 
                            tuto5.play();//On lance le sample de l'étape 5 du tutoriel

             }
      
                  break;
      
     
    
   }
     
 
  }
