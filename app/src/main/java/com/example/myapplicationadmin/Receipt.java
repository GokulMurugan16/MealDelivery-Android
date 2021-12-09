package com.example.myapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Receipt extends AppCompatActivity {

    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;

    String Meal;
    int Price;
    String Sub;
    int Total;
    FirebaseFirestore db;
    String refNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        db = FirebaseFirestore.getInstance();
        Intent g = getIntent();

        Meal = g.getStringExtra("Name");
        Price = g.getIntExtra("Price",0);
        Sub = g.getStringExtra("sub");

        tv1 = (TextView) findViewById(R.id.mealNameReceipt);
        tv2 = (TextView) findViewById(R.id.mealPriceReceipt);
        tv3 = (TextView) findViewById(R.id.mealTaxReceipt);
        tv4 = (TextView) findViewById(R.id.mealTotalReceipt);
        tv5 = (TextView) findViewById(R.id.mealRefReceipt);
        totalCalc();
        addMeal(Meal,Total,Sub,refNum);


    }


    public void totalCalc()
    {
        tv1.setText("Meal Name : "+Meal);
        tv2.setText("Meal Price : " + Price + " $");
        float tax = (float) (Price*0.13);
        tv3.setText("Tax : "+tax+" $");
        Total = (int) (Price + tax);
        tv4.setText("Grand Total : "+Total+" $");
        refNum = refNum();
        tv5.setText("Reference Number : "+refNum);
    }

    public String refNum()
    {
        String a = "";

        String[] b = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","0"};
        for(int i = 0; i<5;i++)
        {
            Random r=new Random();
            int randomNumber=r. nextInt(b.length);
            a+= b[randomNumber];

        }
        return a;
    }

    public void addMeal(String Meal, int Price, String Sub, String RefNum)
    {

        Map<String, Object> user = new HashMap<>();
        user.put("Name", Meal);
        user.put("Price", Price);
        user.put("Subscription", Sub);
        user.put("Reference Number", RefNum);




        db.collection("orders")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });


    }


}