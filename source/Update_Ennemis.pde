void updateEnnemis(){ // Fonction qui permettra d'actualiser les données des ennemis

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
        
        if(e.auSol == false) e.vitesseGravite+=1.25;
        
        ///////////////////////////////
        e.afficherEnnemi();//Fonction qui affiche l'i-ème ennemi de la liste ennemis
        e.hpEnnemi();//Fonction qui affiche la bar de vie de l'i-ème ennemi de la liste ennemis
        
      }

    
}

    
    
    
    
       
         
  
  
  
