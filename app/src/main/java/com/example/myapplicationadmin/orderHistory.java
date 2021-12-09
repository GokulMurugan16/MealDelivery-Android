package com.example.myapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class orderHistory extends AppCompatActivity {

    FirebaseFirestore db;
    ArrayList<String> mea = new ArrayList<>();
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        db = FirebaseFirestore.getInstance();
        lv = (ListView) findViewById(R.id.listView);
        readData();



    }

    public void readData()
    {
        db.collection("orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                             mea.add(document.getData().get("Name").toString()+" | "+document.getData().get("Price").toString()+" $"+" | "+document.getData().get("Reference Number").toString()+" | "+document.getData().get("Subscription").toString());
                            }

                            ArrayAdapter<String> adp = new ArrayAdapter<String>(orderHistory.this, android.R.layout.simple_list_item_1,mea);
                            lv.setAdapter(adp);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent i = new Intent(orderHistory.this,userGetOrder.class);
                                    i.putExtra("details",mea.get(position));
                                    startActivity(i);
                                }
                            });

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}