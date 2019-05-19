package com.example.paualtet.squared;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.PriorityQueue;
import java.util.Random;

public class GameField extends AppCompatActivity /*implements View.OnTouchListener, GestureDetector.OnGestureListener*/{

    private static final String TAG = "GAMEFIELD";
    private int size = 0;

    private int rS;
    private LinearLayout l1;
    private int width, height;

    private int nbTiles;
    private int[] values;
    private int[][] ids;
    private int moves;
    private boolean solved;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        
        /*
        --- Getting Configuration ---
         */
        
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        size = extras.getInt("size") + 3;
        
        /*
        --- Activity View ---
         */
        
        setContentView(R.layout.gamefield);

        LinearLayout l1 = (LinearLayout) findViewById(R.id.fieldLandscape);
        l1.removeAllViews();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        updateMoves();
        updateBestScore();
        

        /*
        --- View Click Behaviours ---
         */
        
        Button b = (Button) findViewById(R.id.returnButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        l1.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeRight() {
                moveRight();
            }
            @Override
            public void onSwipeLeft() {
                moveLeft();
            }
            @Override
            public void onSwipeTop() {
                moveUp();
            }
            @Override
            public void onSwipeBottom() {
                moveDown();
            }
        });


       /*
        --- Screen Size Computation ---
         */

        width = (dm.widthPixels-50)/size;
        height = (dm.heightPixels - findViewById(R.id.linearLayout2).getHeight() - findViewById(R.id.linearLayout3).getHeight())/size;

        Log.d(TAG, "amplada real = " + (dm.widthPixels-100));
        Log.d(TAG, "alçada real = " + (dm.heightPixels - findViewById(R.id.linearLayout2).getHeight() - findViewById(R.id.linearLayout3).getHeight()));

        Log.d(TAG, "amplada = " + width);
        Log.d(TAG, "alçada = " + height);

        if (width > height) rS = height;
        else rS = width;

        Log.d(TAG, "rS =  " + rS);





        /*
        --- Grid Inicialization ---
         */
        
        init();
        shuffle();
        display();

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                TileView tv = (TileView) findViewById(ids[i][j]);
                Log.d(TAG, "tv: " + tv.value + " " + tv.getWidth());
            }
        }
    }

    private void init() {

        nbTiles = size * size - 1;
        values = new int[size * size];
        ids = new int[size][size];

        for (int i = 0; i < values.length; i++) {
            values[i] = (i + 1) % values.length;
        }

        solved = false;

        moves = 0;
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

    private void display() {

        LinearLayout l1 = (LinearLayout) findViewById(R.id.fieldLandscape);
        
        int value, id = 0;

        for (int i = 0; i < size; i++){
            LinearLayout l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setGravity(Gravity.CENTER);

            for (int j = 0; j < size; j++){

                value = values[i*size + j];
                TileView tv = new TileView(this, i, j, value);
                
                id++;
                tv.setId(id);
                ids[i][j] = id;
                tv.setOnTouchListener(new OnSwipeTouchListener(this){
                    @Override
                    public void onSwipeRight() {
                        moveRight();
                    }
                    @Override
                    public void onSwipeLeft() {
                        moveLeft();
                    }
                    @Override
                    public void onSwipeTop() {
                        moveUp();
                    }
                    @Override
                    public void onSwipeBottom() {
                        moveDown();
                    }
                });

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(rS, rS);
                tv.setLayoutParams(params);

                l2.addView(tv);
            }
            l1.addView(l2);
        }

    }

    public void moveRight(){
        Log.d(TAG, "moveRight: called");

        if (!solved){
            TileView tv = (TileView) findViewById(idBlank());
            int x = tv.x;
            int y = tv.y;

            if (y > 0) {
                switchTiles(x, y, x, y - 1);
                moves++;
                updateMoves();
                checkSolved();
            }
        }
    }

    public void moveLeft() {
        Log.d(TAG, "moveLeft: called");

        if (!solved) {
            TileView tv = (TileView) findViewById(idBlank());
            int x = tv.x;
            int y = tv.y;

            if (y < size - 1) {
                switchTiles(x, y, x, y + 1);
                moves++;
                updateMoves();
                checkSolved();
            }
        }
    }

    public void moveUp() {
        Log.d(TAG, "moveUp: called");

        if (!solved) {
            TileView tv = (TileView) findViewById(idBlank());
            int x = tv.x;
            int y = tv.y;

            if (x < size - 1) {
                switchTiles(x, y, x + 1, y);
                moves++;
                updateMoves();
                checkSolved();
            }
        }
    }

    public void moveDown() {
        Log.d(TAG, "moveDown: called");

        if (!solved) {
            TileView tv = (TileView) findViewById(idBlank());
            int x = tv.x;
            int y = tv.y;

            if (x > 0) {
                switchTiles(x, y, x - 1, y);
                moves++;
                updateMoves();
                checkSolved();
            }
        }
    }

    public void updateMoves() {
        TextView text = (TextView) findViewById(R.id.clicksTxt);
        text.setText("Moves: " + moves);
    }

    public void updateBestScore() {
        TextView tv = (TextView) findViewById(R.id.minSteps);
        String key;
        switch(size){
            case 3:
                key = "topScore3";
                break;
            case 4:
                key = "topScore4";
                break;
            case 5:
                key = "topScore5";
                break;
            case 6:
                key = "topScore6";
                break;
            default:
                key = "topScore0";
                break;
        };

        SharedPreferences myScore = getSharedPreferences("TopScore", Context.MODE_PRIVATE);
        int bestScore = myScore.getInt(key, 0);
        if (bestScore != 0) tv.setText("Best Score: " + bestScore);
    }

    public int idBlank(){
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (getValue(ids[i][j]) == 0) return ids[i][j];
            }
        }
        return 0;
    }

    public int getValue(int id){
        TileView tv = (TileView) findViewById(id);
        return tv.value;
    }

    public void switchTiles(int x1, int y1, int x2, int y2){

        TileView t1 = (TileView) findViewById(ids[x1][y1]);
        TileView t2 = (TileView) findViewById(ids[x2][y2]);

        int tempV;

        tempV = t1.value;
        t1.value = t2.value;
        t2.value = tempV;

        t1.updateTile();
        t2.updateTile();
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

    public void checkSolved(){

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                int value = getValue(ids[i][j]);
                int expected = i*size +j+1;
                if (value != expected%values.length){
                    solved = false;
                    return;
                }
            }
        }
        solved = true;
        saveScore();
    }

    public void saveScore(){
        SharedPreferences myScore = getSharedPreferences("TopScore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myScore.edit();
        String key;
        switch (size){
            case 3:
                key = "topScore3";
                break;
            default:
                key = "topScore4";
                break;
        }
            int bestScore = myScore.getInt(key, 0);
            if (moves < bestScore || bestScore == 0){
                editor.putInt(key, moves);
                editor.commit();
            }
    }

    public void exit(){

        Intent i = new Intent(this, GameConfig.class);
        startActivity(i);
    }
    
}
