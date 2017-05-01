package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by marti on 28-04-17.
 */

public class search extends Activity {
    private static final int REQUEST_CODE = 1 ;
    private Button ButtonRetour ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        ButtonRetour = (Button) findViewById(R.id.button_retour);
        ButtonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(search.this, menu.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }
}

