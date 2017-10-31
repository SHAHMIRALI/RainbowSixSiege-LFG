package com.example.pc.rainbowsixsiege_lfg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class PostsScreen extends FragmentActivity {

    public Button postButton;
    public static PostAdapter adaptor;
    public static DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot().child("Posts");//
    private String name, postText, rank, platform, IGN, kdwl, mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_screen);

        postButton = (Button) findViewById(R.id.postButton);

        ListView listView = (ListView) findViewById(R.id.postListView);

        adaptor = new PostAdapter(this);
        listView.setAdapter(adaptor);

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updatePostsScreen(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clickLogout(View view){
        final Intent loginScreen = new Intent(this, LogInScreen.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Logout");
        builder.setMessage("Confirm logout?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();

                        SharedPreferences sharedPref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("email", "");
                        editor.putString("password", "");
                        editor.apply();
                        finish();
                        startActivity(loginScreen);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clickPost(View view){
        Intent makePostScreen = new Intent(this, MakePostScreen.class);
        startActivity(makePostScreen);
    }

    public void clickSettings(View view){
        Intent settingsScreen = new Intent(this, SettingsScreen.class);
        startActivity(settingsScreen);
    }

    //CONSTANTLY UPDATING: NEED TO ADD LIST TO KEEP TRACK OF POSTS//
    private void updatePostsScreen(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){
            IGN = (String) ((DataSnapshot)i.next()).getValue();
            kdwl = (String) ((DataSnapshot)i.next()).getValue();
            mode = (String) ((DataSnapshot)i.next()).getValue();
            name = (String) ((DataSnapshot)i.next()).getValue();
            platform = (String) ((DataSnapshot)i.next()).getValue();
            postText = (String) ((DataSnapshot)i.next()).getValue();
            rank = (String) ((DataSnapshot)i.next()).getValue();
            PostAdapter.postMap.put(new Posts(name, rank, postText, platform, IGN, kdwl, mode), "");
        }


        PostsScreen.adaptor.clear();

        //Only show last 20 posts made
        while(PostAdapter.postMap.size() > 20){
            Posts post = PostAdapter.postMap.keySet().iterator().next();
            PostAdapter.postMap.remove(post);
        }
        //Iterate over PostAdapter.postMap and update adapter
        for(Posts key : PostAdapter.postMap.keySet()){
            PostsScreen.adaptor.insert(key, 0);
        }

    }

}
