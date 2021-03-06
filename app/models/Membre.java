package models;

import javax.persistence.*;
import com.google.gson.annotations.Expose;
import play.db.jpa.*;
import play.data.validation.*;
import java.util.*;
import java.text.*;

/**
 * Created with IntelliJ IDEA.
 * User: lepeteil
 */

@Entity
@Table(uniqueConstraints =
        {@UniqueConstraint(
                columnNames = {"email"
                })},
        name="Membre")
public class Membre extends Model {
    @Required
    public String nom;
    @Required
    public String prenom;
    @Required
    public Date dateNaissance;
    @Required
    public String sexe;
    @Required
    @MinSize(6)
    public String motDePasse;
    @Email
    @Required
    public String email;
    public Date dateInscription;
    public boolean desinscrit;
    @OneToMany(mappedBy="createur", cascade=CascadeType.ALL)
    public Set<Parcours> lesParcoursCrees = new HashSet();
    @ManyToMany
    public Set<Parcours> lesParcoursChoisis =new HashSet();
    @Required
    public boolean isAdmin;

    /**
     * Créé un membre
     * @param nom
     * @param prenom
     * @param motDePasse
     * @param dateNaissance
     * @param email
     * @param sexe
     */
    public Membre(String nom, String prenom,String motDePasse, Date dateNaissance, String email,String sexe) {
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = motDePasse;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.sexe = sexe;
        this.dateInscription = new Date();
        this.desinscrit = false;
        this.isAdmin = false;
    }
    /**
     * Créé un membre
     * @param nom
     * @param prenom
     * @param motDePasse
     * @param dateNaissance
     * @param email
     * @param sexe
     * @param isAdmin
     */
    public Membre(String nom, String prenom,String motDePasse, Date dateNaissance, String email,String sexe, boolean isAdmin) {
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = motDePasse;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.sexe = sexe;
        this.dateInscription = new Date();
        this.desinscrit = false;
        this.isAdmin = isAdmin;
    }

    /**
     * Vérifie lors de la tentative d'authentification l'existence de l'utilisateur en base
     * @param email
     * @param motdepasse
     * @return vrai si le membre est en ligne / connecté sur l'application
     */
    public static boolean connect(String email, String motdepasse) {
        return(Membre.find("email = ? and motDePasse = ?",email, motdepasse).first() != null);
    }

    /**
     * Créé un parcours en tant que conducteur
     * @param parcours
     */
    public void ajouterParcoursCree(Parcours parcours) {
        this.lesParcoursCrees.add(parcours);
        this.save();
    }

    /**
     * Selectionne un parcours comme passager
     * @param parcours
     */
    public void ajouterParcoursChoisi(Parcours parcours) {
        this.lesParcoursChoisis.add(parcours);
        this.save();
    }

    /**
     * Desinscrit le membre
     */
    public void supprimerCompte() {
        this.desinscrit = true;
        this.annulerTousLesParcours();
        this.save();
    }

    /**
     * Annule une reservation pour le parcours
     * @param parcours
     */
    public void seDesinscrireParcours(Parcours parcours){
        if(verifierParcoursChoisiExiste(parcours)){
            this.lesParcoursChoisis.remove(parcours);
            parcours.supprimerMembreInscrit(this);
            this.save();
            parcours.save();
        }
    }

    /**
     * Supprime temporairement le parcours créé au préalable
     * @param parcours
     */
    public void supprimerParcours(Parcours parcours){

        if(verifierParcoursCreeExiste(parcours)){
            parcours.supprimerParcoursCree();
            parcours.save();
            this.save();
        }
    }

    /**
     * Réactive le parcours créé mais annulé précédemment
     * @param parcours
     */
    public void reactiverParcours(Parcours parcours){
        if(verifierParcoursCreeExiste(parcours)){
            parcours.reactiverParcoursCree();
            parcours.save();
            this.save();
        }
    }

    /**
     * Verifie le lien entre le membre et le parcours créé
     * @param parcours
     * @return vrai si le parcours a bien été créé par le membre
     */
    private boolean verifierParcoursCreeExiste(Parcours parcours){
        Iterator<Parcours> itr = lesParcoursCrees.iterator();
        while(itr.hasNext()) {
            if(itr.next().equals(parcours))
                return true;
        }
        return false;
    }

    /**
     * Verifie le lien entre le membre et le parcours reservé
     * @param parcours
     * @return vrai si le parcours a bien été réservé par le membre
     */
    private boolean verifierParcoursChoisiExiste(Parcours parcours){
        Iterator<Parcours> itr = lesParcoursChoisis.iterator();
        while(itr.hasNext()) {
            if(itr.next().equals(parcours))
                return true;
        }
        return false;
    }

    /**
     * Annule tous les parcours proposés par le membre
     */
    private void annulerTousLesParcours(){
        //Gestion des parcours crees
        Iterator<Parcours> itr = lesParcoursCrees.iterator();
        while(itr.hasNext()) {
            Parcours p = itr.next();
            p.supprime = true;
            p.save();
        }

        //Gestion des parcours choisis
        Iterator<Parcours> itr1 = lesParcoursChoisis.iterator();
        while(itr.hasNext()) {
            Parcours p = itr.next();
            p.membresInscrits.remove(this);
            p.save();
        }
        this.save();
    }

}