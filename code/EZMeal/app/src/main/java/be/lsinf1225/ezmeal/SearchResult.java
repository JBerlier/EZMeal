package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by marti on 04-05-17.
 */

public class SearchResult extends ListActivity {


    private String searched_type="";
    private String searched_subtype="";
    private String searched_keyword="";
    public String chosenRecette;
    private static final int REQUEST_CODE=1;
    public List<String> recettes=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent in= getIntent();
        Bundle b = in.getExtras();

        if(b!=null)
        {
            searched_type =(String) b.get("type");
            searched_subtype =(String) b.get("subtype");
            searched_keyword =(String) b.get("keyword");
        }
        MyDatabase db = new MyDatabase(this);
        recettes=db.search(searched_keyword,searched_type,searched_subtype);
        if (recettes.isEmpty())
        {
            Toast.makeText(SearchResult.this, "Pas de résultats", Toast.LENGTH_SHORT).show();
        }
        recettes.add("autre recherche");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, recettes);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenRecette = recettes.get(position);
                if(chosenRecette.equals("autre recherche"))
                {
                    Intent i = new Intent(SearchResult.this, search.class);
                    startActivityForResult(i, REQUEST_CODE);
                }
            }
        });

    }

}
