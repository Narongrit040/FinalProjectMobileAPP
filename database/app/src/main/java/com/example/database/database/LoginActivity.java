package com.example.database.database;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
private Button buttonSignIn;
private EditText editTextEmail;
private EditText editTextPassword;
private TextView textViewSignup;

private FirebaseAuth firebaseAuth;

private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

firebaseAuth = FirebaseAuth.getInstance();
if(firebaseAuth.getCurrentUser() != null){
    finish();
    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

/* profile activity here
    FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference most = ref.child("database-742eb").child(user.getUid()).child("address");

    most.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String email = dataSnapshot.getValue(String.class);
            //do what you want with the email
            if(email != null){
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }else{


            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });*/


}

editTextEmail = (EditText) findViewById(R.id.editTextEmail);
editTextPassword = (EditText) findViewById(R.id.editTextPassword);
buttonSignIn = (Button) findViewById(R.id.buttonLogin);
textViewSignup = (TextView) findViewById(R.id.textViewLogin);

progressDialog = new ProgressDialog(this);

buttonSignIn.setOnClickListener(this);
textViewSignup.setOnClickListener(this);
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please Enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            // password is empty
            Toast.makeText(this, "Please Enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        // if the email and password are not empty
        // display a progress dialog
        progressDialog.setMessage("Login Please Wait ....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {



                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                        // start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v == buttonSignIn){
            userLogin();
        }
        if(v == textViewSignup){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
