package com.example.pc.rainbowsixsiege_lfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsScreen extends AppCompatActivity {

    Spinner ranksSpinner;
    Button browseButton;
    Button saveButton;
    ImageView profilePicture;
    private static final int SELECT_PHOTO = 1;
    Uri profilePictureUri;
    public static String rankText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        //Getting reference to widgets
        ranksSpinner = (Spinner) findViewById(R.id.ranksSpinner);
        browseButton = (Button) findViewById(R.id.browseButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        profilePicture = (ImageView) findViewById(R.id.profilePicture);

        //Load prefered settings
        loadData();
    }

    public void browseClicked(View view){
        openGallery();
    }

    private void openGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == SELECT_PHOTO){
            profilePictureUri = data.getData();
            profilePicture.setImageURI(profilePictureUri);
        }
    }

    public void clickSave(View view){
        //Saving data
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("rank", ranksSpinner.getSelectedItemPosition());
        editor.apply();

        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
        rankText = ranksSpinner.getSelectedItem().toString();
    }

    public void loadData(){
        //Load prefered settings that user set last time
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int position = sharedPref.getInt("rank", 0);
        ranksSpinner.setSelection(position);
        rankText = ranksSpinner.getSelectedItem().toString();
    }
}
