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
