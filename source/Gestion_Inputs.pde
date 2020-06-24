void keyPressed()
        { 
          
          
          if ((key == 'f' || key =='F') && monJoueur.peutTirer) // Si la touche destinée au "tir" est appuyée, on ajoute un objet projectile à notre liste des projectiles
             {
               
               shoot =  minimTuto.loadFile("son/shoot.mp3"); // On charge le sample du tir
               shoot.setGain(-20.0);// On réduits son volume 
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
                 saut.setGain(10.0);// On augmente son volume 
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

void keyReleased()
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
