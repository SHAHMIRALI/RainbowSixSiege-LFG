package com.example.pc.rainbowsixsiege_lfg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LogInScreen extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;
    Button loginButton;
    Button signupButton;
    public static String loginName;
    public static DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("Users");
    private String temp_key;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        //Getting reference to all widgets
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        firebaseAuth = FirebaseAuth.getInstance();

        //loading preference
        loadData();
    }

    public void loginButtonClicked(View view){
        loginName = usernameInput.getText().toString().trim();

        String username = loginName;
        String password = passwordInput.getText().toString();

        if(username.matches("") || password.matches("")){
            return;
        }

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        final Intent postsScreen = new Intent(this, PostsScreen.class);

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    startActivity(postsScreen);
                    finish();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LogInScreen.this, "Login failed" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Saving data
        SharedPreferences sharedPref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", username);
        editor.putString("password", password);
        editor.apply();
    }

    public void loadData(){
        //Load prefered settings that user set last time
        SharedPreferences sharedPref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        String password = sharedPref.getString("password", "");
        usernameInput.setText(email);
        passwordInput.setText(password);
        loginButton.performClick();
    }

    //User gets registered to online database
    public void signupButtonClicked(View view){
//        Map<String, Object> map = new HashMap<String, Object>();//
//        temp_key = root.push().getKey();
//        root.updateChildren(map);
//
//        DatabaseReference userRoot = root.child(usernameInput.getText().toString());
//        Map<String, Object> map2 = new HashMap<String, Object>();
//        map2.put("name", usernameInput.getText().toString());
//        map2.put("pwd", passwordInput.getText().toString().hashCode() + "");
//
//        userRoot.updateChildren(map2);
//
//        Toast.makeText(LogInScreen.this, "Registered" , Toast.LENGTH_SHORT).show();
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if(username.matches("") || password.matches("")){
            Toast.makeText(LogInScreen.this, "Empty fields" , Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //Register user
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //User is successfully registered
                    progressDialog.dismiss();
                    Toast.makeText(LogInScreen.this, "Registered" , Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LogInScreen.this, "Registration failed" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}