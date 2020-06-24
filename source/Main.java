import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Main extends PApplet {

//Zone import librairie sound//

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



public void setup(){
     

     //Initialisation taille fenêtre//
      largEcran = width;             
      hautEcran = height;
     //size(800,600);
     
     tempTotal = millis();        
     minimTuto = new Minim(this); // L'objet Minim destiné aux samples du tutoriel
     
     tuto1 =  minimTuto.loadFile("son/tuto/tuto1.mp3");tuto1.setGain(-10.0f); ///On Charge les différents samples du tutoriel et on réduit leurs volumes
     tuto2 = minimTuto.loadFile("son/tuto/tuto2.mp3");tuto2.setGain(-10.0f);  //...
     tuto3 = minimTuto.loadFile("son/tuto/tuto3.mp3");tuto3.setGain(-10.0f); //...
     tuto4 = minimTuto.loadFile("son/tuto/tuto4.mp3");tuto4.setGain(-10.0f); //...
     tuto5 = minimTuto.loadFile("son/tuto/tuto5.mp3");tuto5.setGain(-10.0f); //...
     
      minimPlayer = new Minim(this); // L'objet Minim destiné aux samples du joueur   
      
     ////////////////////////////////
     
     
     
     //Initialisation objets platformes// // Une règle de trois est faite avec les positions des platformes de l'image initiale pour pouvoir adapter la position des platformes à toute résolution.
     sol.add(new Platforme((largEcran*616)/1950,(hautEcran * 220)/1080 ,(largEcran*225)/1950,0));
     
     sol.add(new Platforme(0,(hautEcran * 386)/1080 ,(largEcran*495)/1950,0.001f));
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

public void resetPositionJoueur(){//Fonction qui repositionne le joueur après le tutoriel
  monJoueur.position.set(largEcran/2,0);// Utilisation du PVecteur.set() pour attribuer de nouvelles valeures à PVecteur.x et PVecteur.y
}


public void resetJeu(int etapeJeu){ //Fonction qui initialise toutes les variables qui doivent-être rénitialisés en début de partie si l'on recommence une partie
  
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


public void draw(){
  
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
//Inspiré de l'exemple de l'animated sprite sur processing.org avec gestion de l'extension des fichiers images
class Animation{

  PImage[] frames;
  int nbFrames;
  int frame;
  
  
  Animation (String animName,String path, int nb,String extension){
    
    nbFrames = nb;
    frames = new PImage[nbFrames];
    
    for(int i = 0; i < nbFrames;i++){
      String fileName = path + animName + i + extension;
      frames[i] = loadImage(fileName);

    }
    
  }
  
  public void display(float x,float y,float largeur,float longueur){
      frame = (frame+1) % nbFrames;
      image(frames[frame],x,y,largeur,longueur); 
      
  }
}
class Ennemi{
  //Variables propres à l'objet Ennemi//
  PVector position,taille;
  float vitesseGravite,vitesseCourse;
  int vie, vieMax, degatArme;
  boolean auSol,course,orienteD,attaque;

  
  //Zone animations avec personnage orienté à droite//
  Animation run = new Animation("enemy", "anims/enemy/enemy1/",10,".png");
  Animation degat = new Animation("enemydmg", "anims/enemy/enemy1/damages/",4,".png");
  ///////////////////////////////////////////////////
  
  //Zone animations avec personnage orienté à gauche//
  Animation leftrun = new Animation("enemyleft", "anims/enemy/enemy1/",10,".png");
  Animation leftdegat = new Animation("enemyleftdmg", "anims/enemy/enemy1/damages/",4,".png");
  ///////////////////////////////////////////////////
  
  public Ennemi(float x, float y,float tX,float tY){
         
         //Zone vecteur//
         position = new PVector(x,y);
         taille = new PVector(tX,tY);
         ///////////////
         
         //Zone booléen//
         this.auSol = false; //Stocke l'état : Ennemi au sol ou pas
         this.course = false; //Stocke l'état : Ennemi se déplace ou pas (destiné à l'animation)
         this.orienteD = true; //Stocke l'état : Joueur orienté vers la droite ou pas (destiné à l'animation)
         this.attaque = false; //Stocke l'état : Ennemi prend des degâts ou pas
         ///////////////
         
         //Zone stats ennemi//
         this.vitesseCourse = random(0.5f,3);// Attribution d'une vitesse de course aléatoire
         this.vitesseGravite = 0;
         this.vie =(int) random(100,250);// Attribution d'un nombre de point de vie aléatoire
         this.vieMax = vie; // 
         ////////////////////

  }
  
  public void afficherEnnemi(){// Fonction qui affiche l'ennemi avec la fonction display() de la class animation en fonction de sa position en x et y et sa taille en x et y
    
    if(orienteD) // Si le joueur est orienté vers la droite on affiche les animations de droites
      run.display(position.x,position.y,taille.x,taille.y);           
    else
      leftrun.display(position.x,position.y,taille.x,taille.y); 
  
  }

  
  public void mouvementEnnemi(int moveR,int moveL){ // Fonction destinée aux déplacements de l'ennemi, qui reprends le système utilisé pour le joueur si moveR = 1 on incrémente position.x,
  //si moveL = -1 position.x est décrementer sinon aucun effet sur position.x
    
    //Course
    position.x += vitesseCourse *(moveL+moveR);
  }
  
  //// Méthode qui affiche les pv de l'ennemi sur sa tête
  public void hpEnnemi(){
    if(vie<0) vie = 0;   //empêche les pv négatifs
    //La barre de vie à la même largeur que l'enemie
    // Les hp restant sont proportionnelle à la largeur max
    float lhp;   // Largeur des pv restant de l'ennemi
    
    rectMode(CORNER);
    fill(127);
    rect(position.x, position.y-taille.y/3, taille.x, taille.y/6);   
    
    //Jauge de vie afficher en vert si l'ennemi a plus de 50pv
    //Sinon, l'affiche en rouge
    if(vie>50) fill(40,100,40);
    else fill(255,0,0);
    lhp = vie*taille.x/vieMax;
    rect(position.x, position.y-taille.y/3, lhp, taille.y/6);
    
    ////
    textSize(taille.y/6);
    fill(255);
    textAlign(CENTER, CENTER);
    
    text(vie,position.x+taille.x/2, position.y-taille.y/3+taille.y/12-2 );  //le -2 est nécessaire car center ne centre pas correctement
  }
  //
  
  
}
class Joueur{ // Objet Joueur
  
  //Variables propres à l'objet Joueur//
  PVector position,taille;
  float gravite,vitesseGravite,vitesseCourse,vitesseSaut;
  int vie,vieMax,barArme,barArmeMax,degatArmeInit,degatArme;
  boolean auSol,saute,peutBouger,course,orienteD,peutTirer,prendDegats;
  
  //// Variables permettant l'affichage des infos sur le joueur
  PImage cadre;     //On définit le cadre qui va contenir les infos  
  int lhw_max;    //Represente la largeur maximale des barres HP et WP
  int hhw;        //Hauteur des barres HP et WP
  

    //
    
  /////////////////////////////////////
  
  //Zone animations avec personnage orienté à droite//
  Animation idle = new Animation("idle", "anims/main_character/idle/idle/",8,".png");
  Animation run = new Animation("run", "anims/main_character/run/run/",6,".png");
  Animation jump = new Animation("jump", "anims/main_character/jump/",12,".png");
  
  Animation idledmg = new Animation("idledmg", "anims/main_character/idle/idledmg/",12,".png");
  Animation rundmg = new Animation("rundmg", "anims/main_character/run/damages/",9,".png");

  ///////////////////////////////////////////////////
  
  
  //Zone animations avec personnage orienté à gauche//
  Animation leftIdle = new Animation("idleleft", "anims/main_character/idle/idle/",8,".png");
  Animation leftRun = new Animation("runleft", "anims/main_character/run/run/",6,".png");
  Animation leftJump = new Animation("jumpleft", "anims/main_character/jump/",12,".png");
  
  Animation leftidledmg = new Animation("idleleftdmg", "anims/main_character/idle/idledmg/",12,".png");
  Animation leftrundmg = new Animation("runleftdmg", "anims/main_character/run/damages/",9,".png");
  ///////////////////////////////////////////////////
  
  Animation fleche = new Animation("fleche", "info/fleche/",14,".png");

  
  public Joueur(float x, float y,float tX,float tY,float vX,float vY){
      
         //Zone Vecteurs//
         position = new PVector(x,y); // Vecteur 2d contenant la position du joueur de coordonnées x et y
         taille = new PVector(tX,tY); // Vecteur 2d contenant la taille du joueur de coordonnées x et y
         ////////////////
         
         //Zone booléen//
         this.auSol = false; //Stocke l'état : Joueur au sol ou pas
         this.saute = false; //Stocke l'état : Joueur saute ou pas 
         this.peutBouger = true; //Stocke l'état : Joueur peut avancer ou pas
         this.peutTirer = true; //Stocke l'état : Joueur peut tirer ou pas
         this.prendDegats = false; //Stocke l'état : Joueur prends des degâts ou pas
         this.course = false; //Stocke l'état : Joueur se déplace ou pas (destiné à l'animation)
         this.orienteD = true; //Stocke l'état : Joueur orienté vers la droite ou pas (destiné à l'animation)
         ///////////////
         
         //Zone stats joueur//
         this.gravite = 0f;
         vitesseCourse = vX; //Vitesse de course
         vitesseSaut = vY;  //Vitesse de saut
         this.vieMax = 100; // Nombre de point de vie maximum
         this.vie = 100; // Nombre de point de vie initial : 100
         this.barArme = 0; // Jauge d'arme initiale : 0
         this.barArmeMax = 100; // Jauge d'arme maximale : 100
         this.degatArmeInit = 10; // Nombre de dégât qu'inflige le joueur : 10
         ////////////////////
        
    ////
    this.cadre = loadImage("info/info.png");
    this.lhw_max = 302;
    this.hhw = 20;
      //
    
  }
  
  public void afficheJoueur(){ // Fonction temporaire qui sert à afficher le joueur à une position donnée, et une taille donnée.
  
            if(!prendDegats){
              if(orienteD){ // Si le joueur est orienté vers la droite on affiche les animations de droites
                if(course && !saute) run.display(position.x,position.y,taille.x,taille.y);
                else if(!course && !saute) idle.display(position.x,position.y,taille.x,taille.y);
              
                if(saute) jump.display(position.x,position.y,taille.x,taille.y);
              }else{ // Sinon on affiche celle de gauche
                if(course && !saute) leftRun.display(position.x,position.y,taille.x,taille.y);
                else leftIdle.display(position.x,position.y,taille.x,taille.y);
              
                if(saute)leftJump.display(position.x,position.y,taille.x,taille.y);// Si le joueur saute on affiche l'animation de saut
                
              }
            }else{
              if(orienteD){ // Si le joueur est orienté vers la droite on affiche les animations de droites
                if(course && !saute) rundmg.display(position.x,position.y,taille.x,taille.y);
                else if(!course && !saute) idledmg.display(position.x,position.y,taille.x,taille.y);
              
                if(saute) jump.display(position.x,position.y,taille.x,taille.y);
              }else{ // Sinon on affiche celle de gauche
                if(course && !saute) leftrundmg.display(position.x,position.y,taille.x,taille.y);
                else leftidledmg.display(position.x,position.y,taille.x,taille.y);
              
                if(saute) leftJump.display(position.x,position.y,taille.x,taille.y);// Si le joueur saute on affiche l'animation de saut
                
              }
            }
  
              
              
  }
  
  //// Methode qui affiche les info sur le joueur, en bas, au centre de l'écran
  public void afficheInfo(){
    // Variables locales
    float x,y;   //coordonnées du cadre informatif
    int lhp;   //largeur du rectangle qui remplie la barre de vie
    int lwp;   //largeur du rectangle qui remplie la barre des points de l'arme
    
    // Calcul du centre du cadre en x et en y
    x = width/2;     
    y = height-91;  //47
    
    // Affichage de la zone qui va contenir les infos sur le joueurs
    imageMode(CENTER);
    image(cadre, x, y);
    imageMode(CORNER);
    
    // Calcul de lpv et lwp en fonction des pv du joueur et des points d'armes réel
    lhp = vie*lhw_max/vieMax;
    lwp = barArme*lhw_max/barArmeMax;
    
    //Affichage des PV
    rectMode(CORNER);
    fill(255);
    rect(x-86, y-53.5f, lhw_max, hhw);
    if(vie>15) fill(40,100,40);         // Jauge de vie en vert si plus de 15 pv
    else fill(255,0,0);                 // Sinon , elle sera en rouge
    rect(x-86, y-53.5f, lhp, hhw);       // Jauge de vie
    
    //Affichage des WP
    fill(255);
    rect(x-86, y+37.5f, lhw_max, hhw);
    if(barArme>80) fill(255,0,0);              //Jauge d'arme en rouge si plus de 80, zone de danger
    else if(barArme>60) fill(237,127,16);      // Sinon , si plus de 60, elle sera en orange, surchauffe
    else fill(0,0,255);                        // Sinon , elle sera en bleu
    rect(x-86, y+37.5f, lwp, hhw);    // Jauge de l'arme
}
    //
  
public void mouvement(int moveR,int moveL,int jump) { // Fonction dédiée aux déplacements du joueur( course,saut)
//si moveR = 1 on incrémente position.x,
  //si moveL = -1 position.x est décrementer sinon aucun effet sur position.x
  //si moveR = 1 et moveL = -1 aucun impact sur position.x
  //si moveR = 0 ou moveL = 0 aucun impact sur position.x
//Gestion des inputs inspiré du sketch https://www.openprocessing.org/sketch/92234/
  
   //Course
   position.x += vitesseCourse *(moveL+moveR);
   //Saut
   position.y -= vitesseSaut * jump;
  
  
  }
  
  public void afficheFleche(){// Fonction qui affiche les fléches pour le tutoriel (EN EFFET CETTE METHODE N'A RIEN A FAIRE LA)
    
    // Calcul du centre du cadre en x et en y pour pouvoir afficher les flèches là ou il faut
    float x = width/4;     
    float y = height-91;  //47
     
      fleche.display(width - width/3, y-75,80,80); // On affiche la première flèche
      fleche.display(width - width/3, y+20,80,80); // On affiche la deuxième flèche
  
}
}
class Platforme{// Class platforme
  //Variables propres à l'objet Platforme//
  PVector position;
  PVector taille;
  
  public Platforme(float x, float y,float tX,float tY){
         
         //Zone Vecteurs//
         position = new PVector(x,y); // Vecteur 2d contenant la position de la platforme de coordonnées x et y
         taille = new PVector(tX,tY); // Vecteur 2d contenant la taille de la platforme de coordonnées x et y
         /////////////////
     
  }  
  public boolean rectCollision(float otherRight,float otherLeft,float otherBottom,float otherTop,float vitGrav){
  //www.openprocessing.org/sketch/119522 inspiré du code de détection de collision de Mr Devon Scott-Thuakin basé sur le principe de superposition de rectangle,
  //elle teste si l'emplacement de chaque coin d'un rectangle donné se superpose avec un autre rectangle
  return !((position.x > otherRight || position.x + taille.x < otherLeft) || (position.y - vitGrav > otherBottom || position.y + taille.y < otherTop));
  
}
  
}
class Projectiles{
  //Variables propres à l'objet Projectile//
  PVector position,taille;
  float vitesse,rotation,orientation;
  PImage rendu,rendu2,rendu3;
  
  public Projectiles(float x, float y, float tX,float tY,float o){
    
    //Zone Vecteurs//
    position = new PVector(x,y); // Vecteur 2d contenant la position du projectile de coordonnées x et y
    taille = new PVector(tX,tY); // Vecteur 2d contenant la taille du projectile de coordonnées x et y
    ///////////////
    
    //Zone stat//
    this.orientation = o;
    this.vitesse = 20;
    /////////////
    //
    this.rendu = loadImage("anims/main_character/bullets/bullet0.png"); //On charge Image 1 du projectile
    this.rendu2 = loadImage("anims/main_character/bullets/bullet1.png"); //On charge Image 2 du projectile
    this.rendu3 = loadImage("anims/main_character/bullets/bullet2.png");//On charge Image 3 du projectile

    
  }
  
  public void mouvementProjectile(){ // Fonction qui fait bouger le projectile en fonction de son orientation, vers la droite si "orientation = 1" vers la gauche sinon
                                        // Orientation determiné en fonction de l'orientation du joueur
    if(orientation == 1) position.x += vitesse;
    else position.x -= vitesse;
    
  }
  
  public void afficheProjectile(int charge){ // Fonction qui affiche le projectile
    
    rectMode(CENTER);
    if(charge < 60)                                        // Si l'arme ne surchauffe pas
    image(rendu,position.x,position.y,taille.x,taille.y); // Image 1 du projectile
    if(charge > 60 && charge < 90)                         // Si l'arme surchauffe
    image(rendu2,position.x,position.y,taille.x,taille.y); // Image 2 du projectile
    if(charge > 90)                                        // Si est trop proche d'exploser
    image(rendu3,position.x,position.y,taille.x,taille.y); // Image 3 du projectile

    rectMode(CORNER);
  }
  
}
public void keyPressed()
        { 
          
          
          if ((key == 'f' || key =='F') && monJoueur.peutTirer) // Si la touche destinée au "tir" est appuyée, on ajoute un objet projectile à notre liste des projectiles
             {
               
               shoot =  minimTuto.loadFile("son/shoot.mp3"); // On charge le sample du tir
               shoot.setGain(-20.0f);// On réduits son volume 
               shoot.play();// Et on le lance
               if(monJoueur.orienteD) //On instancie un projectile allant vers la droite si le joueur est orienté à droite
                  projectiles.add(new Projectiles(monJoueur.position.x + (monJoueur.taille.x / 2) ,monJoueur.position.y + (monJoueur.taille.y/ 2),monJoueur.taille.x/6,monJoueur.taille.x/6,1));
                 else // Sinon un projectile allant vers la gauche
                 projectiles.add(new Projectiles(monJoueur.position.x + (monJoueur.taille.x / 2) ,monJoueur.position.y + (monJoueur.taille.y/ 2),monJoueur.taille.x/6,monJoueur.taille.x/6,-1));
                  
                monJoueur.barArme += 8;
                monJoueur.peutTirer = false; // Permet de forcer le joueur à ré-appuyer sur la touche pour tirer
                if(etatJeu==4 && etapeTuto == 4){ // Si on se trouve dans le tutoriel à l'étape concernant l'arme, on  passe à l'étape suivante
                      etapeTuto = 5; //étape suivante
                  }
                  
             } 
         
             if ((key == 'd' || key =='D')) // Si la touche destinée à la course à droite est appuyée
             {
                  mouvementD = 1; // Coefficient multiplicateur de la fonction mouvement du joueur COURSE
                  if(etatJeu==4 && etapeTuto == 1){ // Si on se trouve dans le tutoriel à l'étape concernant le déplacement à droite, on  passe à l'étape suivante
                      etapeTuto = 2;//étape suivante
                  }
                                            
             } else if ((key == 'q' || key =='Q')) // Si la touche destinée à la course à gauche est appuyée
             {
                  mouvementG = -1; // Coefficient multiplicateur de la fonction mouvement du joueur COURSE
                  if(etatJeu==4 && etapeTuto == 2){ // Si on se trouve dans le tutoriel à l'étape concernant le déplacement à gauche, on  passe à l'étape suivante
                      etapeTuto = 3;//étape suivante
                  }
                  
             }
                      
             if(key == ' '){
                 if(monJoueur.auSol && (etatJeu == 4 || etatJeu ==2)){ // Si la touche destinée à la course à gauche est appuyée, le joueur est au sol et qu'il ne saute pas déjà
                 saut =  minimTuto.loadFile("son/saut.mp3"); // On charge le sample du saut ( bruit de saut le plus étrange de l'univers)
                 saut.setGain(10.0f);// On augmente son volume 
                 saut.play();// et on le lance
                 mouvementH = 1 ;// Coefficient multiplicateur de la fonc2tion mouvement du joueur SAUT
                 monJoueur.saute = true;// Le joueur saute
                 if(etatJeu==4 && etapeTuto == 3){// Si on se trouve dans le tutoriel à l'étape concernant le saut, on  passe à l'étape suivante
                      etapeTuto = 4;//Etape suivante
                  }
                  
            
             
             
        }
             }
             
             if((key == 't' || key == 'T')&&etatJeu == 4){ // Si on se trouve dans le tutoriel et que l'on appuies sur la touche T
                 etatJeu = 2; // On passe au jeu
                 minimTuto.stop(); //Et on "relâche" ( terme utilisé dans la documentation officielle de la libraire "Minim") tous les samples associé à minimTuto
             }
             
             if((etatJeu == 1 || etatJeu ==3)){ // Si on a gagné une partie ou perdu
               if((key == 'r' || key == 'R')){ // Et que l'on appuies sur la touche 'r'
                 resetJeu(2); // On invoque la fonction qui rénitialise les variables qui se doivent d'être rénitialisées pour recommencer une partie
             }
             }
        }

public void keyReleased()
{
             if (key == 'd' || key =='D') // Si la touche destinée à la course à droite est relâchée
             {
                 mouvementD = 0; // Coefficient multiplicateur de la fonction mouvement du joueur COURSE

             } else if (key == 'q' || key =='Q')
             {
                  mouvementG = 0; // Coefficient multiplicateur de la fonction mouvement du joueur COURSE

             }
             
             if(key == ' '){
               mouvementH = 0; // Coefficient multiplicateur de la fonction mouvement du joueur SAUT si la touche du saut est relâchée

             }
             
             if (key == 'f' || key =='F'){ // Si la touche destinée au tir est relâchée
                 monJoueur.peutTirer = true; //Le joueur a de nouveau la possibilité de tirer
             }
             

         
}
public void updateEnnemis(){ // Fonction qui permettra d'actualiser les données des ennemis

       if(numEnnemiTued == 2){ //Si le nombre d'ennemi tués est de deux
         numVague = 2; //On passe à la vague deux
       }
       else if(numEnnemiTued == 4){ //Si le nombre d'ennemi tués est de quatres
         numVague = 3; //On passe à la vague trois
       }
       else if(numEnnemiTued == 10) //Si le nombre d'ennemi tués est de dix
       {
         etatJeu = 3; // On passe à l'état de jeu : Ecran de victoire
       }
       
      
      switch(numVague){//Switch qui attribu un nombre d'ennemis à faire spawn en fonction du numéro de Vague
            case 1: // Vague 1
                   ennemiParVague = 1; // 1 ennemi à la fois
            break;
            case 2:// Vague 2
                   ennemiParVague = 2;   // 2 ennemi à la fois
            break;     
            case 3:// Vague 3
                   ennemiParVague = 3; // 3 ennemi à la fois                    
            break;
        
      }
      
      if(numEnnemiTued + ennemiParVague <= 10){ // Si le nombre d'ennemis tués ajouté au nombre d'ennemi par vague est inférieur au nombre d'ennemi à tuer, on peut en faire spawn d'avantage
      //A pour effet de ne pas faire spawn d'ennemis en trop par rapport au nombre d'ennemi que l'on doit tuer
      if(ennemis.size() < ennemiParVague){//Si il y a moins d'ennemis qu'il en faudrait par vague
        ennemis.add(new Ennemi(random(largEcran/16, width - largEcran/16),-height,largEcran/40,hautEcran/16));//On en rajoute à la liste ennemis
        
      }
      }

  
      for(int i = 0; i <ennemis.size();i++){ //On parcours la liste ennemis
        
        Ennemi e = ennemis.get(i); //i-ème ennemi de la liste ennemis

        for(int j = 0; j < projectiles.size();j++){ // On parcours la liste des projectiles pour déterminer si l'un d'entre eux est entré en collision avec un ennemi
            Projectiles proj = projectiles.get(j);//j-ème projectile de la liste
            

             if (proj.position.x+proj.taille.x>e.position.x // à la manière de "flappy-papang" on contrôle toutes les conditions pour lesquels le projectile entre en collision avec un ennemi
             && e.position.x>proj.position.x-proj.taille.x-e.taille.x
             && proj.position.y + proj.taille.y >= e.position.y
             &&
             proj.position.y + proj.taille.y < e.position.y + e.taille.y) {  
              e.vie -= monJoueur.degatArmeInit; // Si il ya collision l'ennemi perds de la vie
              if(monJoueur.barArme > 60){ // Si l'arme surchauffe
                e.position.x += (50 * proj.orientation);//L'ennemi est repoussé de 50 pixel
               
              }
              projectiles.remove(proj);//Si il y a collision on supprime le j-ème projectile de la liste
             
            }
                  
        }
        

        if(e.vie <= 0){
          ennemis.remove(e);// si le i-ème ennemi de la liste n'a plus de vie on le supprime de cette dernière
          numEnnemiTued++;//le nombre d'ennemi tués est augmenté
        }
        if(e.position.y > hautEcran){//Si le i-ème ennemi tombe hors de l'écran
          ennemis.remove(e);//on le supprime de cette dernière
          numEnnemiTued++;//le nombre d'ennemi tués est augmenté tout de même
        }
        
        ////
        for(int j = 0; j < sol.size();j++){ // On parcours la liste des platformes pour déterminer si l'ennemi entre en collision avec l'une d'entre elles
        
        Platforme plat = sol.get(j);     //j-ème platforme de la liste sol
       
    
          if(plat.rectCollision(e.position.x + e.taille.x,e.position.x,e.position.y + e.taille.y,e.position.y,e.vitesseGravite)){
          //Fonction qui détecte les superpositions(voir objet Platforme)
           if(e.vitesseGravite  > 0 && e.position.y + e.taille.y < plat.position.y){ // Si l'ennemi est en contact avec le dessus d'une platforme
             e.vitesseGravite = 0; // La vitesse de la gravité de base à 1.25 passe à 0 pour en annuler l'effet
             e.auSol = true; // L'ennemi  est au sol
           }
            
        }
        }
        ///IA/////
         //Mouvements de l'ennemi//
     if(e.auSol && (e.position.y > monJoueur.position.y - 10 && e.position.y < monJoueur.position.y + monJoueur.taille.y)){ //Si l'ennemi est sur le même axe Y que le joueur
       if(monJoueur.position.x > e.position.x + e.taille.x ){ //Et que le joueur se trouve à droite de l'ennemi
            e.orienteD = true; // L'ennemi est orienté vers la droite (booléen servant à l'animation)
            e.mouvementEnnemi(1,0); // On inovoque la fonction qui fait bouger l'ennemi vers la droite
        }else if(monJoueur.position.x > e.position.x + e.taille.x || e.position.x < monJoueur.position.x + monJoueur.taille.x){//Si l'ennemi est en contact avec le joueur
          
          e.mouvementEnnemi(0,0);//On stop les mouvements de l'ennemi
        }
        else{
           e.mouvementEnnemi(0,-1);// On inovoque la fonction qui fait bouger l'ennemi vers la gauche
           e.orienteD = false; // L'ennemi est orienté vers la gauche (booléen servant à l'animation)
        }
     }else{//Si l'ennemi n'est pas sur le même axe Y que le joueur
       e.mouvementEnnemi(0,0); //On stop les mouvements de l'ennemi
     }
     //////////
        
        
      
      //////////////////////////
        
      
     
       
       //Action simulant la gravité//
        e.position.y +=  e.vitesseGravite;
        e.auSol = false;
        /////
        
        if(e.auSol == false) e.vitesseGravite+=1.25f;
        
        ///////////////////////////////
        e.afficherEnnemi();//Fonction qui affiche l'i-ème ennemi de la liste ennemis
        e.hpEnnemi();//Fonction qui affiche la bar de vie de l'i-ème ennemi de la liste ennemis
        
      }

    
}

    
    
    
    
       
         
  
  
  

public void updateJoueur(){ // Fonction qui permettra d'actualiser les données du  joueur

  
  //Initialisation de certains booléens//
   monJoueur.auSol = false;
   monJoueur.saute = true;
   ////////////////////////////////////
   
   //variables armes joueur//
   if(monJoueur.barArme >= 100) monJoueur.vie = 0; // Si l'arme surcharge, le joueur meurt
   if(monJoueur.peutTirer && monJoueur.barArme > 0) monJoueur.barArme -= (2 / frameRate*2);// Réduction de la barre d'arme toutes les frames avec une formule totalement aléatoire, mais qui rends bien
   if(monJoueur.vie <=0) etatJeu = 1; // Si le joueur meurt, on passe à l'état 1 du jeu: Ecran défaite
   if(monJoueur.position.x > largEcran || monJoueur.position.x + monJoueur.taille.x < 0 || monJoueur.position.y > hautEcran) etatJeu = 1;// Si le joueur sort de l'écran, on passe à l'état 1 du jeu: Ecran défaite
   
   /////////////////////////
   if(etatJeu==4){ // Pour le tutoriel,  on n'utilise pas de liste dans laquelle on stocke les platformes, vu qu'il n'est composé que d'un sol
    
          
           if(vitGrav > 0 && monJoueur.position.y + monJoueur.taille.y + vitGrav >= ((hautEcran * 882)/1080)){ // Si le joueur est en contact avec le dessus du  sol
             vitGrav = 0; // La vitesse de la gravité de base à 1.25 passe à 0 pour en annuler l'effet
             monJoueur.auSol = true; // Le joueur est au sol
             monJoueur.saute = false;// Le joueur ne saute pas, car il est au sol
           
            
        }
   }
  if(etatJeu==2){ // Pour le jeu
  for(int i = 0; i < sol.size();i++){ // On parcours la liste des platformes pour déterminer si le joueur entre en collision avec l'une d'entre elles
    
    Platforme plat = sol.get(i);// i-ème platforme de la liste
    
        if(plat.rectCollision(monJoueur.position.x + monJoueur.taille.x,monJoueur.position.x,monJoueur.position.y + monJoueur.taille.y,monJoueur.position.y,vitGrav)){
          //Fonction qui détecte les superpositions(voir objet Platforme)
          
           if(vitGrav > 0 && monJoueur.position.y + monJoueur.taille.y < plat.position.y){ // Si le joueur est en contact avec le dessus de la i-ème platforme de la liste
             vitGrav = 0; // La vitesse de la gravité de base à 1.25 passe à 0 pour en annuler l'effet
             monJoueur.auSol = true; // Le joueur est au sol
             monJoueur.saute = false;// Le joueur ne saute pas, car il est au sol
           }
            
        }
         
  }
        int tempsPasse = millis() - tempTotal;

 
        for(int i = 0; i <ennemis.size();i++){ // On parcours la liste des ennemis pour déterminer si le joueur entre en collision avec l'un d'entre eux
        
          Ennemi e = ennemis.get(i); //i-ème ennemi de la liste ennemis
          if ((monJoueur.position.x+monJoueur.taille.x>=e.position.x // à la manière de "flappy-papang" on contrôle toutes les conditions pour lesquels le joueur entre en collision avec un ennemi
               && monJoueur.position.x <= e.position.x+e.taille.x)
               && ((monJoueur.position.y + monJoueur.taille.y >= e.position.y && monJoueur.position.y + monJoueur.taille.y <= e.position.y+e.taille.y)
               ||(monJoueur.position.y  >= e.position.y && monJoueur.position.y  <= e.position.y+e.taille.y)
               )) {  
                 
                monJoueur.vie -= 0.125f;// Si il y a collision le joueur perds de la vie
                monJoueur.prendDegats = true; // Booléen qui permet de gérer l'animation
  
          }
          if(monJoueur.prendDegats){// Si le joueur prends des dégâts
            if (tempsPasse > animationDegatDelay) { // Et que le temps passé est celui du délais de l'animation de dégât du joueur
              monJoueur.prendDegats = false; // On passe le bolléen à l'état false
              tempTotal = millis(); // On rénitialise le "timer"
            }
              
          }
        }
  }
    //On détermines si le joueur est en course, et son orientation
    if(mouvementD + mouvementG != 0) monJoueur.course = true; else monJoueur.course = false;
    if(mouvementD == 1) monJoueur.orienteD = true; else if(mouvementG == -1) monJoueur.orienteD = false;
    ////////////////////////////////////////////////////////////////
    
   //Action simulant la gravité//
   monJoueur.position.y += vitGrav;
   vitGrav+=1.25f;
   ///////////////////////////////
    
    //Appels des fonctions propre à l'objet Joueur//
    
    monJoueur.mouvement(mouvementD,mouvementG,mouvementH); // Fonction mouvement du joueur
    monJoueur.afficheJoueur();// On affiche le joueur
    //////////////
    
    
    monJoueur.afficheInfo();  //Méthode qui affiche les infos sur le joueur
     
    
    //Debug Zone//
    //print(mouvementH);
    /////////////
  
  
}
public void updateProjectiles(){  // Fonction qui permettra d'actualiser les données des projectiles
  
    for(int i = 0; i < projectiles.size();i++){ // On parcours la liste des projectiles
              
              Projectiles proj = projectiles.get(i);
              proj.mouvementProjectile(); // Fonction mouvement du i-ème projectile de la liste
              proj.afficheProjectile(monJoueur.barArme); // Fonction qui affiche le i-ème projectile de la liste
             
              
               if(proj.position.x > width || proj.position.x < 0) projectiles.remove(proj); // Si le projectile est hors de l'écran, il est enlevé de la liste projectiles
                                                                                                 // ce qui a pour effet de le "supprimer"
  }
  
}
PImage vague; //On définit l'image qui va afficher les informations des vagues

public void afficheInfoVague(){
  
  switch(numVague){
        case 1:                              //Si on est à la vague numéro 1
        vague = loadImage("info/vague0.png");  // on charge l'image correspondante
        break;
        case 2:                                  //Si on est à la vague numéro 2
        vague = loadImage("info/vague1.png");    // on charge l'image correspondante
        break;
        case 3:                                  //Si on est à la vague numéro 3
        vague = loadImage("info/vague2.png");    // on charge l'image correspondante
        break;
  }
  // Affichage de la zone qui va contenir les infos sur le joueurs
    imageMode(CENTER); //Permet de changer le mode à CENTER
    image(vague, largEcran/2, hautEcran/16,largEcran/4,hautEcran/8); //Affichage de l'image en haut de l'écran
    imageMode(CORNER); //Permet de changer le mode à CENTER
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
