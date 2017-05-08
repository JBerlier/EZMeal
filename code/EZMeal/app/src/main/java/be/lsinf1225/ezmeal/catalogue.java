package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marti on 28-04-17.
 * TO DO créer le intent vers la recette + rendre cela plus jolie
 */

public class catalogue extends ListActivity {
    private static final int REQUEST_CODE = 1 ;
    private List<String> recettes =new ArrayList();
    private String chosenrecette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyDatabase db = new MyDatabase(this);
        recettes=db.catalog();
        recettes.add("retour au menu");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,recettes);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenrecette=recettes.get(position);
                if(chosenrecette.equals("retour au menu"))
                {
                    Intent i = new Intent(catalogue.this, menu.class);
                    startActivityForResult(i, REQUEST_CODE);
                }
                else
                {
                    Intent i = new Intent(catalogue.this, menu.class);//menu->RecetteActivity
                    i.putExtra("recette",chosenrecette);
                    startActivityForResult(i, REQUEST_CODE);
                }
            }
        });
    }

}
