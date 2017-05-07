package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class AccountActivity extends Activity {
    private static final String TAG="AccountActivity";
    private static final int REQUEST_CODE=1;

    private EditText username_input, new_password_input, old_password_input, age_input, address_input;
    private Spinner gender_spinner;
    private Button cancel_button, change_info_button;

    private static String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.account_info);
        if(this.getIntent().hasExtra("USERNAME_INFO")) // Recupere le nom d'utilisateur
            this.username=this.getIntent().getStringExtra("USERNAME_INFO");

        this.username_input=(EditText)this.findViewById(R.id.username_input);
        this.old_password_input=(EditText)this.findViewById(R.id.old_password_input);
        this.new_password_input=(EditText)this.findViewById(R.id.new_password_input);
        this.age_input=(EditText)this.findViewById(R.id.age_input);
        this.address_input=(EditText)this.findViewById(R.id.address_input);
        this.change_info_button=(Button)this.findViewById(R.id.account_change_button);
        this.cancel_button=(Button)this.findViewById(R.id.cancel_button);
        this.gender_spinner=(Spinner)this.findViewById(R.id.gender_spinner);

        this.change_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_info();
            }
        });
        this.cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountActivity.this,menu.class);
                intent.putExtra("USERNAME_INFO",username);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        this.putInformation();
    }

    // Remplit les champs avec les informations de l'utilisateur, sauf le mot de passe
    public void putInformation() {
        MyDatabase db=new MyDatabase(this);
        String age=db.getAge(username);
        String address=db.getAddress(username);
        String gender=db.getGender(username);

        this.username_input.setText(username);
        this.age_input.setText(age);
        this.address_input.setText(address);

        // On doit reprendre le sexe de l'utilisateur dans la DB et le traduire
        String[] listGender=this.getResources().getStringArray(R.array.gender);
        int index=0; // Indice dans la ressource ou la traduction se trouve
        switch(gender) {
            case "Male":
                gender=listGender[0];
                index=0;
                break;
            case "Female":
                gender=listGender[1];
                index=1;
                break;
            case "Other":
                gender=listGender[2];
                index=2;
                break;
        }
        listGender[index]=listGender[0];
        listGender[0]=gender;

        // Remplit la liste avec les differents sexe en commencant par celui de l'utilisateur
        ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, listGender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.gender_spinner.setAdapter(adapter);
    }

    /**
     * Met a jour les informations dans la DB
     * Le mot de passe est change si et seulement le champ de l'ancien mot de passe est correct et que
     *      le nouveau mot de passe n'est pas vide
     */
    public void change_info() {
        final String new_username=username_input.getText().toString();
        final String old_password=old_password_input.getText().toString();
        final String new_password=new_password_input.getText().toString();
        final String age=age_input.getText().toString();
        final String address=address_input.getText().toString();
        final String gender=String.valueOf(this.gender_spinner.getSelectedItem());

        if(!this.validateInfo(new_username,old_password,new_password,age,address,gender)) {
            changeFailed();
            return;
        }

        this.cancel_button.setEnabled(false);
        this.change_info_button.setEnabled(false);
        final ProgressDialog progress=new ProgressDialog(AccountActivity.this);
        progress.setIndeterminate(true);
        progress.setMessage(this.getString(R.string.authenticate_label));
        progress.show();

        // Joue l'animation de progression pendant trois secondes avant de continuer le programme
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if(replaceDB(new_username,old_password,new_password,age,address,gender)) {
                            changeSuccess();
                        } else {
                            changeFailed();
                        }
                        progress.dismiss();
                    }
                },3000);
    }

    /**
     * Verifie que les differentes informations donnees sont valides
     * Une information est valide si : le champ n'est pas vide
     *                                 @age est bien un ciffre positif < 100
     *                                 @gender est bien string contenu dans la liste de sexe contenu dans les ressources
     *                                 @username n'existe pas encore dans la DB
     * Quand une information n'est pas bonne, affiche un message d'erreur a cote du champ concerne
     */
    public boolean validateInfo(String username, String old_passsword, String new_password, String age, String address, String gender) {
        boolean flag=true;
        MyDatabase db=new MyDatabase(this);

        if(username.isEmpty()) {
            this.username_input.setError("Enter a valid username");
            flag=false;
        }
        // On ne peut pas changer le mot de passe si le nouveau mot de passe est vide
        if(!old_passsword.isEmpty() && new_password.isEmpty()) {
            this.old_password_input.setError("No new password entered");
            flag=false;
        }
        // On ne peut pas change le mot de passe si l'ancien mot de passe est vide
        if(old_passsword.isEmpty() && !new_password.isEmpty()) {
            this.new_password_input.setError("No old password entered");
            flag=false;
        }
        try {
            int age_number=Integer.parseInt(age);
            if(age_number<=0 || age_number>100) {
                this.age_input.setError("Enter a valid age");
                flag=false;
            }
        } catch(NumberFormatException e) {
            this.age_input.setError("Enter a valid age");
            flag=false;
        }
        if(address.isEmpty()) {
            this.address_input.setError("Enter a valid address");
            flag=false;
        }
        if(!this.arrayContains(gender,this.getResources().getStringArray(R.array.gender))) {
            TextView errorText=(TextView)this.gender_spinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Enter a valid gender");
            flag=false;
        }
        // Verifie si le nouveau nom d'utilisateur est deja dans la DB
        if(db.checkUsername(username) && !this.username.equals(username)) {
            this.username_input.setError("Username already taken");
            flag=false;
        }
        // Si les deux champs pour le mot de passe sont remplis, indique si l'ancien mot de passe est correct
        if(!new_password.isEmpty() && !old_passsword.isEmpty() && !db.checkPassword(this.username,old_passsword)) {
            this.old_password_input.setError("Wrong password");
            flag=false;
        }

        return flag;
    }
    public boolean replaceDB(String username,String old_password, String new_password, String age, String address, String gender) {
        MyDatabase db=new MyDatabase(this);
        if(db.open()) {
            return db.replaceData(this.username,username,new_password,age,address,gender);
        } else {
            throw new Error("Impossible d'ouvrir la DB");
        }
    }
    // Verifie que @elem est contenu dans @array
    public boolean arrayContains(String elem, String[] array) {
        int size=array.length;
        for(int i=0; i<size; i++) {
            if(elem.equals(array[i]))
                return true;
        }
        return false;
    }
    // Echec des changements -> affiche message d'erreur + reactive le bouton/lien
    public void changeFailed() {
        this.change_info_button.setEnabled(true);
        this.cancel_button.setEnabled(true);
        Toast.makeText(this.getBaseContext(),this.getString(R.string.unsuccessfull_change_label), Toast.LENGTH_LONG).show();
    }
    // Success de la modification -> lance l'activitee du menu
    public void changeSuccess() {
        this.cancel_button.setEnabled(true);
        this.change_info_button.setEnabled(true);
        Intent intent=new Intent(AccountActivity.this,menu.class);
        intent.putExtra("USERNAME_INFO",username);
        startActivityForResult(intent, REQUEST_CODE);
    }
}