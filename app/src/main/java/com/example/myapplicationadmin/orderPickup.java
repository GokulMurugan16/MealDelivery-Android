package com.example.myapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class orderPickup extends AppCompatActivity {

    FirebaseFirestore db;
    int idPos;
    ArrayList<String> pickups = new ArrayList<String>();
    ListView lv1;
    ArrayList<String> done = new ArrayList<String>();
    ArrayAdapter<String> adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pickup);
        db = FirebaseFirestore.getInstance();

        lv1 = (ListView) findViewById(R.id.listViewOrder);
        read();
    }

    public void read()
    {
        db.collection("OrderPickup")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                pickups.add(document.getData().get("Date")+" | "+document.getData().get("Details")+" | Parking : "+document.getData().get("Parking"));
                                done.add(document.getId());
                            }
                            adp = new ArrayAdapter<String>(orderPickup.this, android.R.layout.simple_list_item_1,pickups);
                            lv1.setAdapter(adp);
                            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    idPos = position;
                                    new AlertDialog.Builder(orderPickup.this)
                                            .setTitle("Delivered?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                 delete(idPos);
                                                }
                                            }).show();
                                }
                            });
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });




    }


    void delete(int id)
    {
        final String TAG  = "Hello";
        db.collection("OrderPickup")
                .document(done.get(id))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Hello","Deleted Sucessfully");
                        adp.notifyDataSetChanged();
                        finish();

                    }
                });


    }


}