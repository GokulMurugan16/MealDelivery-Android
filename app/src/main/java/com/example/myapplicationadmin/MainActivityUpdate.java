package com.example.myapplicationadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivityUpdate extends AppCompatActivity {

    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    EditText et5;
    FirebaseFirestore db;

    MainActivity2 update = new MainActivity2();

    String image;
    String mName;
    String mPmonth = "0";
    String mP6month = "0";
    String mDescription;
    String mAllergy = "None";
    int id;
    Bitmap selectedPhoto;

    ImageView imgv;

    public final static int  TAKE_PHOTO_ACTIVITY_REQUEST_CODE = 999;
    public final static int  GET_PHOTO_FROM_GALLERY_REQUEST_CODE = 1500;
    public String fileName = "myphoto.jpg";
    File photoFile;

    ArrayList<pojo> getArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_update);

        db = FirebaseFirestore.getInstance();
        imgv = (ImageView) findViewById(R.id.mImageView);
        et1 = (EditText) findViewById(R.id.mealName);
        et2 = (EditText) findViewById(R.id.perMonthPrice);
        et3 = (EditText) findViewById(R.id.per6MonthPrice);
        et4 = (EditText) findViewById(R.id.desc);
        et5 = (EditText) findViewById(R.id.allergy);


        Intent i = getIntent();
        id = i.getIntExtra("Position",0);

        readData();



    }
    public static String bitmapToString (Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public File getFileForPhoto(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "TAG");

        if (mediaStorageDir.exists() == false && mediaStorageDir.mkdirs() == false) {
            Log.d("TAG", "Cannot create directory for storing photos");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedPhoto = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                imgv.setImageBitmap(selectedPhoto);

            } else {
                Toast t = Toast.makeText(this, "Not able to take photo", Toast.LENGTH_SHORT);
                t.show();
            }
        }

        if (requestCode == GET_PHOTO_FROM_GALLERY_REQUEST_CODE) {
            if (data != null) {
                Uri photoURI = data.getData();

                Log.d("TAG", "Path to the photo the user selected: " + photoURI.toString());
                try {
                    selectedPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);

                    imgv.setImageBitmap(selectedPhoto);
                }
                catch (FileNotFoundException e) {
                    Log.d("TAG", "FileNotFoundException: Unable to open photo gallery file");
                    e.printStackTrace();
                }
                catch (IOException e) {
                    Log.d("TAG", "IOException: Unable to open photo gallery file");
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateMeal(View view)
    {
        mName = et1.getText().toString();
        mPmonth = et2.getText().toString();
        mP6month = et3.getText().toString();
        mDescription = et4.getText().toString();
        mAllergy = et5.getText().toString();
        image = bitmapToString(selectedPhoto);

            mealUpdate(image,mName,mPmonth,mP6month,mDescription,mAllergy,id);
            et1.setText("");
            et2.setText("");
            et3.setText("");
            et4.setText("");
            et5.setText("");
            imgv.setImageBitmap(null);
            finish();
            return;
    }

    public void mealUpdate(String image, String name, String perMonth, String per6month, String desc, String allergy, int id)
    {


        Map<String, Object> meals = new HashMap<>();
        meals.put("mealName", name);
        meals.put("mealImage", image);
        meals.put("oneMonth", perMonth);
        meals.put("sixMonth", per6month);
        meals.put("description", desc);
        meals.put("allergy", allergy);


        db.collection("meals")
                .document(getArray.get(id).getId())
                .update(meals)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivityUpdate.this,"Successfully Updated", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("case", "Error adding document", e);
                    }
                });

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
                                    getArray.add(p);

                                }

                            setData();
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });
    }

    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void setData()
    {
        et1.setText(getArray.get(id).getmName());
        et2.setText(getArray.get(id).getmP1ame());
        et3.setText(getArray.get(id).getmP6ame());
        et4.setText(getArray.get(id).getmDesc());
        et5.setText(getArray.get(id).getmAllergy());
        imgv.setImageBitmap(convert(getArray.get(id).getmImage()));
        selectedPhoto = convert(getArray.get(id).getmImage());


        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivityUpdate.this)
                        .setTitle("Select Options")
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                photoFile = getFileForPhoto(fileName);
                                Uri fileProvider = FileProvider.getUriForFile(MainActivityUpdate.this, "com.example.cameraandphotos", photoFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(intent, TAKE_PHOTO_ACTIVITY_REQUEST_CODE);
                                }

                            }
                        })
                        .setNegativeButton("Photo Library", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(intent, GET_PHOTO_FROM_GALLERY_REQUEST_CODE);
                                }
                            }
                        }).show();

            }
        });

    }


    public void deleteMeal(View view)
    {

        db.collection("meals")
                .document(getArray.get(id).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivityUpdate.this,"Successfully Deleted", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("case", "Error adding document", e);
                    }
                });



    }
}