package com.example.myapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserRecyclerView extends AppCompatActivity implements recyclerAdapter.OnEntryClickListener {


    FirebaseFirestore db;

    RecyclerView recyclerView;
    RecyclerView.Adapter rAdpapter;
    RecyclerView.LayoutManager rLayoutManager;

    ArrayList<pojo> mealListUser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recycler_view);

        db = FirebaseFirestore.getInstance();
        read();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerUser);
        recyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(this);

    }

    public void read()
    {
        db.collection("meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                pojo p = new pojo(document.getData().get("mealImage").toString(),document.getData().get("mealName").toString(),document.getData().get("oneMonth").toString(),document.getData().get("sixMonth").toString(),document.getData().get("description").toString(),document.getData().get("allergy").toString(),document.getId());
                                mealListUser.add(p);

                            }

                            if(mealListUser.size()!=0)
                            {
                                rAdpapter = new recyclerAdapter(mealListUser,UserRecyclerView.this);

                                recyclerView.setLayoutManager(rLayoutManager);
                                recyclerView.setAdapter(rAdpapter);
                            }


                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onEntryClick(View view, int position)
    {
        Log.d("Recycler", "Selected Index:" +position);
        Intent iMeal = new Intent(UserRecyclerView.this,MealDescription.class);
        iMeal.putExtra("PositionMeal",position);
        startActivity(iMeal);


    }

    public void viewOrder(View view) {

        Intent i = new Intent(UserRecyclerView.this,orderHistory.class);
        startActivity(i);
    }
}