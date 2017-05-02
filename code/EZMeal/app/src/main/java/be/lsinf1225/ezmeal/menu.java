package be.lsinf1225.ezmeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class menu extends AppCompatActivity {
    private static final int REQUEST_CODE = 1 ;
    private Button ButtonSearch ;
    private Button ButtonCatalogue ;
    private Button ButtonSuggest ;
    private Button ButtonSignOut ;
    //private Button ButtonUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        ButtonSignOut = (Button) findViewById(R.id.button_signout);
        ButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu.this, SignInActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        ButtonSearch = (Button) findViewById(R.id.button_search);
        ButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu.this, search.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        ButtonCatalogue = (Button) findViewById(R.id.button_catalogue);
        ButtonCatalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu.this, catalogue.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        ButtonSuggest = (Button) findViewById(R.id.button_suggest);
        ButtonSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu.this, suggest.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        /*ButtonUser = (Button) findViewById(R.id.button_user);
        ButtonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu.this, menu.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });*/
    }

}

