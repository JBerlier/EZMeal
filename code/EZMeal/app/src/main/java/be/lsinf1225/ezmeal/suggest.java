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

/**TO DO relier le buttonImage à la recette
 * Created by marti on 28-04-17.
 */

public class suggest extends Activity {
    private static final int REQUEST_CODE = 1 ;
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

        note=(RatingBar)this.findViewById(R.id.ratingBar);
        note.setRating(db.getAvgGrade(suggestion));

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        Bitmap bitmap=db.getImageRecette(suggestion);
        imageButton.setImageBitmap(bitmap);
        imageButton.setOnClickListener(new View.OnClickListener() {

                                           @Override
                                           public void onClick(View arg0) {

                                               Intent i = new Intent(suggest.this, menu.class);//menu->RecetteActivity
                                               i.putExtra("recette",suggestion);
                                               startActivityForResult(i, REQUEST_CODE);

                                           }
                                       });
        Button ButtonAutre = (Button) findViewById(R.id.button_autre);
        ButtonAutre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                if (index>=suggestions.size())
                {
                    Toast.makeText(suggest.this,R.string.plus_de_sug, Toast.LENGTH_SHORT).show();
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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(suggest.this, menu.class);
        startActivityForResult(i, REQUEST_CODE);
    }
}
