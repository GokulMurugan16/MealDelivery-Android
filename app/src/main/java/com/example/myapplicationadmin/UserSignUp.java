package com.example.myapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserSignUp extends AppCompatActivity {

    EditText et1;
    EditText et2;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        mAuth = FirebaseAuth.getInstance();
        et1 = (EditText) findViewById(R.id.userNameuser);
        et2 = (EditText) findViewById(R.id.userpassworduser);


    }

    public void signup(View view)
    {
        if(!et1.getText().toString().isEmpty()&&!et2.getText().toString().isEmpty()) {
            String email = et1.getText().toString();
            String password = et2.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                et1.setText("");
                                et1.setText("");
                                Intent i = new Intent(UserSignUp.this, User.class);
                                startActivity(i);

                            } else {
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(UserSignUp.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        else
        {
            Toast.makeText(UserSignUp.this, "Invalid Details.",
                    Toast.LENGTH_SHORT).show();

        }




    }




}