package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**TO DO relier le buttonImage à la recette et charger la bonne image en fonction de la recette dans le imagebutton
 * Created by marti on 28-04-17.
 */

public class suggest extends Activity {
    private static final int REQUEST_CODE = 1 ;
    private Button ButtonRetour ;
    private Button ButtonAutre;
    private ImageButton imageButton;
    private TextView Recette_name;
    private RatingBar note;
    private List<String> suggestions=new ArrayList<>();
    private String suggestion;
    private int index =0 ;
    MyDatabase db = new MyDatabase(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest);

        suggestions = db.suggest();
        suggestion = suggestions.get(index);

        Recette_name =(TextView) findViewById(R.id.Recette_name);
        Recette_name.setText(suggestion);

        ButtonRetour = (Button) findViewById(R.id.button_retour);
        ButtonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(suggest.this, menu.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        note=(RatingBar)this.findViewById(R.id.ratingBar);
        note.setRating(db.getAvgGrade(suggestion));

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        Bitmap bitmap=db.getImageRecette(suggestion);
        imageButton.setImageBitmap(bitmap);
        imageButton.setOnClickListener(new View.OnClickListener() {

                                           @Override
                                           public void onClick(View arg0) {

                                               Toast.makeText(suggest.this,
                                                       "ImageButton is clicked!", Toast.LENGTH_SHORT).show();

                                           }
                                       });
        ButtonAutre = (Button) findViewById(R.id.button_autre);
        ButtonAutre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                if (index>=suggestions.size())
                {
                    Toast.makeText(suggest.this, "Plus de suggestions", Toast.LENGTH_SHORT).show();
                }
                else {
                    suggestion = suggestions.get(index);
                    Recette_name.setText(suggestion);
                    Bitmap bitmap=db.getImageRecette(suggestion);
                    imageButton.setImageBitmap(bitmap);
                    note.setRating(db.getAvgGrade(suggestion));
                }
            }
        });
    }
}
