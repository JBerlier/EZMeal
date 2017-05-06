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
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    public void  signin() {
        final String username=this.username_text.getText().toString();
        final String password=this.password_text.getText().toString();
        if(!this.validateData(username,password)) {
            signinFailed();
            return;
        }

        this.signin_button.setEnabled(false);
        this.signup_link.setEnabled(false);
        final ProgressDialog progress=new ProgressDialog(SignInActivity.this);
        progress.setIndeterminate(true);
        progress.setMessage(this.getString(R.string.authenticate_label));
        progress.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if(checkDataDB(username,password)) {
                            signinSuccess(username);
                        } else {
                            signinFailed();
                        }
                        progress.dismiss();
                    }
                },3000);
    }
    public boolean validateData(String username, String password) {
        boolean flag=true;
        if(username.isEmpty()) {
            this.username_text.setError(this.getString(R.string.invalid_username_label));
            flag=false;
        }
        if(password.isEmpty()) {
            this.password_text.setError(this.getString(R.string.invalid_password_label));
            flag=false;
        }

        return flag;
    }
    public boolean checkDataDB(String username, String password) {
        MyDatabase db=new MyDatabase(this);
        if(db.open()) {
            return db.checkPassword(username,password);
        } else {
            throw new Error("Impossible d'ouvrir la base de donnees");
        }
    }
    public void signinSuccess(String username) {
        this.signin_button.setEnabled(true);
        this.signup_link.setEnabled(true);
        Intent intent=new Intent(SignInActivity.this,menu.class);
        intent.putExtra("USERNAME_INFO",username);
        this.startActivityForResult(intent,REQUEST_CODE);
    }
    public void signinFailed() {
        Toast.makeText(this.getBaseContext(), this.getString(R.string.unsuccessfull_login_label), Toast.LENGTH_LONG).show();
        this.signin_button.setEnabled(true);
        this.signup_link.setEnabled(true);
    }
}