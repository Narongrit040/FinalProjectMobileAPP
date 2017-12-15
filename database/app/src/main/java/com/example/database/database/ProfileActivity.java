package com.example.database.database;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int PICK_IMAGE_REQUEST = 234;
    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout,buttonSave,buttonChoose, home;
    private DatabaseReference databaseReference;
    private EditText editTextName, editTextAddress, number, age1;
    private ImageView imageView;
    private Uri filePath;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

      //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        // if the user is not logged in
        // that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            // closing this activity
            finish();
            // starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Edit adress and name
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextName = (EditText) findViewById(R.id.editTextName);
        number = (EditText) findViewById(R.id.editTextNumber);
        age1 = (EditText) findViewById(R.id.editTextAge);

        // add to the people
        buttonSave = (Button) findViewById(R.id.buttonAddPeople);

        //get data from user in firebase
        FirebaseUser user = firebaseAuth.getCurrentUser();


        textViewUserEmail = (TextView)findViewById(R.id.textViewUserEmail);
// edit email name
        String a = user.getEmail();
        StringBuilder sb = new StringBuilder();
        char c[] =new char[a.length()];
        for(int i = 0 ; i<a.length() ; i++){
            c[i]=a.charAt(i);
            if(c[i]=='@'){
             break;
            }
            sb.append(c[i]);
        }

        textViewUserEmail.setText("Welcome "+sb.toString());

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        home = (Button)findViewById(R.id.ButtonHome);
        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.imageView);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);

        buttonChoose.setOnClickListener(this);
        home.setOnClickListener(this);

    }

        private void saveUserInformation(){

        String name = editTextName.getText().toString().trim();
        String add = editTextAddress.getText().toString().trim();
            String num = number.getText().toString().trim();
            String age = age1.getText().toString().trim();



        UserInformation userInformation = new UserInformation(age, add, name, num);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // save user information
        databaseReference.child(user.getUid()).setValue(userInformation);

        // upload image
            if(filePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading ....");
                progressDialog.show();
                Random rand = new Random();
                int n = rand.nextInt(1000);
                StorageReference riversRef = storageReference.child(user.getUid()+"/profile"+n+".jpg");

                riversRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"File Uploaded", Toast.LENGTH_LONG).show();

                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), exception.getMessage(),Toast.LENGTH_LONG).show();

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){

                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage(((int)progress)+"% Upload...");
                            }

                        });

            }else{
                //display a error toast
            }
        Toast.makeText(this, "Information", Toast.LENGTH_LONG).show();

    }

    private  void showFileChooser(){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select an Image"), PICK_IMAGE_REQUEST);
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData() != null){
        filePath = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onClick(View v) {
        // if logout is pressed
    if(v == buttonLogout){
        // logging out the user
    firebaseAuth.signOut();
        //closing acitivity
    finish();
        //starting login activity
    startActivity(new Intent(this, LoginActivity.class));

    }else if(v == buttonChoose){
        //open file chooser
        showFileChooser();

    }else if(v == buttonSave){

        saveUserInformation();


    }else if(v == home){
        finish();
        startActivity(new Intent(this, home.class));

    }
    }
}
