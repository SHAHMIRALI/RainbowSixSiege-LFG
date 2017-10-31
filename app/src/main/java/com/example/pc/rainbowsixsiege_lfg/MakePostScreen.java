package com.example.pc.rainbowsixsiege_lfg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.CharacterReader;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MakePostScreen extends AppCompatActivity {

    Button postButton;
    EditText postTextfield;
    Spinner ranksSpinner;
    Spinner platformsSpinner;
    EditText ignInput;
    TextView killDeath;
    TextView winLoss;
    CheckBox rankedBox;
    CheckBox casualBox;
    public static long time = -1;
    String mode = "Ranked";
    private String temp_key;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post_screen);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching stats...");

        postButton = (Button) findViewById(R.id.postButton);
        postTextfield = (EditText) findViewById(R.id.postTextfield);
        ranksSpinner = (Spinner) findViewById(R.id.ranksSpinner);
        platformsSpinner = (Spinner) findViewById(R.id.platformsSpinner);
        ignInput = (EditText) findViewById(R.id.ignInput);
        ignInput.setText("");
        killDeath = (TextView) findViewById(R.id.killDeath);
        winLoss = (TextView) findViewById(R.id.winLoss);
        rankedBox = (CheckBox) findViewById(R.id.rankedBox);
        casualBox = (CheckBox) findViewById(R.id.casualBox);
        killDeath.setText("K/D: N/A");
        winLoss.setText("W/L: N/A");


        ranksSpinner.setSelection(19);
    }

    public void rankedBoxClicked(View view){
        if(!rankedBox.isChecked()){
            rankedBox.setChecked(true);
            mode = rankedBox.getText().toString();
        }
        else{
            casualBox.setChecked(false);
            rankedBox.setChecked(true);
            mode = rankedBox.getText().toString();
        }
    }

    public void casualBoxClicked(View view){
        if(!casualBox.isChecked()){
            casualBox.setChecked(true);
            mode = casualBox.getText().toString();
        }
        else{
            casualBox.setChecked(true);
            rankedBox.setChecked(false);
            mode = casualBox.getText().toString();
        }
    }

    public void getInfo(View view){
        progressDialog.show();
        new findStats(progressDialog ,mode, platformsSpinner.getSelectedItem().toString(), ignInput.getText().toString()).execute();
    }

    public void clickPostButton(View view){

        if(time == -1 || (System.currentTimeMillis() - time) >= 120000) {

            String name = LogInScreen.loginName;
            String IGN = ignInput.getText().toString();
            String rank = ranksSpinner.getSelectedItem().toString();
            String platform = platformsSpinner.getSelectedItem().toString();
            String postText = postTextfield.getText().toString();
            String mode = "";

            if(rankedBox.isChecked()){
                mode = rankedBox.getText().toString();
            }
            else{
                mode = casualBox.getText().toString();
            }

            if (rank.matches("")) {
                Toast.makeText(MakePostScreen.this, "Please save rank in settings", Toast.LENGTH_SHORT).show();
                finish();
                return;
            } else if (postText.matches("")) {
                Toast.makeText(MakePostScreen.this, "Empty post", Toast.LENGTH_SHORT).show();
                return;
            }else if(IGN.matches("")){
                Toast.makeText(MakePostScreen.this, "Enter IGN", Toast.LENGTH_SHORT).show();
            }
            else {
                //Sending post data to online database
                Map<String, Object> map = new HashMap<String, Object>();//
                temp_key = PostsScreen.root.push().getKey();
                PostsScreen.root.updateChildren(map);

                DatabaseReference postRoot = PostsScreen.root.child(temp_key);
                LinkedHashMap<String, Object> map2 = new LinkedHashMap<String, Object>();
                map2.put("name", "Username: " + name);
                map2.put("IGN", "IGN: " + IGN);
                map2.put("rank", "Rank: " + rank);
                map2.put("platform", "Platform: " + platform);
                map2.put("postText", postText);
                map2.put("kdwl", killDeath.getText().toString() + " " + winLoss.getText().toString());
                map2.put("mode", "Mode: " + mode);

                postRoot.updateChildren(map2);

                Toast.makeText(MakePostScreen.this, "Posted", Toast.LENGTH_SHORT).show();

                time = System.currentTimeMillis();
                finish();
            }
        }

        else{
            Toast.makeText(MakePostScreen.this, "Wait before posting again", Toast.LENGTH_SHORT).show();
        }

    }

    public class findStats extends AsyncTask<Void, Void, Void>{

        String words;
        String platform;
        String mode;
        String ign;
        String kd;
        boolean foundKd = false;
        boolean foundWl = false;
        String wl;
        String wins;
        String losses;
        Document doc;
        ProgressDialog progressDialog;

        public findStats(ProgressDialog progressDialog, String mode,String platform, String ign){
            if(platform.matches("PS4")){
                this.platform = "ps4";
            }
            else if(platform.contains("XBOX")){
                this.platform = "xone";
            }
            else{
                this.platform = "uplay";
            }

            //Ranked or casual
            if(mode.matches("Ranked")){
                this.mode = "ranked";
            }
            else{
                this.mode = "casual";
            }

            this.ign = ign;
            this.progressDialog = progressDialog;

        }

        @Override
        protected Void doInBackground(Void... params) {

//            try{
//                Document doc = Jsoup.connect("https://r6stats.com/stats/" + platform + "/" +
//                        ign + "/ranked").get();
//                words = doc.text();
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//
//            return null;

            try{
                doc = Jsoup.connect("https://r6stats.com/stats/" + platform + "/" + ign + "/" + mode).get();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Elements elements = doc.getElementsByClass("value");

            String kdratio = "";
            String wlratio= "";

            if(elements.size() >= 4) {
                kdratio = elements.get(2).toString();
                wlratio = elements.get(4).toString();
            }
            else{
                progressDialog.dismiss();
                Toast.makeText(MakePostScreen.this, "Could not fetch stats", Toast.LENGTH_SHORT).show();
                return;
            }

            kd = "";
            wl = "";
            wins = "";

            for(int i = 0; i<kdratio.length(); i++){
                char c = kdratio.charAt(i);
                if(c == '.'){
                    foundKd = true;
                    kd+=c;
                }
                if(Character.isDigit(c)){
                    kd += c;
                }
            }

            if(wlratio.length() >= 18) {
                for (int i = 18; i < wlratio.length(); i++) {
                    char c = wlratio.charAt(i);
                    if (c == '-') {
                        foundWl = true;
                        wl += c;
                    }
                    if (Character.isDigit(c)) {
                        wl += c;
                    }

                }
            }

            if (foundKd && foundWl) {
                killDeath.setText("K/D: " + kd);
                winLoss.setText("W/L: " + wl);
                progressDialog.dismiss();
            }
            else{
                killDeath.setText("K/D: N/A");
                winLoss.setText("W/L: N/A");
                progressDialog.dismiss();
                Toast.makeText(MakePostScreen.this, "Could not fetch stats", Toast.LENGTH_SHORT).show();
            }
//            char k;
//            kd = "";
//            double wl;
//            wins = "";
//            losses = "";
//            //Extracting kd
//            for(int i = 0; i < words.length(); i++){
//                char c = words.charAt(i);
//                if(c == 'K' && words.charAt(i+1) == '/' && words.charAt(i+2) == 'D'){
//                    foundKd = true;
//                    i = i+4;
//                    k = words.charAt(i);
//                    while(Character.isDigit(k) || k == '.'){
//                        kd += k;
//                        i+=1;
//                        k = words.charAt(i);
//                    }
//                }
//                if(c == 'R' && words.charAt(i+1) == 'e' && words.charAt(i+2) == 'c'&& words.charAt(i+3) == 'o'
//                         && words.charAt(i+4) == 'r' && words.charAt(i+5) == 'd'){
//                    foundWl = true;
//                    i = i+7;
//                    k = words.charAt(i);
//                    while(Character.isDigit(k)){
//                        wins += k;
//                        i+=1;
//                        k = words.charAt(i);
//                    }
//                    i = i+3;
//                    k = words.charAt(i);
//                    while(Character.isDigit(k)){
//                        losses += k;
//                        i+=1;
//                        k = words.charAt(i);
//                    }
//                }
//            }
//            if(foundKd && foundWl) {
//                wl = (Double.parseDouble(wins) / (Double.parseDouble(losses)));
//                wl = Math.round(wl * 100);
//                wl = wl / 100;
//                killDeath.setText("K/D: " + kd);
//                winLoss.setText("W/L: " + wl);
//            }
//            else{
//                killDeath.setText("K/D: N/A");
//                winLoss.setText("W/L: N/A");
//            }
        }
    }

}
