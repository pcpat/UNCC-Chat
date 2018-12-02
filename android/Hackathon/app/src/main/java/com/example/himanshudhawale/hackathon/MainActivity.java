package com.example.himanshudhawale.hackathon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {


    EditText emailField, passwordField;
    Button loginBtn, signBtn;
    String USER_KEY="Main";
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();




        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, DashBoard.class);

            startActivity(intent);
        }




        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginBtn =  findViewById(R.id.loginBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //On Click Activity

                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String verEmail=email.substring(email.indexOf("@"),email.length());

                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty values entered", Toast.LENGTH_LONG).show();
                }
                else if(!(verEmail.equals("@uncc.edu")))
                {
                    Toast.makeText(MainActivity.this, "Only 49ers are allowed", Toast.LENGTH_SHORT).show();

                }

                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("test", "signInWithEmail:onComplete:" + task.isSuccessful());
                                    if (!task.isSuccessful()) {
                                        Log.w("test", "signInWithEmail:failed", task.getException());
                                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(MainActivity.this, DashBoard.class);
                                       // intent.putExtra(USER_KEY, user);
                                         startActivity(intent);
                                    }
                                }

                            });

                }

            }
        });




        signBtn =  findViewById(R.id.signUpBtn);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });

    }
}
