package com.example.pc.rainbowsixsiege_lfg;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by pc on 2017-07-11.
 */

public class PostAdapter extends ArrayAdapter<Posts> {

    public static LinkedHashMap<Posts, String> postMap = new LinkedHashMap<>();

    public PostAdapter(@NonNull Context context) {
        super(context, R.layout.list_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater shahmirsInflater = LayoutInflater.from(getContext());
        View customView = shahmirsInflater.inflate(R.layout.list_item, parent, false);

        Posts singePostItem = getItem(position);
        TextView tt = (TextView) customView.findViewById(R.id.toptext);
        TextView tt2 = (TextView) customView.findViewById(R.id.toptext2);
        TextView mt = (TextView) customView.findViewById(R.id.middletext);
        TextView mt2 = (TextView) customView.findViewById(R.id.middletext2);
        TextView bt = (TextView) customView.findViewById(R.id.bottomtext);
        TextView kdwl = (TextView) customView.findViewById(R.id.kdwltext);

        tt.setText(singePostItem.getIGN());
        tt2.setText(singePostItem.getMode());
        mt.setText(singePostItem.getPlatform());
        mt2.setText(singePostItem.getRank());
        bt.setText(singePostItem.getDescription());
        kdwl.setText(singePostItem.getKdwl());

        //platform text color
        if(singePostItem.getPlatform().matches("Platform: PS4")){
            mt.setTextColor(Color.parseColor("#190BEE"));
        }
        else if(singePostItem.getPlatform().contains("XBOX")){
            mt.setTextColor(Color.parseColor("#4EF108"));
        }
        else{
            mt.setTextColor(Color.parseColor("#F7FF00"));
        }

        //Rank text color
        if(singePostItem.getRank().contains("Diamond")){
            mt2.setTextColor(Color.parseColor("#00FFF0"));
        }
        else if(singePostItem.getRank().contains("Platinum")){
            mt2.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else if(singePostItem.getRank().contains("Gold")){
            mt2.setTextColor(Color.parseColor("#D4DC26"));
        }
        else if(singePostItem.getRank().contains("Silver")){
            mt2.setTextColor(Color.parseColor("#95958F"));
        }
        else if(singePostItem.getRank().contains("Bronze")){
            mt2.setTextColor(Color.parseColor("#884C08"));
        }
        else{
            mt2.setTextColor(Color.parseColor("#761308"));
        }

        return customView;
    }

    @Override
    public void insert(@Nullable Posts object, int index) {
        super.insert(object, index);
    }
}
