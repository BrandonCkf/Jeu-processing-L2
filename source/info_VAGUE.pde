PImage vague; //On définit l'image qui va afficher les informations des vagues

void afficheInfoVague(){
  
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
