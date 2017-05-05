package be.lsinf1225.ezmeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

/**
 * Créé par Arthur van Stratum
 */
public class AvisActivity extends AppCompatActivity {
    private static final String TAG = AvisActivity.class.getSimpleName();

    private EditText comment;
    private Button setComment, setGrade;
    private RatingBar stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avis);
        this.comment=(EditText)this.findViewById(R.id.comment_input);
        this.setComment=(Button)this.findViewById(R.id.set_comment_button);
        this.setGrade=(Button)this.findViewById(R.id.set_grade_button);
        this.stars=(RatingBar)this.findViewById(R.id.ratingBar_input);

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
    }

    public void set_comment(){
        final String comm_final=this.comment.getText().toString();
        MyDatabase db=new MyDatabase(this);
        if(db.open()) {
            db.addComment(comm_final, "jean-ma", "choucroute");//user, recipe));
        } else {
            throw new Error("Impossible d'ouvir la base de donnees");
        }

    }

    public void set_grade(){
        final int grade_final=this.stars.getNumStars();
        MyDatabase db=new MyDatabase(this);
        if(db.open()) {
            try {
                db.addGrade(grade_final, "Laurent", "Muffins au chocolat");//user, recipe);
            } catch (Throwable t){
                Toast toast = Toast.makeText(getApplicationContext(), "Nombre non valide d'étoiles envoyés", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            throw new Error("Impossible d'ouvir la base de donnees");
        }
    }
}
