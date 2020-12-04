package com.example.thegram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private TextView textViewSignIn;
    private EditText editTextEmailAddress,editTextPhoneNumber;
    private EditText editTextPassword;
    private FirebaseAuth mAuth;
    private Button registerButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPhoneNumber = findViewById(R.id.editTextPhone);
        textViewSignIn = findViewById(R.id.textViewSignIn);
        registerButton = findViewById(R.id.registerButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,loginActivity.class);
                startActivity(intent);
    }
});

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegister();
            }
        });
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public void UserRegister(){
        final String email = editTextEmailAddress.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phoneNumber =editTextPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Your Email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter Password",Toast.LENGTH_LONG).show();
            return;
        }

        // createUserWithEmailAndPassword:
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db =mDatabase.child(user_id);;
                            current_user_db.child("password").setValue(password);
                            current_user_db.child("phone number").setValue(phoneNumber);

                            current_user_db.child("email").setValue(email);
                            current_user_db.child("Bio").setValue("Sample Bio");

                            mAuth.signOut();
                            Intent intent = new Intent(SignUpActivity.this,loginActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Snackbar.make(findViewById(android.R.id.content),"Email is already registered",Snackbar.LENGTH_LONG).setAction("Action",null).show();

                        }


                    }
                });
    }
    }
