package be.lsinf1225.ezmeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

/**
 * Ecris par Arthur van Stratum
 * Afiche un écran pour poster un avis ou une note
 * doit ètre appelé via un intent contenant AVIS_USERNAME le nom de l'utilisateur et AVIS_RECIPE,
 * le nom de la recette
 */
public class AvisActivity extends AppCompatActivity {
    private static final String TAG = AvisActivity.class.getSimpleName();

    private EditText comment;
    private Button setComment, setGrade;
    private RatingBar stars;
    private MyDatabase db;
    private String username;
    private String recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avis);
        db=new MyDatabase(this);
        this.comment=(EditText)this.findViewById(R.id.comment_input);
        this.setComment=(Button)this.findViewById(R.id.set_comment_button);
        this.setGrade=(Button)this.findViewById(R.id.set_grade_button);
        this.stars=(RatingBar)this.findViewById(R.id.ratingBar_input);
        this.stars.setStepSize(1);//on ne veux qu'un nombre entier d'étoiles
        this.setComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_comment();
            }
        });
        this.setGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_grade();
            }
        });
        Intent intent = getIntent();
        username = intent.getStringExtra("AVIS_USERNAME");
        recipe = intent.getStringExtra("AVIS_RECIPE");
    }

    @Override
    protected void onStart(){
        super.onStart();
        this.stars.setNumStars(db.getGrade(username,recipe));
        this.comment.setText(db.getComment(username,recipe));
    }

    public void set_comment(){
        final String comm_final=this.comment.getText().toString();
        if(db.open()) {
            db.addComment(comm_final, "Laurent", "Muffins au chocolat");//user, recipe));
        } else {
            throw new Error("Impossible d'ouvir la base de donnees");
        }

    }

    public void set_grade(){
        final int grade_final=(int)this.stars.getRating();//cast acceptable car la granularité du rating est de 1
        if(db.open()) {
            try {
                db.addGrade(grade_final, "Morgane", "Muffins au chocolat");//user, recipe);
                db.addGrade(grade_final+1, "Morgane", "Muffins au chocolat");//user, recipe);
            } catch (Throwable t){
                Toast toast = Toast.makeText(getApplicationContext(), "Nombre non valide d'étoiles envoyés", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            throw new Error("Impossible d'ouvir la base de donnees");
        }
    }
}
