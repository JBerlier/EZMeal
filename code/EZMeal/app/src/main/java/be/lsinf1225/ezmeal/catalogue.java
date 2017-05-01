package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by marti on 28-04-17.
 */

public class catalogue extends Activity{
    private static final int REQUEST_CODE = 1 ;
    private Button ButtonRetour ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalogue);

        ButtonRetour = (Button) findViewById(R.id.button_retour);
        ButtonRetour.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View view) {
                Intent i = new Intent(catalogue.this, menu.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

}
