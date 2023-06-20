package com.example.finalpam;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity implements View.OnClickListener {
    EditText email,password;
    Button login;
    TextView daftar;

    private FirebaseAuth mFire;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mFire = FirebaseAuth.getInstance();
        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.Login);
        daftar = findViewById(R.id.textView3);

        daftar.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentU = mFire.getCurrentUser();
        updateUI(currentU);
    }
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(login.this,
                    MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(login.this,"Log In First",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public boolean cekform(){
        boolean result = true;
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Required");
            result = false;
        } else {
            email.setError(null);
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
           password.setError("Required");
            result = false;
        } else {
            password.setError(null);
        }
        return result;
    }
 public void regis (String email, String password){
        if(!cekform()){
            return;
        }
     mFire.createUserWithEmailAndPassword(email, password)
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {

                     if (task.isSuccessful()) {
                         //  signup success
                         AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(login.this);
                         alertDialogBuilder.setTitle("Signup");
                         alertDialogBuilder.setMessage("Your account has been registered. Please sign in use your username and password.");
                         alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 dialogInterface.dismiss();
                             }
                         });
                         alertDialogBuilder.show();
                     } else {
                         task.getException().printStackTrace();
                         //  signup fail
                         final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "registered has been failed! Please try again.", Snackbar.LENGTH_INDEFINITE);
                         snackbar.setAction("OK", new View.OnClickListener() {
                             @Override
                             public void onClick(View view) {
                                 snackbar.dismiss();
                             }
                         });
                         snackbar.setActionTextColor(getResources().getColor(R.color.red));
                         snackbar.show();
                     }
                 }
             });
 }
 public void login(String email, String password){
     if(!cekform()){
         return;
     }
     mFire.signInWithEmailAndPassword(email, password)
             .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()) {
                         //  login sucess
                         //  go to dashboard
                         Intent le = new Intent(login.this, MainActivity.class);
                         startActivity(le);
                     } else {
                         //  login failed
                         Toast.makeText(login.this,"Login failed. Your username " +
                                 "and password is not matched",Toast.LENGTH_SHORT ).show();
                     }
                 }
             });
 }

    @Override
    public void onClick(View view) {
    if(daftar.getId()==view.getId()){
        regis(email.getText().toString().trim(),password.getText().toString().trim());
    }else if (login.getId()==view.getId()){
        login(email.getText().toString().trim(),password.getText().toString().trim());
    }
    }
}