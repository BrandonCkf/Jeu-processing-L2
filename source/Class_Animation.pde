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
  
  void display(float x,float y,float largeur,float longueur){
      frame = (frame+1) % nbFrames;
      image(frames[frame],x,y,largeur,longueur); 
      
  }
}
