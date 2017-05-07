package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.widget.TextView;
import android.view.View;
import android.widget.AdapterView;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;





/**
 * Created by JJ on 05-05-17.
 */

public class Recipe extends Activity {

    String this_recette ;
    String this_catégorie;
    String this_sous_catégorie;
    String this_difficulté;
    String this_p_time;
    String this_c_time;
    String this_description;

    private TextView thiscategorie;
    String[] ColumnStringArray = {"RECETTE_PICTURE_COLUMN","RECETTE_DESCRIPTION_COLUMN","RECETTE_DATE_COLUMN","RECETTE_AUTHOR_COLUMN","RECETTE_NBRE_PERS_COLUMN","RECETTE_DIFFICULTY_COLUMN","RECETTE_TYPE_COLUMN","RECETTE_SOUS_TYPE_COLUMN"} ;
   // String[] Strings = {this_date, this_auteur, this_classement} ;

      String recette = "Kefta" ;

       // for 1: size(ColumnStringAray)
    //this_recette = db.getRecetteColumn(String recette_name, String COLUMN)



        @Override
        protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);

         this_catégorie = raw_data(recette,ColumnStringArray[7] );
        thiscategorie = (TextView) findViewById(R.id.current_categorie);
        thiscategorie.setText(this_catégorie);

    }

    public String raw_data(String recette, String truc) {

        MyDatabase db=new MyDatabase(this);
        if(db.open()) {
            return db.getRecetteColumn(recette,truc);
        } else {
            throw new Error("Impossible d'ouvrir la base de donnees");
        }
    }
  //  public get_data (String recette)

    //{
   // }




    //public get_etapes (string recette)

    //{
    //  this_recette = recette ;
    //}
    //public global_note ()

    //{


    //}
}

