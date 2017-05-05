package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by maximemawait on 21/04/2017.
 */

public class Etape_middle extends Activity {
    private static final String TAG="Step_middle_activity";
    private static final int REQUEST_CODE=1;

    private Button etap_prev, etap_suiv;

    //Intent prev_intent = getIntent();

    //private String rec_name = prev_intent.getStringExtra("firstKeyName");
    //private int step_nb = prev_intent.getIntExtra("secondKeyName",0); //Si pas de valeur, c'est la premiere etape

    private String recette_name = "Lasagne";
    private int step_number = 2;
    private int max_nb_step;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.etape_middlescreen);

        MyDatabase db = new MyDatabase(this);

        if(db.open()) {
            String step_name = db.getStepNameColumn(recette_name,step_number);
            String step_explanation = db.getStepExplanationColumn(recette_name,step_number);
            max_nb_step = db.getNbStep(recette_name);
            LinearLayout l_title =  (LinearLayout) findViewById(R.id.etape_title);
            LinearLayout l_explain = (LinearLayout) findViewById(R.id.etape_text);

            TextView text_name = new TextView(this);
            text_name.setText(step_name);

            l_title.addView(text_name);

            TextView text_explain = new TextView(this);
            text_explain.setText(step_explanation);

            l_explain.addView(text_explain);

            //String step_name = "Couper les oignon.";

            //String step_explanation = "Couper maintenant les oignons en petits cube de 1cm  de côté.";


            this.etap_prev = (Button) findViewById(R.id.etap_prec);
            this.etap_suiv = (Button) findViewById(R.id.etap_suiv);

            this.etap_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setEtap_prev();
                }
            });

            this.etap_suiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setEtap_suiv();
                }
            });

        } else {
            throw new Error("Impossible d'ouvrir la base de donnees");
        }
    }

    public void setEtap_prev(){
        if (step_number > 0){
            Intent intent = new Intent(this, Etape_middle.class);
            intent.putExtra("firstKeyName",recette_name);
            intent.putExtra("secondKeyName",step_number-1);
            startActivityForResult(intent,REQUEST_CODE);
        }
        else {
            this.finish();
        }
    }

    public void setEtap_suiv(){
        if (step_number < max_nb_step){
            Intent intent = new Intent(this, Etape_middle.class);
            intent.putExtra("firstKeyName",recette_name);
            intent.putExtra("secondKeyName",step_number+1);
            startActivityForResult(intent,REQUEST_CODE);
        }
        else {
            this.finish();
        }
    }
}
