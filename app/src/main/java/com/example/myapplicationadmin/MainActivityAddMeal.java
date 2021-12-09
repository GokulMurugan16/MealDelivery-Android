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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivityAddMeal extends AppCompatActivity {

    EditText et1;
    EditText et2;
    EditText et3;

    EditText et4;
    EditText et5;
    FirebaseFirestore db;

    String image;
    String mName;
    String mPmonth = "0";
    String mP6month = "0";
    String mDescription;
    String mAllergy = "None";
    Bitmap selectedPhoto;
    ImageView imgv;

    public final static int  TAKE_PHOTO_ACTIVITY_REQUEST_CODE = 999;
    public final static int  GET_PHOTO_FROM_GALLERY_REQUEST_CODE = 1500;
    public String fileName = "myphoto.jpg";
    File photoFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_meal);

        db = FirebaseFirestore.getInstance();

        et1 = (EditText) findViewById(R.id.mealName);
        et2 = (EditText) findViewById(R.id.perMonthPrice);
        et3 = (EditText) findViewById(R.id.per6MonthPrice);
        et4 = (EditText) findViewById(R.id.desc);
        et5 = (EditText) findViewById(R.id.allergy);

        imgv = (ImageView) findViewById(R.id.mImageView);

        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivityAddMeal.this)
                        .setTitle("Select Options")
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                photoFile = getFileForPhoto(fileName);
                                Uri fileProvider = FileProvider.getUriForFile(MainActivityAddMeal.this, "com.example.cameraandphotos", photoFile);
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

    public void addMeal(View view)
    {
        mName = et1.getText().toString();
        mPmonth = et2.getText().toString();
        mP6month = et3.getText().toString();
        mDescription = et4.getText().toString();
        mAllergy = et5.getText().toString();
        image = bitmapToString(selectedPhoto);


        if(!mName.isEmpty()&&!mPmonth.isEmpty()&&!mP6month.isEmpty()&&!mDescription.isEmpty())
        {
            mealDataAdd(image,mName,mPmonth,mP6month,mDescription,mAllergy);
            et1.setText("");
            et2.setText("");
            et3.setText("");
            et4.setText("");
            et5.setText("");
            imgv.setImageBitmap(null);
            finish();
            return;

        }
        else
        {
            Toast.makeText(MainActivityAddMeal.this, "Task Un Successful",
                    Toast.LENGTH_SHORT).show();
        }

    }


    public void mealDataAdd(String image, String name, String perMonth, String per6month, String desc, String allergy)
    {
        Map<String, Object> meals = new HashMap<>();
        meals.put("mealName", name);
        meals.put("mealImage", image);
        meals.put("oneMonth", perMonth);
        meals.put("sixMonth", per6month);
        meals.put("description", desc);
        meals.put("allergy", allergy);


        db.collection("meals")
                .add(meals)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivityAddMeal.this, "Meal Added Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
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
}