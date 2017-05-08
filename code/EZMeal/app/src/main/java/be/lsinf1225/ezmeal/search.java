package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class search extends Activity {
    private static final int REQUEST_CODE = 1 ;
    private Button buttonRetour ;
    private Button buttonSearch ;
    private EditText keyword;
    private Spinner spinner_type;
    private Spinner spinner_subtype;
    private String searched_type="";
    private String searched_subtype="";
    private String searched_keyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        keyword =(EditText) findViewById(R.id.editText_keywords);
        buttonRetour = (Button) findViewById(R.id.button_retour);
        buttonSearch = (Button) findViewById(R.id.button_search);
        spinner_type = (Spinner) findViewById(R.id.spinner_type);
        spinner_subtype = (Spinner) findViewById(R.id.spinner_subtype);

        loadSpinnerData();
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searched_type = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_subtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searched_subtype = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searched_keyword= keyword.getText().toString();
                Intent i = new Intent(search.this, SearchResult.class);
                i.putExtra("type",searched_type);
                i.putExtra("subtype",searched_subtype);
                i.putExtra("keyword",searched_keyword);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        buttonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(search.this, menu.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
   private void loadSpinnerData() {
       MyDatabase db = new MyDatabase(this);
       List<String> types =db.getTypes();
       List<String> subtypes = db.getSubTypes();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, types);
       ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
               R.layout.spinner_item, subtypes);

        // Drop down layout style - list view with radio button
        dataAdapter1
                .setDropDownViewResource(R.layout.spinner_dropdown_item);
       dataAdapter2
               .setDropDownViewResource(R.layout.spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_type.setAdapter(dataAdapter1);
        spinner_subtype.setAdapter(dataAdapter2);
    }
}