package com.example.myapplicationadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class launchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    public void admin(View view) {
        Intent i = new Intent(launchActivity.this,MainActivity.class);
        startActivity(i);

    }

    public void user(View view) {

        Intent i = new Intent(launchActivity.this,User.class);
        startActivity(i);

    }
}