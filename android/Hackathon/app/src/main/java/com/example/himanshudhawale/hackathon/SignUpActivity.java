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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    EditText firstNameField, emailField, passwordField,lastNameField, rpasswordField;
    Button signUpBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstNameField =  findViewById(R.id.editText2);
        lastNameField= findViewById(R.id.editText3);
        emailField =  findViewById(R.id.editText4);
        passwordField =  findViewById(R.id.editText5);
        rpasswordField=findViewById(R.id.editText6);
        signUpBtn =  findViewById(R.id.signUpBtn);
        cancelBtn =  findViewById(R.id.cancelBtn);



        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();




        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String firstName = firstNameField.getText().toString();
                final String email = emailField.getText().toString();
                final String password = passwordField.getText().toString();
                final String lastName= lastNameField.getText().toString();
                final String rpassword=rpasswordField.getText().toString();
                String verEmail=email.substring(email.indexOf("@"),email.length());


                if(email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill all the details", Toast.LENGTH_LONG).show();
                } else if (password.length() < 6 ) {
                    Toast.makeText(SignUpActivity.this, "Password Length", Toast.LENGTH_LONG).show();
                }
                else if(!(password.equals(rpassword)))
                {
                    Toast.makeText(SignUpActivity.this, "Password not matched", Toast.LENGTH_SHORT).show();
                }
                else if(!(verEmail.equals("@uncc.edu"))){
                    Toast.makeText(SignUpActivity.this, "Only 49ers are allowed", Toast.LENGTH_SHORT).show();

                }
                    else
                {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
                                    } else {
                                        FirebaseUser fUser = mAuth.getCurrentUser();
                                        if (fUser != null) {

                                            User user = new User();

                                            String useruid = fUser.getUid();

                                            user.uID=useruid;
                                            user.first= firstName;
                                            user.last=lastName;
                                            user.email=email;
                                            user.password=password;


                                            mDatabase.getReference().child("users").child(useruid).setValue(user);

                                            Toast.makeText(SignUpActivity.this, "Success!", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(SignUpActivity.this, DashBoard.class);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });
                }

            }
        });



        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
