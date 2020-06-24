void updateProjectiles(){  // Fonction qui permettra d'actualiser les données des projectiles
  
    for(int i = 0; i < projectiles.size();i++){ // On parcours la liste des projectiles
              
              Projectiles proj = projectiles.get(i);
              proj.mouvementProjectile(); // Fonction mouvement du i-ème projectile de la liste
              proj.afficheProjectile(monJoueur.barArme); // Fonction qui affiche le i-ème projectile de la liste
             
              
               if(proj.position.x > width || proj.position.x < 0) projectiles.remove(proj); // Si le projectile est hors de l'écran, il est enlevé de la liste projectiles
                                                                                                 // ce qui a pour effet de le "supprimer"
  }
  
}
