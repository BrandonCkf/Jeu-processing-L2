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
         this.vitesseCourse = random(0.5,3);// Attribution d'une vitesse de course aléatoire
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
  void hpEnnemi(){
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
