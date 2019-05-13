package com.example.paualtet.squared;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class GameField extends AppCompatActivity {

    private int size = 0;
    private static final int[] icons = new int[] {
            R.drawable.blank,
            R.drawable.icon1,
            R.drawable.icon2,
            R.drawable.icon3,
            R.drawable.icon4,
            R.drawable.icon5,
            R.drawable.icon6,
            R.drawable.icon7,
            R.drawable.icon8,
            R.drawable.icon9,
            R.drawable.icon10,
            R.drawable.icon11,
            R.drawable.icon12,
            R.drawable.icon13,
            R.drawable.icon14,
            R.drawable.icon15,
    };

    private int rS;
    private LinearLayout l1;
    private int width, height;

    private int nbTiles;
    private int[] values;
    private int[][] ids;
    private int moves;

    private static int BLANK_UP = 1, BLANK_DOWN = 2, BLANK_LEFT = 3, BLANK_RIGHT = 4, NO_BLANK = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamefield);

        Bundle extras = getIntent().getExtras();
        size = extras.getInt("size") + 3;

        LinearLayout l1 = (LinearLayout) findViewById(R.id.fieldLandscape);
        l1.removeAllViews();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        TextView text = (TextView) findViewById(R.id.clicksTxt);
        text.setText("Size: " + size);

        width = dm.widthPixels / size;
        height = (dm.widthPixels - 180) /size;

        if (width > height) rS = height;
        else rS = width;

        Button b = (Button) findViewById(R.id.returnButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        init();
        //tv = new TileView();
    }

    private void init(){

        nbTiles = size*size -1;
        values = new int[size*size];
        ids = new int[size][size];

        for (int i = 0; i < values.length; i++){
            values[i] = (i+1)%values.length;
        }

        moves = 0;

        LinearLayout l1 = (LinearLayout) findViewById(R.id.fieldLandscape);

        shuffle();


        int value, id = 0;

        for (int i = 0; i < size; i++){
            LinearLayout l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setGravity(Gravity.CENTER);

            for (int j = 0; j < size; j++){

                value = values[i*size + j];
                TileView tv = new TileView(this, i, j, value, icons[value]);
                id++;
                tv.setId(id);
                ids[i][j] = id;
                tv.setHeight(rS);
                tv.setWidth(rS);
                tv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view){
                        hasClick(((TileView)view).x, ((TileView)view).y);
                    }
                });
                l2.addView(tv);
            }
            l1.addView(l2);
        }

    }

    private void shuffle(){

        Random r = new Random();

        do {
            for (int i = 0; i < values.length; i++) {
                int randomPosition = r.nextInt(values.length);
                int temp = values[i];
                values[i] = values[randomPosition];
                values[randomPosition] = temp;
            }
        } while (!isSolvable());
    }

    public void hasClick(int x, int y){

        TextView tv = (TextView) findViewById(R.id.clicksTxt);

        if (!solved()) {
            tv.setText("Moves: " + moves);
            move(x, y);
        }

        if (solved()){
            tv.setTextColor(Color.RED);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setText("WIN Moves:" + moves);
        }
    }

    public void move(int x, int y){
        int action = scanPosition(x, y);
        switch (action){
            case 1: //blank up
                switchTiles(x, y, x+1, y);
                moves++;
                break;

            case 2: //blank down
                switchTiles(x, y, x-1, y);
                moves++;
                break;

            case 3: //blank left
                switchTiles(x, y, x, y-1);
                moves++;
                break;

            case 4: //blank right
                switchTiles(x, y, x, y+1);
                moves++;
                break;
        }
    }

    public int scanPosition(int x, int y){
        if (x > 0){
            if (getValue(ids[x-1][y]) == 0) return BLANK_DOWN;
        }
        if (x < size -1){
            if (getValue(ids[x+1][y]) == 0) return BLANK_UP;
        }
        if (y > 0){
            if (getValue(ids[x][y-1]) == 0) return BLANK_LEFT;
        }
        if (y < size -1){
            if (getValue(ids[x][y+1]) == 0) return BLANK_RIGHT;
        }
        return NO_BLANK;
    }

    public int getValue(int id){
        TileView tv = (TileView) findViewById(id);
        return tv.value;
    }

    public void switchTiles(int x1, int y1, int x2, int y2){

        TileView t1 = (TileView) findViewById(ids[x1][y1]);
        TileView t2 = (TileView) findViewById(ids[x2][y2]);

        int tempB, tempV;

        tempB = t1.background;
        tempV = t1.value;

        t1.value = t2.value;
        t1.background = t2.background;
        t1.setBackgroundResource(t1.background);

        t2.background = tempB;
        t2.value = tempV;
        t2.setBackgroundResource(t2.background);

    }

    public boolean isSolvable(){
        int inversions = 0;

        for (int i = 0; i < values.length; i++){
            if (values[i] != 0) {
                for (int j = 0; j < i; j++) {
                    if (values[j] > values[i]) inversions++;
                }
            }
        }

        return inversions%2 == 0;
    }

    public boolean solved(){

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                int value = getValue(ids[i][j]);
                int expected = i*size +j+1;
                if (value != expected%values.length) return false;
            }
        }
        return true;
    }

    public void exit(){

        Intent i = new Intent(this, GameConfig.class);
        startActivity(i);
    }
}
