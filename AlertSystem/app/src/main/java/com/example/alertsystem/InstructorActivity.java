package com.example.alertsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InstructorActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
//    private TextView textViewInstructor;
    private TextView textViewLoggedin;
//    private TextView textViewCourseID;
    private Button buttonSignout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user= firebaseAuth.getCurrentUser();

//        textViewInstructor = (TextView) findViewById(R.id.textViewInstructor);
        textViewLoggedin = (TextView) findViewById(R.id.textViewLoggedin);
        String webmail = user.getEmail();
        String username = processWebmail(webmail);
        textViewLoggedin.setText("Welcome, " + username );
//        textViewCourseID = (TextView) findViewById(R.id.textViewYourCourseID);

        buttonSignout = (Button) findViewById(R.id.buttonSignout);

        buttonSignout.setOnClickListener(this);
    }

    private String processWebmail(String webmail) {
        if (webmail.contains("@")) {
            return webmail;
        } else {
            return webmail + "@iitg.ernet.in";
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignout) {
            // SignOut
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
