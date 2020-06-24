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
    rect(x-86, y-53.5, lhw_max, hhw);
    if(vie>15) fill(40,100,40);         // Jauge de vie en vert si plus de 15 pv
    else fill(255,0,0);                 // Sinon , elle sera en rouge
    rect(x-86, y-53.5, lhp, hhw);       // Jauge de vie
    
    //Affichage des WP
    fill(255);
    rect(x-86, y+37.5, lhw_max, hhw);
    if(barArme>80) fill(255,0,0);              //Jauge d'arme en rouge si plus de 80, zone de danger
    else if(barArme>60) fill(237,127,16);      // Sinon , si plus de 60, elle sera en orange, surchauffe
    else fill(0,0,255);                        // Sinon , elle sera en bleu
    rect(x-86, y+37.5, lwp, hhw);    // Jauge de l'arme
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
  
  void afficheFleche(){// Fonction qui affiche les fléches pour le tutoriel (EN EFFET CETTE METHODE N'A RIEN A FAIRE LA)
    
    // Calcul du centre du cadre en x et en y pour pouvoir afficher les flèches là ou il faut
    float x = width/4;     
    float y = height-91;  //47
     
      fleche.display(width - width/3, y-75,80,80); // On affiche la première flèche
      fleche.display(width - width/3, y+20,80,80); // On affiche la deuxième flèche
  
}
}
