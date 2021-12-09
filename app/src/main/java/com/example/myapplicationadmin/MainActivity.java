package com.example.myapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText et1;
    EditText et2;
    FirebaseFirestore db;

    ArrayList<admin> admin = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        et1 = (EditText) findViewById(R.id.userName);
        et2 = (EditText) findViewById(R.id.passWord);
        readAdminDb();


    }


    public void login(View view)
    {
        if(!et1.getText().toString().isEmpty() && !et2.getText().toString().isEmpty())
        {
            String userName = et1.getText().toString();
            String passWord = et2.getText().toString();
            for(int i =0;i<admin.size();i++)
            {
                if(admin.get(i).userName.equalsIgnoreCase(userName) && admin.get(i).password.equals(passWord))
                {
                    Intent i1 = new Intent(MainActivity.this,MainActivity2.class);
                    startActivity(i1);
                    et1.setText("");
                    et2.setText("");
                    return;
                }

            }

        }

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Invalid Details")
                .setMessage("Please Enter correct Details ")
                .setPositiveButton("ok", null).show();
    }








    void readAdminDb()
    {
        db.collection("admin")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                admin ad = new admin(document.getData().get("userName").toString(),document.getData().get("passWord").toString());
                                admin.add(ad);
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


}