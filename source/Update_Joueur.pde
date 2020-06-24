
void updateJoueur(){ // Fonction qui permettra d'actualiser les données du  joueur

  
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
                 
                monJoueur.vie -= 0.125;// Si il y a collision le joueur perds de la vie
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
   vitGrav+=1.25;
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
