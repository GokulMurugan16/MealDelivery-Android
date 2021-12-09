package com.example.myapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements recyclerAdapter.OnEntryClickListener {

    FirebaseFirestore db;

    RecyclerView recyclerView;
    RecyclerView.Adapter rAdpapter;
    RecyclerView.LayoutManager rLayoutManager;

    ArrayList<pojo> mealList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        db = FirebaseFirestore.getInstance();
        read();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(this);




    }

    public void addMealButton(View view)
    {

        Intent i = new Intent(MainActivity2.this,MainActivityAddMeal.class);
        startActivity(i);
    }


    public void read()
        {
            db.collection("meals")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                mealList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    pojo p = new pojo(document.getData().get("mealImage").toString(),document.getData().get("mealName").toString(),document.getData().get("oneMonth").toString(),document.getData().get("sixMonth").toString(),document.getData().get("description").toString(),document.getData().get("allergy").toString(),document.getId());
                                    mealList.add(p);

                                }

                                if(mealList.size()!=0)
                                {
                                    rAdpapter = new recyclerAdapter(mealList,MainActivity2.this);

                                    recyclerView.setLayoutManager(rLayoutManager);
                                    recyclerView.setAdapter(rAdpapter);
//                                rAdpapter.setOnEntryClickListener(new recyclerAdapter.OnEntryClickListener() {
//                                    @Override
//                                    public void onEntryClick(View view, int position) {
//
//
//                                    }
//                                });
                                }


                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });
    }

    @Override
    protected void onResume() {
        super.onResume();
        read();
    }

    @Override
    public void onEntryClick(View view, int position) {

        Intent i = new Intent(MainActivity2.this,MainActivityUpdate.class);
        i.putExtra("Position", position);
        startActivity(i);


    }

    public void viewMeal(View view)
    {
        Intent o = new Intent(MainActivity2.this,orderPickup.class);
        startActivity(o);
    }
}
