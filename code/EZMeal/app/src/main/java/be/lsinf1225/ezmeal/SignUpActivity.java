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
                Log.d(TAG,"Changing to sign in");

                Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(this.TAG,"Receive an intent");
        if(requestCode==this.REQUEST_CODE && resultCode==this.RESULT_OK) {
            Log.d(this.TAG,"Successfull intent");
        }
    }
    @Override
    public void onBackPressed() {
        Log.d(this.TAG,"Back button pressed");
        this.moveTaskToBack(false);
    }

    public void signup() {
        Log.d(TAG,"Register");

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
        Log.d(this.TAG,"Validating : Username = "+username+"\tPassword = "+password+"\t Age = "+age+"\t address = "+address+"\t gender = "+gender);

        boolean flag=true;
        if(username.isEmpty()) {
            this.username_text.setError("Enter a valid username");
            flag=false;
        }
        if(password.isEmpty()) {
            this.password_text.setError("Enter a valid password");
            flag=false;
        }
        try {
            int age_number=Integer.parseInt(age);
            if(age_number<=0 || age_number>100) {
                this.age_text.setError("Enter a valid age");
                flag=false;
            }
        } catch(NumberFormatException e) {
            this.age_text.setError("Enter a valid age");
            flag=false;
        }
        if(address.isEmpty()) {
            this.address_text.setError("Enter a valid address");
            flag=false;
        }
        if(!this.arrayContains(gender,this.getResources().getStringArray(R.array.gender))) {
            TextView errorText=(TextView)this.gender_spinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Enter a valid gender");
            flag=false;
        }
        MyDatabase db=new MyDatabase(this);
        if(db.open()) {
            if(db.checkUsernameDB(username)) {
                this.username_text.setError("Username already taken");
                flag=false;
            }
        } else {
            throw new Error("Impossible d'ouvir la base de donnees");
        }

        return flag;
    }
    public boolean addToDB(String username, String password, String age, String address, String gender) {
        MyDatabase db=new MyDatabase(this);
        if(db.open()) {
            return db.addDataRegister(username,password,age,address,gender);
        } else {
            throw new Error("Impossible d'ouvir la base de donnees");
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
        Log.d(this.TAG,"Successfull login");

        this.signup_button.setEnabled(true);
        Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
        this.startActivityForResult(intent,REQUEST_CODE);
    }
    public void signupFailed() {
        Log.d(this.TAG,"Unssuccessful registering");

        Toast.makeText(this.getBaseContext(), "Unsuccessful registering, try again", Toast.LENGTH_LONG).show();
        this.signup_button.setEnabled(true);
    }
}
