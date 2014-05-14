/**
 * Created by tonyguolei on 5/10/14.
 */

/*La Classe Membre*/

/*NameSpace*/
var ProjetWeb = {}

ProjetWeb.Membre = function() {
    /*properties of class Membre*/
    this.id = null;
    this.nom = null;
    this.prenom = null;
    this.age =null;
    this.email = null;
    this.dateInscription = null;
    this.lesParcoursCrees = {};
    this.lesParcoursChoisis = {};

    /*consctructor of class Membre*/
    this.constructor = function(id, nom, prenom, age, email, dateInscription, lesParcoursCrees, lesParcoursChoisis){
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.email = email;
        this.dateInscription = dateInscription;
        this.lesParcoursCrees = lesParcoursCrees;
        this.lesParcoursChoisis = lesParcoursChoisis;
    };
};

/*methodes of class Membre*/
ProjetWeb.Membre.prototype = {
    recupererMembreInfo: function(email, cb_success, cb_error) {
        var self = this;
        $.ajax({
            url: "/recupererMembreInfo",
            data: {email: email}
        })
            .done(function(data) {
                console.log(data);
                self.constructor(data.id, data.nom, data.prenom, data.age, data.email, data.dateInscription, data.lesParcoursCrees, data.lesParcoursChoisis);
                cb_success(data);
            })
            .fail(function(error) {
                console.log("error recupererMembreInfo");
                cb_error(error);
            })
    },

    recupererMembreInscrit: function(parcour_id, cb_success, cb_error) {
        var self = this;
        $.ajax( {
            url: "/recupererMembreInscrit",
            data: { id: parcour_id }
        } )
        .done(function(data) {
            console.log(data);
            cb_success(data);
        })
        .fail(function(error) {
            console.log("error recupererMembreInscrit");
            cb_error(error);
        })
    },

    creerParcour : function() {},

    supprimerParcour : function() {},

    modifierParcour : function() {}

}