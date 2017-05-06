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
import android.widget.Spinner;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Laurent on 24/04/2017-25/04.
 */

public class SignUpActivity extends Activity {
    private static final String TAG="SignUpActivity";
    private static final int REQUEST_CODE=1;

    private EditText username_text, password_text, age_text, address_text;
    private Button signup_button;
    private TextView signin_link;
    private Spinner gender_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sign_up);

        this.username_text=(EditText)this.findViewById(R.id.username_input);
        this.password_text=(EditText)this.findViewById(R.id.password_input);
        this.age_text=(EditText)this.findViewById(R.id.age_input);
        this.address_text=(EditText)this.findViewById(R.id.address_input);
        this.signup_button=(Button)this.findViewById(R.id.signup_button);
        this.signin_link=(TextView)this.findViewById(R.id.signin_link);
        this.gender_spinner=(Spinner)this.findViewById(R.id.gender_spinner);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.gender_spinner.setAdapter(adapter);

        this.signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        this.signin_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    public void signup() {
        final String username=this.username_text.getText().toString();
        final String password=this.password_text.getText().toString();
        final String age=this.age_text.getText().toString();
        final String address=this.address_text.getText().toString();
        final String gender=String.valueOf(this.gender_spinner.getSelectedItem());
        if(!this.validateData(username,password,age,address,gender)) {
            signupFailed();
            return;
        }

        this.signup_button.setEnabled(false);
        this.signin_link.setEnabled(false);
        final ProgressDialog progress=new ProgressDialog(SignUpActivity.this);
        progress.setIndeterminate(true);
        progress.setMessage(this.getString(R.string.authenticate_label));
        progress.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if(addToDB(username,password,age,address,gender)) {
                            signupSuccess();
                        } else {
                            signupFailed();
                        }
                        progress.dismiss();
                    }
                },3000);
    }
    public boolean validateData(String username, String password, String age, String address, String gender) {
        boolean flag=true;
        if(username.isEmpty()) {
            this.username_text.setError(this.getString(R.string.invalid_username_label));
            flag=false;
        }
        if(password.isEmpty()) {
            this.password_text.setError(this.getString(R.string.invalid_password_label));
            flag=false;
        }
        try {
            int age_number=Integer.parseInt(age);
            if(age_number<=0 || age_number>100) {
                this.age_text.setError(this.getString(R.string.invalid_age_label));
                flag=false;
            }
        } catch(NumberFormatException e) {
            this.age_text.setError(this.getString(R.string.invalid_age_label));
            flag=false;
        }
        if(address.isEmpty()) {
            this.address_text.setError(this.getString(R.string.invalid_address_label));
            flag=false;
        }
        if(!this.arrayContains(gender,this.getResources().getStringArray(R.array.gender))) {
            TextView errorText=(TextView)this.gender_spinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText(this.getString(R.string.invalid_gender_label));
            flag=false;
        }
        MyDatabase db=new MyDatabase(this);
        if(db.checkUsername(username)) {
            this.username_text.setError(this.getString(R.string.invalid_username_label));
            flag = false;
        }

        return flag;
    }
    public boolean addToDB(String username, String password, String age, String address, String gender) {
        MyDatabase db=new MyDatabase(this);
        if(db.open()) {
            return db.addData(username,password,age,address,gender);
        } else {
            throw new Error("Impossible d'ouvir la DB");
        }
    }
    public boolean arrayContains(String elem, String[] array) {
        int size=array.length;
        for(int i=0; i<size; i++) {
            if(elem.equals(array[i]))
                return true;
        }
        return false;
    }
    public void signupSuccess() {
        this.signup_button.setEnabled(true);
        this.signin_link.setEnabled(true);
        Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
        this.startActivityForResult(intent,REQUEST_CODE);
    }
    public void signupFailed() {
        Toast.makeText(this.getBaseContext(), this.getString(R.string.unsuccessfull_register_label), Toast.LENGTH_LONG).show();
        this.signup_button.setEnabled(true);
        this.signin_link.setEnabled(true);
    }
}