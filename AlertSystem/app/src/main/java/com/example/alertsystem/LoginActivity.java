package com.example.alertsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private Spinner spinnerRole;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private RadioGroup radioRole;
    private String role;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        radioRole = (RadioGroup)findViewById(R.id.radioRole);
        radioRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                // Method 1 For Getting Index of RadioButton
                pos = radioRole.indexOfChild(findViewById(checkedId));

                switch (pos)
                {
                    case 0 :
                        role = "Student";
                        Toast.makeText(LoginActivity.this, "Role : " + role , Toast.LENGTH_SHORT).show();
                        break;
                    case 1 :
                        role = "Instructor";
                        Toast.makeText(LoginActivity.this, "Role :  " + role , Toast.LENGTH_SHORT).show();
                        break;
                    default :
                        role = "Student";
                        Toast.makeText(LoginActivity.this, "Role :  " + role , Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {
            //profile activity here
            if (role.equals("Instructor")) {
                // go to instructor's page
                startActivity(new Intent(getApplicationContext(), InstructorActivity.class));
                finish();
            } else if (role.equals("Student")) {
                // go to student's page
                startActivity(new Intent(getApplicationContext(), StudentActivity.class));
                finish();
            }

        }

        progressDialog = new ProgressDialog(this);

        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        textViewSignup = (TextView) findViewById(R.id.textViewSignup);
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }
    private void signIn(){
        Log.d(TAG, "signIn");
        if(!validateForm()){
            return;
        }
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        String webmail = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        webmail = processWebmail(webmail);

        firebaseAuth.signInWithEmailAndPassword(webmail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(LoginActivity.this, "Logged In " + user , Toast.LENGTH_SHORT).show();
                            if (role.equals("Instructor")) {
                                // go to instructor's page
                                startActivity(new Intent(getApplicationContext(), InstructorActivity.class));
                                finish();
                            } else if (role.equals("Student")) {
                                // go to student's page
                                startActivity(new Intent(getApplicationContext(), StudentActivity.class));
                                finish();
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private String processWebmail(String webmail) {
        if (webmail.contains("@")) {
            return webmail;
        } else {
            return webmail + "@iitg.ernet.in";
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
            editTextEmail.setError("Required");
            result = false;
        } else {
            editTextEmail.setError(null);
        }

        if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
            editTextPassword.setError("Required");
            result = false;
        } else {
            editTextPassword.setError(null);
        }

        return result;
    }
    @Override
    public void onClick(View view) {
        if (view == buttonSignIn) {
            signIn();
        }

        if (view == textViewSignup) {
            // will open the register activity here
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
