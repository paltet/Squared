package com.example.paualtet.squared;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.content.ContentValues.TAG;

class TileView extends android.support.v7.widget.AppCompatButton{

    private static final String TAG = "GAMEFIELD";

    public int x, y, value, background;

    public TileView(Context context, int x, int y, int value){

        super(context);

        this.x = x;
        this.y = y;
        this.value = value;

        this.setTextSize(30);
        this.setBackgroundResource(android.R.drawable.btn_default);

        if (value == 0){
            setBackgroundResource(R.drawable.blank);
        }
        else this.setText(String.valueOf(value));
    }

    public void updateTile(){
        if (value == 0){
            setBackgroundResource(R.drawable.blank);
            setText("");
        }
        else {
            this.setText(String.valueOf(value));
            this.setBackgroundResource(android.R.drawable.btn_default);
        }
    }
}
