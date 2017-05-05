package be.lsinf1225.ezmeal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  TO DO : WHERE TO SEND WHEN LOG WITH WHAT INFO
 * Created by Laurent on 23/04/2017.
 */

public class SignInActivity extends Activity {
    private static final String TAG="SignInActivity";
    private static final int REQUEST_CODE=1;

    private EditText  username_text, password_text;
    private Button signin_button;
    private TextView signup_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sign_in);
        this.username_text=(EditText)this.findViewById(R.id.username_input);
        this.password_text=(EditText)this.findViewById(R.id.password_input);
        this.signin_button=(Button)this.findViewById(R.id.signin_button);
        this.signup_link=(TextView)this.findViewById(R.id.signup_link);

        this.signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });
        this.signup_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Changing to sign up");

                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==this.REQUEST_CODE && resultCode==this.RESULT_OK) {
            Log.d(this.TAG,"Successfull intent to SignInActivity");
        }
    }
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(false);
    }

    public void  signin() {
        Log.d(this.TAG, "Login");

        final String username=this.username_text.getText().toString();
        final String password=this.password_text.getText().toString();
        if(!this.validateData(username,password)) {
            signinFailed();
            return;
        }

        this.signin_button.setEnabled(false);
        final ProgressDialog progress=new ProgressDialog(SignInActivity.this);
        progress.setIndeterminate(true);
        progress.setMessage(this.getString(R.string.authenticate_label));
        progress.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if(checkDataDB(username,password)) {
                            signinSuccess();
                        } else {
                            signinFailed();
                        }
                        progress.dismiss();
                    }
                },3000);
    }
    public boolean validateData(String username, String password) {
        Log.d(this.TAG,"Validating : Username = "+username+"\tPassword = "+password);

        boolean flag=true;
        if(username.isEmpty()) {
            this.username_text.setError("Enter a valid username");
            flag=false;
        }
        if(password.isEmpty()) {
            this.password_text.setError("Enter a valid password");
            flag=false;
        }

        return flag;
    }
    public boolean checkDataDB(String username, String password) {
        MyDatabase db=new MyDatabase(this);
        if(db.open()) {
            return db.checkDataLogin(username,password);
        } else {
            throw new Error("Impossible d'ouvrir la base de donnees");
       }
    }
    public void signinSuccess() {
        Log.d(this.TAG,"Successfull login");

        this.signin_button.setEnabled(true);
        Intent intent=new Intent(SignInActivity.this,menu.class);
        this.startActivityForResult(intent,REQUEST_CODE);
    }
    public void signinFailed() {
        Log.d(this.TAG,"Unsuccessfull login");

        Toast.makeText(this.getBaseContext(), "Unsuccessful log in, try again", Toast.LENGTH_LONG).show();
        this.signin_button.setEnabled(true);
    }
}
