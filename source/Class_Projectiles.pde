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
