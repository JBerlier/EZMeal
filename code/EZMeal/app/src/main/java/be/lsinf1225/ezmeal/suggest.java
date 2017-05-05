package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marti on 28-04-17.
 */

public class suggest extends Activity {
    private static final int REQUEST_CODE = 1 ;
    private Button ButtonRetour ;
    private List<String> suggestions=new ArrayList<>();
    MyDatabase db = new MyDatabase(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest);
        suggestions =db.suggest();

        ButtonRetour = (Button) findViewById(R.id.button_retour);
        ButtonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(suggest.this, menu.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

}
