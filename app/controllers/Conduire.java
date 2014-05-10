package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Membre;
import models.Parcours;
import models.Ville;
import play.mvc.Controller;

import java.util.List;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
/**
 * Created by tonyguolei on 5/9/14.
 */
public class Conduire extends Controller {
    // Pour resoudre probleme de JSON
    public static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static void testAjax(){
        Membre m1 = new Membre("Guo","Lei","123456",23,"tonyguolei@gmail.com").save();
        Membre m2 = new Membre("membreInscrit1","Lei","123456",23,"tonyguolei@gmail.com").save();
        Membre m3 = new Membre("membreInscrit2","Lei","123456",23,"tonyguolei@gmail.com").save();
        Ville v1 = new Ville("Grenoble",38000).save();
        Ville v2 = new Ville("Lyon",69000).save();
        Parcours p1 = new Parcours(m1,v1,v2,25,1).save();
        Parcours p2 = new Parcours(m1,v1,v2,28,1).save();
        Parcours p3 = new Parcours(m2,v1,v2,30,1).save();

        m1.addParcoursCree(p1);
        m1.addParcoursCree(p2);

        m1.addParcoursChoisi(p3);

        p1.addmembreInscrit(m2);
        p1.addmembreInscrit(m3);

        JSONSerializer serializer = new JSONSerializer();

        // test pour createur
        renderJSON(serializer.include("lesParcoursCrees", "lesParcoursChoisis").exclude("*.class").transform(new DateTransformer("yyyy/MM/dd hh:mm:ss"), "dateInscription").serialize(m1));

        //test pour parcour
//        renderJSON(serializer.include("membresInscrit").exclude("*.class").transform(new DateTransformer("yyyy/MM/dd hh:mm:ss"), "dateParcours").serialize(p1));
    }
}
