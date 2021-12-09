package com.example.myapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MealDescription extends AppCompatActivity {

    int pos;
    ArrayList<pojo> mealDesc = new ArrayList<>();
    FirebaseFirestore db;

    ImageView imageViewMeal;
    TextView mealName;
    TextView mealAllergy;
    TextView mealDescrip;
    TextView oneMonthPrice;
    TextView sixMonthPrice;
    TextView sixMonthIncentive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_description);

        Intent b = getIntent();
        pos = b.getIntExtra("PositionMeal",0);

        db = FirebaseFirestore.getInstance();

        imageViewMeal = (ImageView) findViewById(R.id.mealImage);
        mealName = (TextView) findViewById(R.id.mealNameUser);
        mealAllergy = (TextView) findViewById(R.id.mealAllergyAlert);
        mealDescrip = (TextView) findViewById(R.id.description);
        oneMonthPrice = (TextView) findViewById(R.id.oneMonthPrice);
        sixMonthPrice = (TextView) findViewById(R.id.sixMonthPrice);
        sixMonthIncentive = (TextView) findViewById(R.id.sixincentive);

        readData();

    }

    public void readData()
    {
        db.collection("meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                pojo p = new pojo(document.getData().get("mealImage").toString(),document.getData().get("mealName").toString(),document.getData().get("oneMonth").toString(),document.getData().get("sixMonth").toString(),document.getData().get("description").toString(),document.getData().get("allergy").toString(),document.getId());
                                mealDesc.add(p);
                            }

                            setUIData();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    public void setUIData()
    {
        imageViewMeal.setImageBitmap(convert(mealDesc.get(pos).getmImage()));
        mealName.setText(mealDesc.get(pos).getmName());
        mealAllergy.setText("Allergy Alert : "+mealDesc.get(pos).getmAllergy());
        mealDescrip.setText(mealDesc.get(pos).getmDesc());
        oneMonthPrice.setText("Price : "+mealDesc.get(pos).getmP1ame()+" $");
        sixMonthPrice.setText("Price : "+mealDesc.get(pos).getmP6ame()+" $");
        sixMonthIncentive.setText("Incentive : "+calcInc()+" $");


    }


    public void oneMonthPlan(View view)
    {
        Intent i = new Intent(MealDescription.this,Receipt.class);
        i.putExtra("Name",mealDesc.get(pos).getmName());
        String price = mealDesc.get(pos).getmP1ame();
        int Price = Integer.parseInt(price.trim());
        i.putExtra("Price",Price);
        i.putExtra("sub","One Month");
        startActivity(i);

    }

    public void sixMonthPlan(View view) {
        Intent i = new Intent(MealDescription.this,Receipt.class);
        i.putExtra("Name",mealDesc.get(pos).getmName());
        String price = mealDesc.get(pos).getmP6ame();
        int Price = Integer.parseInt(price.trim());
        i.putExtra("Price",Price);
        i.putExtra("sub","Six Month");
        startActivity(i);
    }

    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public int calcInc()
    {
       String a = mealDesc.get(pos).getmP1ame();
       int b = Integer.parseInt(a.trim());
       String c = mealDesc.get(pos).getmP6ame();
       int d = Integer.parseInt(c.trim());

       int inc = (b*6) - d;

        return inc;
    }

}